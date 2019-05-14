package com.amazonaws.toolkit.core.hackathon.example;

import com.amazonaws.toolkit.core.hackathon.actions.FooAction;
import com.amazonaws.toolkit.core.hackathon.models.FooInput;

// Naother
public class FooActionRun extends AbstractActionRun<FooInput> {

 protected FooActionRun() {
        super(new FooAction());
    }

       @Override
    protected FooInput createActionInput() {
        return new FooInput("some project");
    }
}
