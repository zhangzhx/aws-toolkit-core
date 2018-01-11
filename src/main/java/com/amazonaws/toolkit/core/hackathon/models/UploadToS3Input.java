package com.amazonaws.toolkit.core.hackathon.models;

public class UploadToS3Input extends ActionInput {
    private final AwsScope awsScope;
    private final String sourceFile;
    private final String bucketName;
    private final String keyPrefix;

    private final EncryptionType encryptionType;
    private final String kmsKeyArn;

    public UploadToS3Input(AwsScope awsScope, String sourceFile, String bucketName, String keyPrefix, EncryptionType encryptionType,
            String kmsKeyArn) {
        this.awsScope = awsScope;
        this.sourceFile = sourceFile;
        this.bucketName = bucketName;
        this.keyPrefix = keyPrefix;
        this.encryptionType = encryptionType;
        this.kmsKeyArn = kmsKeyArn;
    }

    public static enum EncryptionType {
        NONE,
        S3,
        KMS
    }

    public AwsScope getAwsScope() {
        return awsScope;
    }

    public String getSourceFile() {
        return sourceFile;
    }

    public String getBucketName() {
        return bucketName;
    }

    public String getKeyPrefix() {
        return keyPrefix;
    }

    public EncryptionType getEncryptionType() {
        return encryptionType;
    }

    public String getKmsKeyArn() {
        return kmsKeyArn;
    }
}
