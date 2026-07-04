package com.fudo.messaging.repository;

import com.fudo.messaging.entity.ConversationParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.UUID;

public interface ConversationParticipantJpaRepository extends JpaRepository<ConversationParticipant, UUID> {

    boolean existsByConversationIdAndUserId(UUID conversationId, UUID userId);

    @Modifying
    @Query("update ConversationParticipant p set p.lastReadAt = :now where p.conversationId = :conversationId and p.userId = :userId")
    void markRead(@Param("conversationId") UUID conversationId, @Param("userId") UUID userId, @Param("now") Instant now);
}
