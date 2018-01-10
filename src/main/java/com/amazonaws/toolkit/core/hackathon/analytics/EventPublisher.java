package com.amazonaws.toolkit.core.hackathon.analytics;

import java.util.Collection;
import java.util.Collections;

/**
 * Publishes events to a backing analytics platform
 */
public abstract class EventPublisher {
    /**
     * Adds a metric to all future published events. Takes a lower priority than
     * {@link ToolkitEvent#addMetric(String, double)}
     *
     * @param metricName Name of the metric
     * @param value Value of the metric
     * @return this
     */
    public abstract EventPublisher addGlobalMetric(String metricName, double value);

    /**
     * Adds a metric to all future published events. Takes a lower priority than
     * {@link ToolkitEvent#addAttribute(String, String)} (String, double)}
     *
     * @param metricName Name of the attribute
     * @param value Value of the attribute
     * @return this
     */
    public abstract EventPublisher addGlobalAttribute(String metricName, String value);

    /**
     * Creates a new event with the specified name
     *
     * @param eventName The name to publish the event as
     * @return The new event
     */
    public abstract ToolkitEvent createEvent(String eventName);

    /**
     * Records the event to the backing analytics platform
     *
     * @param event The event to publish
     * @return true if successfully published, else false
     */
    public boolean recordEvent(ToolkitEvent event) {
        return recordEvents(Collections.singleton(event));
    }

    /**
     * Records the collection of events to the backing analytics platform
     *
     * @param events The events to publish
     * @return true if successfully published, else false
     */
    public abstract boolean recordEvents(Collection<ToolkitEvent> events);

    /**
     * Shutdown the publisher. May flush any pending events before exiting.
     */
    public void shutdown() {}
}