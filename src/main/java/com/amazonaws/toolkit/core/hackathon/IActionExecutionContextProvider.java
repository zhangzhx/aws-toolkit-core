package com.amazonaws.toolkit.core.hackathon;

import com.amazonaws.toolkit.core.hackathon.models.ActionInput;

public interface IActionExecutionContextProvider<I extends ActionInput> {

    ToolkitLogger createLogger();

    ToolkitProgresser createProgresser();

    I createActionInput();
}
