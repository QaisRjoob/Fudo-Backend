package com.fudo.notification.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "notifications", indexes = {
        @Index(name = "ix_notifications_recipient_id", columnList = "recipient_id"),
        @Index(name = "ix_notifications_created_at", columnList = "created_at")
})
public class Notification {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id = UUID.randomUUID();

    @Column(name = "recipient_id", nullable = false)
    private UUID recipientId;

    @Column(name = "actor_id")
    private UUID actorId;

    @Column(name = "type", nullable = false, length = 64)
    private String type;

    @Column(name = "resource_id")
    private String resourceId;

    @Column(name = "resource_type", length = 32)
    private String resourceType;

    @Column(name = "message", length = 256)
    private String message;

    @Column(name = "is_read", nullable = false)
    private boolean isRead = false;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt = Instant.now();
}
