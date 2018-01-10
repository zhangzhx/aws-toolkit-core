package com.amazonaws.toolkit.core.hackathon.analytics.ui;

import com.amazonaws.toolkit.core.hackathon.analytics.EventPublisher;
import com.amazonaws.toolkit.core.hackathon.analytics.NoOpEventPublisher;

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
