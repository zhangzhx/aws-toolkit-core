package com.amazonaws.toolkit.core.hackathon;

import java.io.Closeable;

//TODO replace ILog with the real one.
public interface ToolkitLogger extends Closeable {

    void info(String format, Object... params);

    void warning(String format, Object... params);

    void error(String format, Object... params);

}
