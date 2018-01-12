package com.amazonaws.toolkit.core.hackathon.example;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import org.apache.commons.io.FileUtils;

import com.amazonaws.toolkit.core.hackathon.actions.UploadToS3Action;
import com.amazonaws.toolkit.core.hackathon.models.AwsScope;
import com.amazonaws.toolkit.core.hackathon.models.UploadToS3Input;

public class UploadToS3Run extends AbstractActionRun<UploadToS3Input> {

    protected UploadToS3Run() {
        super(new UploadToS3Action());
    }

    @Override
    protected UploadToS3Input createActionInput() {
        UploadToS3Input input = new UploadToS3Input(
                new AwsScope("default", "us-east-1"),
                "/Users/zhaoxiz/Desktop/aws-toolkit-core",
                "zhaoxiz-aws-toolkit-core-test",
                action.getActionInfo().getName(),
                UploadToS3Input.EncryptionType.NONE,
                null);
        return input;
    }

    private static String createTempFile(long size) {
        File tempFile;
        try {
            tempFile = File.createTempFile("tempfile", ".txt");
            for (int i = 0; i < size; ) {
                String uuid = UUID.randomUUID().toString() + "\n";
                FileUtils.writeStringToFile(tempFile, uuid, StandardCharsets.UTF_8, true);
                i += uuid.length();
            }

            return tempFile.getAbsolutePath();
        } catch (IOException e) {
            throw new RuntimeException("Can't create a tempory file!", e);
        }
    }
}
