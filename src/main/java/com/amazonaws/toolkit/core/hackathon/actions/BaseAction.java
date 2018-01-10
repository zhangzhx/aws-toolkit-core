package com.amazonaws.toolkit.core.hackathon.actions;

import java.io.IOException;

import com.amazonaws.toolkit.core.hackathon.ActionContext;
import com.amazonaws.toolkit.core.hackathon.ActionInfo;
import com.amazonaws.toolkit.core.hackathon.analytics.ToolkitEvent;
import com.amazonaws.toolkit.core.hackathon.models.ActionException;
import com.amazonaws.toolkit.core.hackathon.models.ActionInput;
import com.amazonaws.toolkit.core.hackathon.models.ActionOutput;

public abstract class BaseAction<I extends ActionInput, O extends ActionOutput, E extends ActionException> {
    private final ActionInfo actionInfo;

    protected BaseAction(ActionInfo actionInfo) {
        this.actionInfo = actionInfo;
    }

    public O execute(I input, ActionContext context) throws E {
        ToolkitEvent metrics = context.getEvent();
        try {
            long startTimeMilli = System.currentTimeMillis();
            O output = doExecute(input, context);
            long time = System.currentTimeMillis() - startTimeMilli;
            metrics.addMetric("Execution time milli", time);
            metrics.addAttribute("Result", output.getResult().name());
            return output;
        } catch (ActionException e) {
            metrics.addAttribute("Result", ActionOutput.ActionResult.FAILED.name());
            throw e;
        } finally {
            // TODO to be removed in the prod code
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

    protected abstract O doExecute(I input, ActionContext context) throws E;
}
