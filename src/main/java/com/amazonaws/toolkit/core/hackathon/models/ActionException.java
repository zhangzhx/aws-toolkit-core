package com.amazonaws.toolkit.core.hackathon.models;

// Comment
public class ActionException extends RuntimeException {
    public ActionException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
