package com.amazonaws.toolkit.core.hackathon.actions;

import com.amazonaws.toolkit.core.hackathon.ActionContext;
import com.amazonaws.toolkit.core.hackathon.ActionInfo;
import com.amazonaws.toolkit.core.hackathon.IActionExecutionContextProvider;
import com.amazonaws.toolkit.core.hackathon.ToolkitLogger;
import com.amazonaws.toolkit.core.hackathon.ToolkitProgresser;
import com.amazonaws.toolkit.core.hackathon.analytics.ToolkitEvent;
import com.amazonaws.toolkit.core.hackathon.models.ActionException;
import com.amazonaws.toolkit.core.hackathon.models.ActionOutput;
import com.amazonaws.toolkit.core.hackathon.models.ActionOutput.ActionResult;
import com.amazonaws.toolkit.core.hackathon.models.FooInput;

public class FooAction extends BaseAction<FooInput, ActionOutput, ActionException>{

    public FooAction(IActionExecutionContextProvider<FooInput> provider) {
        super(ActionInfo.FOO, provider);
    }

    @Override
    public ActionOutput doExecute(FooInput input, ActionContext context) throws ActionException {
        ToolkitEvent metrics = context.getEvent();
        ToolkitProgresser progresser = context.getProgresser();
        ToolkitLogger logger = context.getLogger();

        progresser.beginTask("Executing Foo Action");
        for (int i = 0; i < 10; i++) {
            if (progresser.isCanceled()) {
                // TODO clean up the task safely
                logger.error("The action is canceled at %d seconds.\n", i);
                metrics.addMetric("Execute Times", i);
                return new ActionOutput(ActionResult.CANCELED);
            }
            try {
                Thread.sleep(1000);
                logger.info("I have slept for %d seconds in project %s.\n", i+1, input.getProjectName());
//                if (new Random().nextInt(12) == 1) {
//                    throw new ActionException("Random assertion failed!", null);
//                }
                progresser.workedFraction(0.1);
            } catch (InterruptedException e) {
                // Allowed exception, we handle here.
            }
        }
        progresser.done();
        return new ActionOutput(ActionResult.SUCCEEDED);
    }
}
