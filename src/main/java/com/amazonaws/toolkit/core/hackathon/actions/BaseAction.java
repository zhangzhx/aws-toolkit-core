package com.amazonaws.toolkit.core.hackathon.actions;

import java.io.IOException;

import com.amazonaws.toolkit.core.hackathon.ActionContext;
import com.amazonaws.toolkit.core.hackathon.ActionInfo;
import com.amazonaws.toolkit.core.hackathon.IActionExecutionContextProvider;
import com.amazonaws.toolkit.core.hackathon.analytics.ToolkitEvent;
import com.amazonaws.toolkit.core.hackathon.analytics.ui.EventPublisherProvider;
import com.amazonaws.toolkit.core.hackathon.models.ActionException;
import com.amazonaws.toolkit.core.hackathon.models.ActionInput;
import com.amazonaws.toolkit.core.hackathon.models.ActionOutput;

public abstract class BaseAction<I extends ActionInput, O extends ActionOutput, E extends ActionException> {
    private final ActionInfo actionInfo;
    private final ToolkitEvent metrics;
    private final IActionExecutionContextProvider<I> contextProvider;

    protected BaseAction(ActionInfo actionInfo, IActionExecutionContextProvider<I> contextProvider) {
        this.actionInfo = actionInfo;
        this.contextProvider = contextProvider;
        this.metrics = EventPublisherProvider.INSTANCE.getEventPublisher().createEvent(actionInfo.getName());
    }

    public O execute() throws E {
        I input = contextProvider.createActionInput();
        ActionContext context = createActionContext();
        try {
            long startTimeMilli = System.currentTimeMillis();
            O output = doExecute(input, context);
            long time = System.currentTimeMillis() - startTimeMilli;
            metrics.addMetric("Execution time milli", time);
            if (output.isSucceeded()) {
                metrics.addAttribute("Result", output.getResult().name());
            }
            return output;
        } catch (ActionException e) {
            metrics.addAttribute("Result", ActionOutput.ActionResult.FAILED.name());
            throw e;
        } finally {
            ToolkitEvent.print(context.getLogger(), metrics);
            metrics.record();
            try {
                context.getLogger().close();
            } catch (IOException e) {
                //TODO What to do?
            }
        }
    }

    public ActionInfo getActionInfo() {
        return this.actionInfo;
    }

    private ActionContext createActionContext() {
        return new ActionContext(metrics,
                contextProvider.createLogger(),
                contextProvider.createProgresser());
    }

    protected abstract O doExecute(I input, ActionContext context) throws E;
}
