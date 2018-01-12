package com.amazonaws.toolkit.core.hackathon.example;

import com.amazonaws.toolkit.core.hackathon.ActionContext;
import com.amazonaws.toolkit.core.hackathon.ToolkitLogger;
import com.amazonaws.toolkit.core.hackathon.ToolkitProgresser;
import com.amazonaws.toolkit.core.hackathon.actions.BaseAction;
import com.amazonaws.toolkit.core.hackathon.analytics.ToolkitEvent;
import com.amazonaws.toolkit.core.hackathon.analytics.ui.EventPublisherProvider;
import com.amazonaws.toolkit.core.hackathon.models.ActionException;
import com.amazonaws.toolkit.core.hackathon.models.ActionInput;
import com.amazonaws.toolkit.core.hackathon.models.ActionOutput;

public abstract class AbstractActionRun<I extends ActionInput> {
    private final ToolkitEvent eventMetrics;
    protected final BaseAction<I, ActionOutput, ActionException> action;

    protected AbstractActionRun(BaseAction<I, ActionOutput, ActionException> action) {
        this.eventMetrics = EventPublisherProvider.INSTANCE.getEventPublisher()
                .createEvent(action.getActionInfo().getName());
        this.action = action;
    }

    public Object execute() {
        I input = createActionInput();
        ToolkitLogger logger = new ToolkitLoggerImpl(action.getActionInfo().getDisplayName());
        ToolkitProgresser progresser = new ToolkitProgresserImpl(logger);

        ActionContext context = new ActionContext(eventMetrics, logger, progresser);
        try {
            action.execute(input, context);
        } catch (ActionException e) {
            // Report error
            throw e;
        }
        return null;
    }

    protected abstract I createActionInput();
}
