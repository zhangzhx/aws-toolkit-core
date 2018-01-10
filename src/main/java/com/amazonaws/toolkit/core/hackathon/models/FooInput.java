package com.amazonaws.toolkit.core.hackathon.models;

public class FooInput extends ActionInput {
    private final String projectName;

    public FooInput(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectName() {
        return projectName;
    }
}
