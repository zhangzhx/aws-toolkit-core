package com.amazonaws.toolkit.core.hackathon.actions;

import java.io.File;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.event.ProgressEvent;
import com.amazonaws.event.ProgressListener;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CreateBucketRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.transfer.Transfer.TransferState;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.amazonaws.services.s3.transfer.Upload;
import com.amazonaws.toolkit.core.hackathon.ActionContext;
import com.amazonaws.toolkit.core.hackathon.ActionInfo;
import com.amazonaws.toolkit.core.hackathon.ToolkitLogger;
import com.amazonaws.toolkit.core.hackathon.ToolkitProgresser;
import com.amazonaws.toolkit.core.hackathon.models.ActionException;
import com.amazonaws.toolkit.core.hackathon.models.ActionOutput;
import com.amazonaws.toolkit.core.hackathon.models.ActionOutput.ActionResult;
import com.amazonaws.toolkit.core.hackathon.models.UploadToS3Input;

public class UploadToS3Action extends BaseAction<UploadToS3Input, ActionOutput, ActionException> {
    private static final String INVALID_PARAMETER_ERROR_MESSAGE = "Invalid parameter";

    public UploadToS3Action() {
        super(ActionInfo.UPLOAD_TO_S3);
    }

    @Override
    protected ActionOutput doExecute(UploadToS3Input input, ActionContext context) throws ActionException {
        AmazonS3 s3 = AmazonS3Client.builder()
                .withCredentials(new ProfileCredentialsProvider(input.getAwsScope().getProfile()))
                .withRegion(input.getAwsScope().getRegion())
                .build();
        File sourceFile = new File(input.getSourceFile());
        ToolkitLogger logger = context.getLogger();

        logger.info("Source file location: %s\n", input.getSourceFile());
        logger.info("Target S3 location: s3://%s/%s\n", input.getBucketName(), input.getKeyPrefix());
        if (!sourceFile.exists()) {
            String errorMessage = "The source file " + input.getSourceFile() + "must exist!";
            context.getLogger().error(errorMessage);
            throw new ActionException(INVALID_PARAMETER_ERROR_MESSAGE,
                    new IllegalArgumentException(errorMessage));
        }
        if (!sourceFile.isFile()) {
            String errorMessage = "The source file " + input.getSourceFile() + "must be a file!";
            context.getLogger().error(errorMessage);
            throw new ActionException(INVALID_PARAMETER_ERROR_MESSAGE,
                    new IllegalArgumentException(errorMessage));
        }
        TransferManager tm = TransferManagerBuilder.standard()
                .withS3Client(s3)
                .build();
        boolean bucketExists = s3.doesBucketExistV2(input.getBucketName());
        context.getEvent().addBooleanMetric("Bucket exists", bucketExists);
        if (!bucketExists) {
            logger.warning("Target bucket %s doesn't exist, create one...\n", input.getBucketName());
            s3.createBucket(new CreateBucketRequest(input.getBucketName()));
        }
        ToolkitProgresser progresser = context.getProgresser();
        progresser.beginTask("Uploading to Amazon S3");

        Upload upload = tm.upload(new PutObjectRequest(input.getBucketName(), input.getKeyPrefix(), sourceFile));
        upload.addProgressListener(new ProgressListener() {
            @Override
            public void progressChanged(ProgressEvent e) {
                TransferState state = upload.getState();
                if (state == TransferState.InProgress) {
                    progresser.workedFraction(upload.getProgress().getPercentTransferred()/100);
                }
            }
        });
        try {
            upload.waitForCompletion();
        } catch (AmazonClientException | InterruptedException e1) {
            throw new ActionException("Failed to upload to S3.", e1);
        }
        progresser.done();

        ActionResult result;
        switch (upload.getState()) {
        case Completed: result = ActionResult.SUCCEEDED; break;
        case Canceled: result = ActionResult.CANCELED; break;
        case Failed:
        default: result = ActionResult.FAILED; break;
        }
        tm.shutdownNow();

        return new ActionOutput(result);
    }
}
