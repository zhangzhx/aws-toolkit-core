package com.amazonaws.toolkit.core.hackathon.example;

import java.io.IOException;

import com.amazonaws.toolkit.core.hackathon.ToolkitLogger;

public class ToolkitLoggerImpl implements ToolkitLogger {
    private final String name;

    public ToolkitLoggerImpl(String name) {
        this.name = name;
    }

    @Override
    public void close() throws IOException {
    }

    @Override
    public void info(String format, Object... params) {
        System.out.print(String.format("[INFO - %s] %s", name, String.format(format, params)));
    }

    @Override
    public void warning(String format, Object... params) {
        System.out.print(String.format("[WARNING - %s] %s", name, String.format(format, params)));
    }

    @Override
    public void error(String format, Object... params) {
        System.err.print(String.format("[ERROR - %s] %s", name, String.format(format, params)));
    }
}
