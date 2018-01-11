package com.amazonaws.toolkit.core.hackathon.example;

import java.text.DecimalFormat;

import com.amazonaws.toolkit.core.hackathon.ToolkitLogger;
import com.amazonaws.toolkit.core.hackathon.ToolkitProgresser;

public class ToolkitProgresserImpl implements ToolkitProgresser {
    private final ToolkitLogger logger;
    private String currentTask;

    public ToolkitProgresserImpl(ToolkitLogger logger) {
        this.logger = logger;
    }

    @Override
    public void beginTask(String name) {
        this.currentTask = name;
        logger.info("Starting task: %s\n", name);
    }

    @Override
    public void workedFraction(double fraction) {
        if (currentTask != null) {
            DecimalFormat decimalFormat = new DecimalFormat("###.##%");
            logger.info("Task %s has %s completed\n", currentTask, decimalFormat.format(fraction));
//            logger.info("Task %s has %.2f\\\\% completed\n", currentTask, fraction*100);
        }
    }

    @Override
    public void done() {
        if (currentTask != null) {
            logger.info("Task %s has completed\n", currentTask);
        }
    }

    @Override
    public boolean isCanceled() {
        return false;
    }

    @Override
    public void cancel() {
    }
}
