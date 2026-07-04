package com.fudo.shared.events;

import java.time.Instant;
import java.util.UUID;

public abstract class BaseIntegrationEvent implements IntegrationEvent {

    private final UUID eventId = UUID.randomUUID();
    private final Instant occurredAt = Instant.now();

    @Override
    public UUID getEventId() {
        return eventId;
    }

    @Override
    public Instant getOccurredAt() {
        return occurredAt;
    }
}
