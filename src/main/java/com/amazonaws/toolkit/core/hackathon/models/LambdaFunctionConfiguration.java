package com.amazonaws.toolkit.core.hackathon.models;

public class LambdaFunctionConfiguration {
    private final String functionName;
    private final String description;
    private final String handler;
    private final String role;
    private final Integer memorySize;
    private final Integer timeout;

    public LambdaFunctionConfiguration(String functionName, String description, String handler, String role,
            Integer memorySize, Integer timeout) {
        this.functionName = functionName;
        this.description = description;
        this.handler = handler;
        this.role = role;
        this.memorySize = memorySize;
        this.timeout = timeout;
    }

    public String getFunctionName() {
        return functionName;
    }

    public String getDescription() {
        return description;
    }

    public String getHandler() {
        return handler;
    }

    public String getRole() {
        return role;
    }

    public Integer getMemorySize() {
        return memorySize;
    }

    public Integer getTimeout() {
        return timeout;
    }
}
