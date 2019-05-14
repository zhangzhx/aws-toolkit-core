package com.amazonaws.toolkit.core.hackathon.models;

// new comment
public class ActionOutput {
    private final ActionResult result;

    public ActionOutput(ActionResult result) {
        this.result = result;
    }

    public ActionResult getResult() {
        return result;
    }

    public boolean isSucceeded() {
        return result == ActionResult.SUCCEEDED;
    }

    public boolean isCanceled() {
        return result == ActionResult.CANCELED;
    }

    public boolean isFailed() {
        return result == ActionResult.FAILED;
    }

    public static enum ActionResult {
        SUCCEEDED,
        FAILED,
        CANCELED
        ;
    }
}
