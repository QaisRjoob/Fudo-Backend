package com.fudo.shared.messaging;

import com.fudo.shared.events.IntegrationEvent;

/**
 * Publishes domain integration events so other services can react asynchronously
 * (e.g. NotificationAPI creating a notification when a PostLikedEvent fires).
 */
public interface EventPublisher {

    void publish(IntegrationEvent event);
}
