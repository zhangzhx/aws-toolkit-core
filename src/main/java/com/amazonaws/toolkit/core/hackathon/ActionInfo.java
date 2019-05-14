package com.amazonaws.toolkit.core.hackathon;

// Master commit 2
public enum ActionInfo {
    UPLOAD_TO_S3("UploadToS3", "Upload to S3"),
    DEPLOY_LAMBDA_FUNCTION("DeployLambdaFunction", "Deploy Lambda function"),
    FOO("Foo", "Foo Action"),
    ;

    // This name for each action must be unique
    private final String name;
    private final String displayName;

    private ActionInfo(String name, String displayName) {
        this.name = name;
        this.displayName = displayName;
    }

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        return displayName;
    }
}
