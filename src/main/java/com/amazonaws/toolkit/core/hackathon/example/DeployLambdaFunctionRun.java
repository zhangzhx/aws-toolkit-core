package com.amazonaws.toolkit.core.hackathon.example;

import com.amazonaws.toolkit.core.hackathon.actions.DeployLambdaFunctionAction;
import com.amazonaws.toolkit.core.hackathon.models.AwsScope;
import com.amazonaws.toolkit.core.hackathon.models.DeployLambdaFunctionInput;
import com.amazonaws.toolkit.core.hackathon.models.LambdaFunctionConfiguration;
import com.amazonaws.toolkit.core.hackathon.models.UploadToS3Input;
import com.amazonaws.toolkit.core.hackathon.models.UploadToS3Input.EncryptionType;

public class DeployLambdaFunctionRun extends AbstractActionRun<DeployLambdaFunctionInput> {
    private static final String artifactLocation = "/Users/zhaoxiz/Workspace/runtime-EclipseApplication/hello-world/target/demo-1.0.0.jar";

    public DeployLambdaFunctionRun() {
        super(new DeployLambdaFunctionAction());
    }

    @Override
    protected DeployLambdaFunctionInput createActionInput() {
        AwsScope scope = new AwsScope("default", "us-east-1");
        DeployLambdaFunctionInput input = new DeployLambdaFunctionInput(
                scope,
                new LambdaFunctionConfiguration(
                        "zhaoxiz-aws-toolkit-core-test",
                        "Demo Lambda function in AWS Toolkit Core",
                        "com.serverless.demo.function.HelloWorld",
                        "arn:aws:iam::539686528318:role/LambdaRole",
                        512, 300),
                false,
                false,
                "somealias",
                artifactLocation,
                new UploadToS3Input(
                        scope,
                        artifactLocation,
                        "zhaoxiz-aws-toolkit-core-test",
                        "lambda-function-aws-toolkit-core-test",
                        EncryptionType.NONE,
                        null));
        return input;
    }
}
