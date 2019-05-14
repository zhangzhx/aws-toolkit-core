package com.amazonaws.toolkit.core.hackathon;

import com.amazonaws.toolkit.core.hackathon.analytics.ToolkitEvent;

// Master commit 1
public class ActionContext {
    private final ToolkitEvent event;
    private final ToolkitLogger logger;
    private final ToolkitProgresser progresser;

    public ActionContext(ToolkitEvent event,
            ToolkitLogger logger,
            ToolkitProgresser progresser) {
        this.event = event;
        this.logger = logger;
        this.progresser = progresser;
    }

    public ToolkitEvent getEvent() {
        return event;
    }

    public ToolkitLogger getLogger() {
        return logger;
    }

    public ToolkitProgresser getProgresser() {
        return progresser;
    }
}
