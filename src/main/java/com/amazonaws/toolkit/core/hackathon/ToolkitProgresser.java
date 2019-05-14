package com.amazonaws.toolkit.core.hackathon;

// Another sommed
// Hoho
public interface ToolkitProgresser {

    /**
     * Begin a task with the specified task name.
     *
     * @param name
     */
    void beginTask(String name);

    /**
     * Set task execution fraction
     *
     * @param fraction - A double number between 0 and 1.
     */
    void workedFraction(double fraction);

    /**
     * Notify the task is done.
     */
    void done();

    boolean isCanceled();

    void cancel();
}
