package com.amazonaws.toolkit.core.hackathon.models;

//TODO All the input pojo should be in builder pattern. Kotlin should be a good fit for this.
public class DeployLambdaFunctionInput extends ActionInput {
    private final AwsScope awsScope;
    private final LambdaFunctionConfiguration functionConfiguration;

    private final Boolean publishNewVersion;
    private final Boolean publishNewAlias;
    private final String alias;

    private final String targetArtifactLocation;

    private final UploadToS3Input s3Input;

    public DeployLambdaFunctionInput(AwsScope awsScope, LambdaFunctionConfiguration functionConfiguration,
            Boolean publishNewVersion, Boolean publishNewAlias, String alias, String targetArtifactLocation,
            UploadToS3Input s3Input) {
        this.awsScope = awsScope;
        this.functionConfiguration = functionConfiguration;
        this.publishNewVersion = publishNewVersion;
        this.publishNewAlias = publishNewAlias;
        this.alias = alias;
        this.targetArtifactLocation = targetArtifactLocation;
        this.s3Input = s3Input;
    }

    public AwsScope getAwsScope() {
        return awsScope;
    }

    public LambdaFunctionConfiguration getFunctionConfiguration() {
        return functionConfiguration;
    }

    public Boolean getPublishNewVersion() {
        return publishNewVersion;
    }

    public Boolean getPublishNewAlias() {
        return publishNewAlias;
    }

    public String getAlias() {
        return alias;
    }

    public String getTargetArtifactLocation() {
        return targetArtifactLocation;
    }

    public UploadToS3Input getS3Input() {
        return s3Input;
    }
}
