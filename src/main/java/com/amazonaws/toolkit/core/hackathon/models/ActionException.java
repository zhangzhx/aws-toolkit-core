package com.amazonaws.toolkit.core.hackathon.models;

public class ActionException extends RuntimeException {
    public ActionException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
