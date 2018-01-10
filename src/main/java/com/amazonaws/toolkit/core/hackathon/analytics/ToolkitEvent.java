package com.amazonaws.toolkit.core.hackathon.analytics;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.amazonaws.toolkit.core.hackathon.ToolkitLogger;

public abstract class ToolkitEvent<T> {
    protected final String eventName;
    protected final Date eventTime;
    protected final Map<String, Double> metrics;
    protected final Map<String, String> attributes;

    private EventPublisher publisher;

    protected ToolkitEvent(String eventName, EventPublisher publisher) {
        this.eventName = eventName;
        this.publisher = publisher;
        this.metrics = new ConcurrentHashMap<>();
        this.attributes = new ConcurrentHashMap<>();
        this.eventTime = new Date();
    }

    /**
     * Allows overriding of the event publisher when used with delegation such as {@link BatchingEventPublisher}
     *
     * @param publisher The publisher to override
     */
    final void setPublisher(EventPublisher publisher) {
        this.publisher = publisher;
    }

    /**
     * Finalizes the event and sends it to the {@link EventPublisher} for recording if valid
     */
    public final void record() {
        if(isValid()) {
            publisher.recordEvent(this);
        }
    }

    /**
     * @return True if the event complies with the backing analytics platform API
     */
    protected boolean isValid() {
        return true;
    }

    /**
     * @return The event in the corresponding analytics platform's view of it
     */
    protected abstract T convertEvent();

    /**
     * Adds a metric to the event
     *
     * @param metricName Name of the metric
     * @param value Value of the metric
     * @return this
     */
    public ToolkitEvent addMetric(String metricName, double value) {
        this.metrics.put(metricName, value);
        return this;
    }

    /**
     * Adds a boolean metric to the event
     *
     * @param metricName Name of the attribute
     * @param value If the event metric was successful
     * @return this
     */
    public ToolkitEvent addBooleanMetric(String metricName, boolean value) {
        this.metrics.put(metricName, value ? 1d : 0);
        return this;
    }

    /**
     * Adds an attribute to the event
     *
     * @param metricName Name of the attribute
     * @param value Value of the attribute
     * @return this
     */
    public ToolkitEvent addAttribute(String metricName, String value) {
        this.attributes.put(metricName, value);
        return this;
    }

    public static void print(ToolkitLogger logger, ToolkitEvent event) {
        logger.error("[%tc - %s]:\n", event.eventTime, event.eventName);
        event.attributes.forEach((k, v) -> logger.error("Attribute - [%s: %s]\n", k, v));
        event.metrics.forEach((k, v) -> logger.error("Metric - [%s: %.2f]\n", k, v));
    }
}