package com.amazonaws.toolkit.core.hackathon.models;

public class AwsScope {
    private final String profile;
    private final String region;

    // TODO validation
    public AwsScope(String profile, String region) {
        this.profile = profile;
        this.region = region;
    }

    public String getProfile() {
        return profile;
    }

    public String getRegion() {
        return region;
    }
}
