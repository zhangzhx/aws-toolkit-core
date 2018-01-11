package com.amazonaws.toolkit.core.hackathon.models;

//TODO
public class DeployLambdaFunctionInput {
    private AwsScope awsScope;
    private LambdaFunctionConfiguration functionConfiguration;

    private Boolean publishNewVersion;
    private Boolean publishNewAlias;
    private String alias;

    private String targetArtifactLocation;

    private UploadToS3Input s3Input;
}
