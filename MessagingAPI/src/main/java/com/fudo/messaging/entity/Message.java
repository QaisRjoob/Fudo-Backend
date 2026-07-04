package com.fudo.messaging.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "messages", indexes = {
        @Index(name = "ix_messages_conversation_id", columnList = "conversation_id"),
        @Index(name = "ix_messages_created_at", columnList = "created_at")
})
public class Message {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID id = UUID.randomUUID();

    @Column(name = "conversation_id", nullable = false)
    private UUID conversationId;

    @Column(name = "sender_id", nullable = false)
    private UUID senderId;

    @Column(nullable = false, length = 2000)
    private String content;

    @Column(name = "message_type", nullable = false, length = 16)
    private String messageType = "text";

    @Column(name = "media_url")
    private String mediaUrl;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conversation_id", insertable = false, updatable = false,
            foreignKey = @ForeignKey(name = "fk_message_conversation"))
    private Conversation conversation;
}
