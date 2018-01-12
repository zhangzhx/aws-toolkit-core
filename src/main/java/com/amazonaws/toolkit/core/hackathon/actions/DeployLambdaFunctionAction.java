package com.amazonaws.toolkit.core.hackathon.actions;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import org.apache.commons.io.FileUtils;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.lambda.AWSLambda;
import com.amazonaws.services.lambda.AWSLambdaClient;
import com.amazonaws.services.lambda.model.CreateFunctionRequest;
import com.amazonaws.services.lambda.model.FunctionCode;
import com.amazonaws.services.lambda.model.GetFunctionRequest;
import com.amazonaws.services.lambda.model.ResourceNotFoundException;
import com.amazonaws.services.lambda.model.Runtime;
import com.amazonaws.services.lambda.model.UpdateFunctionCodeRequest;
import com.amazonaws.services.lambda.model.UpdateFunctionConfigurationRequest;
import com.amazonaws.toolkit.core.hackathon.ActionContext;
import com.amazonaws.toolkit.core.hackathon.ActionInfo;
import com.amazonaws.toolkit.core.hackathon.ToolkitLogger;
import com.amazonaws.toolkit.core.hackathon.ToolkitProgresser;
import com.amazonaws.toolkit.core.hackathon.analytics.ToolkitEvent;
import com.amazonaws.toolkit.core.hackathon.analytics.ui.EventPublisherProvider;
import com.amazonaws.toolkit.core.hackathon.models.ActionException;
import com.amazonaws.toolkit.core.hackathon.models.ActionOutput;
import com.amazonaws.toolkit.core.hackathon.models.ActionOutput.ActionResult;
import com.amazonaws.toolkit.core.hackathon.models.DeployLambdaFunctionInput;
import com.amazonaws.toolkit.core.hackathon.models.LambdaFunctionConfiguration;

public class DeployLambdaFunctionAction extends BaseAction<DeployLambdaFunctionInput, ActionOutput, ActionException>{
    private static final int K = 1024;
    private static final int M = 1024 * K;
    private static final int DEPLOYMENT_PACKAGE_SIZE = 20 * M;  // 20M
    private static final String INVALID_PARAMETER_ERROR_MESSAGE = "Invalid parameter";

    public DeployLambdaFunctionAction() {
        super(ActionInfo.DEPLOY_LAMBDA_FUNCTION);
    }

    @Override
    protected ActionOutput doExecute(DeployLambdaFunctionInput input, ActionContext context) throws ActionException {
        AWSLambda lambda = AWSLambdaClient.builder()
                .withCredentials(new ProfileCredentialsProvider(input.getAwsScope().getProfile()))
                .withRegion(input.getAwsScope().getRegion())
                .build();

        ToolkitLogger logger = context.getLogger();
        ToolkitEvent metrics = context.getEvent();
        ToolkitProgresser progresser = context.getProgresser();

        File artifact = new File(input.getTargetArtifactLocation());
        if (!artifact.exists() || !artifact.isFile()) {
            String errorMessage = "Provided artifact file path is not valid!";
            logger.error("%s: %s", INVALID_PARAMETER_ERROR_MESSAGE, errorMessage);
            throw new ActionException(INVALID_PARAMETER_ERROR_MESSAGE, new IllegalArgumentException(errorMessage));
        }
        boolean needToUploadToS3 = needToUseS3Location(artifact);
        metrics.addBooleanMetric("Upload to S3", needToUploadToS3);
        metrics.addMetric("Artifact length:", artifact.length());

        if (needToUploadToS3) {
            if (input.getS3Input() == null) {
                String errorMessage = "S3 location must be specified as the artifact zip file is too big.";
                logger.error(errorMessage);
                throw new ActionException(INVALID_PARAMETER_ERROR_MESSAGE, new IllegalArgumentException(errorMessage));
            }
            logger.info("The artifact is bigger than %dM, upload to S3\n", DEPLOYMENT_PACKAGE_SIZE/M);

            //TODO move the event creation to BaseAction
            ActionOutput uploadToS3Output = new UploadToS3Action().execute(input.getS3Input(),
                    new ActionContext(EventPublisherProvider.INSTANCE.getEventPublisher().createEvent(ActionInfo.UPLOAD_TO_S3.getName()),
                            logger, progresser));
            if (uploadToS3Output.getResult() != ActionResult.SUCCEEDED) {
                return uploadToS3Output;
            }
        }

        progresser.beginTask("Deploying to AWS Lambda");
        LambdaFunctionConfiguration config = input.getFunctionConfiguration();
        try {
            lambda.getFunction(new GetFunctionRequest().withFunctionName(config.getFunctionName()));
            logger.info("Updating configuration for Lambda function %s\n", config.getFunctionName());
            metrics.addBooleanMetric("Create new function", false);
            lambda.updateFunctionConfiguration(new UpdateFunctionConfigurationRequest()
                    .withFunctionName(config.getFunctionName())
                    .withDescription(config.getDescription())
                    .withHandler(config.getHandler())
                    .withMemorySize(config.getMemorySize())
                    .withTimeout(config.getTimeout())
                    .withRole(config.getRole()));

            progresser.workedFraction(0.5);

            UpdateFunctionCodeRequest updateCodeRequest = new UpdateFunctionCodeRequest()
                .withFunctionName(config.getFunctionName())
                .withPublish(input.getPublishNewVersion());
            if (needToUseS3Location(artifact)) {
                updateCodeRequest.withS3Bucket(input.getS3Input().getBucketName())
                    .withS3Key(input.getS3Input().getKeyPrefix());
            } else {
                try {
                    ByteBuffer byteBuffer = ByteBuffer.wrap(FileUtils.readFileToByteArray(artifact));
                    updateCodeRequest.withZipFile(byteBuffer);
                } catch (IOException e) {
                    // TODO
                }
            }
            logger.info("Updating code for Lambda function %s\n", config.getFunctionName());
            lambda.updateFunctionCode(updateCodeRequest);
        } catch (ResourceNotFoundException e) {
            // create a new function
            metrics.addBooleanMetric("Create new function", true);
            CreateFunctionRequest createFunctionRequest = new CreateFunctionRequest()
                    .withFunctionName(config.getFunctionName())
                    .withDescription(config.getDescription())
                    .withHandler(config.getHandler())
                    .withRuntime(Runtime.Java8)
                    .withMemorySize(config.getMemorySize())
                    .withTimeout(config.getTimeout())
                    .withPublish(input.getPublishNewVersion())
                    .withRole(config.getRole());
            if (needToUseS3Location(artifact)) {
                createFunctionRequest.withCode(new FunctionCode()
                        .withS3Bucket(input.getS3Input().getBucketName())
                        .withS3Key(input.getS3Input().getKeyPrefix()));
            } else {
                try {
                    ByteBuffer byteBuffer = ByteBuffer.wrap(FileUtils.readFileToByteArray(artifact));
                    createFunctionRequest.withCode(new FunctionCode()
                            .withZipFile(byteBuffer));
                } catch (IOException e1) {
                    // TODO
                }
            }
            logger.info("Create Lambda function %s\n", config.getFunctionName());
            lambda.createFunction(createFunctionRequest);
        }
        progresser.workedFraction(1);
        progresser.done();
        return new ActionOutput(ActionResult.SUCCEEDED);
    }

    private boolean needToUseS3Location(File artifact) {
        return artifact.length() > DEPLOYMENT_PACKAGE_SIZE;
    }
}
