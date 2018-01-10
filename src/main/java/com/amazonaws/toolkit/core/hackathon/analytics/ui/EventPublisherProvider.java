package com.amazonaws.toolkit.core.hackathon.analytics.ui;

import com.amazonaws.toolkit.core.hackathon.analytics.EventPublisher;
import com.amazonaws.toolkit.core.hackathon.analytics.NoOpEventPublisher;

/**
 * A default event publisher manager which is independent from the upper level toolkit.
 */
public class EventPublisherProvider {

    private EventPublisher eventPublisher;
    public static EventPublisherProvider INSTANCE = new EventPublisherProvider();

    private EventPublisherProvider() {
        this.eventPublisher = new NoOpEventPublisher();
    }

    public EventPublisher getEventPublisher() {
        return eventPublisher;
    }
}
