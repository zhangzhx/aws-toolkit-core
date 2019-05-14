package com.amazonaws.toolkit.core.hackathon;

import com.amazonaws.toolkit.core.hackathon.models.ActionInput;

/**
 * Upper level toolkit must provide implementation to this interface.
 *
 * @param <I> The concrete ActionInput
 */
// Another new comment
public interface IActionExecutionContextProvider<I extends ActionInput> {

    // Create the Logger to be used by the action
    ToolkitLogger createLogger();

    // Create the progress manager to be used by the action
    ToolkitProgresser createProgresser();

    // Collect action input for the action.
    I createActionInput();
}
