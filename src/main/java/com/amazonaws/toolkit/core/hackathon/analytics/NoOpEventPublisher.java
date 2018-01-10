package com.amazonaws.toolkit.core.hackathon.analytics;

import java.util.Collection;

/**
 * A NO-OP event publisher
 */
public class NoOpEventPublisher extends EventPublisher {
    @Override
    public EventPublisher addGlobalMetric(String metricName, double value) {
        return this;
    }

    @Override
    public EventPublisher addGlobalAttribute(String metricName, String value) {
        return this;
    }

    @Override
    public ToolkitEvent createEvent(String eventName) {
        return new ToolkitEvent(eventName, this) {
            @Override
            protected boolean isValid() {
                return false;
            }

            @Override
            protected Object convertEvent() {
                return null;
            }
        };
    }

    @Override
    public boolean recordEvent(ToolkitEvent event) {
        return true;
    }

    @Override
    public boolean recordEvents(Collection<ToolkitEvent> events) {
        return true;
    }
}