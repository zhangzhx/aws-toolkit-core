package com.amazonaws.toolkit.core.hackathon;

import java.io.Closeable;

// Comment for Toolkit Logger
public interface ToolkitLogger extends Closeable {

    void info(String format, Object... params);

    void warning(String format, Object... params);

    void error(String format, Object... params);

    default void infoLine(String format, Object... params) {
        info(format + "\n", params);
    }

    default void warningLine(String format, Object... params) {
        warning(format + "\n", params);
    }

    default void errorLine(String format, Object... params) {
        error(format + "\n", params);
    }
}
