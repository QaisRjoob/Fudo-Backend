package com.fudo.messaging.repository;

import com.fudo.messaging.entity.Conversation;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ConversationJpaRepository extends JpaRepository<Conversation, UUID> {

    @EntityGraph(attributePaths = "participants")
    Optional<Conversation> findById(UUID id);

    @Query("""
            select c from Conversation c join c.participants p
            where c.isGroup = false and p.userId = :userId
            and exists (select 1 from ConversationParticipant p2 where p2.conversationId = c.id and p2.userId = :otherUserId)
            """)
    @EntityGraph(attributePaths = "participants")
    Optional<Conversation> findDirectConversation(@Param("userId") UUID userId, @Param("otherUserId") UUID otherUserId);

    @Query("select c.id from Conversation c join c.participants p where p.userId = :userId order by c.lastMessageAt desc")
    List<UUID> findIdsForUser(@Param("userId") UUID userId, org.springframework.data.domain.Pageable pageable);

    @Query("select count(c) from Conversation c join c.participants p where p.userId = :userId")
    long countForUser(@Param("userId") UUID userId);

    @EntityGraph(attributePaths = "participants")
    @Query("select c from Conversation c where c.id in :ids order by c.lastMessageAt desc")
    List<Conversation> findByIdInOrderByLastMessageAtDesc(@Param("ids") List<UUID> ids);

    @Modifying
    @Query("update Conversation c set c.lastMessageAt = :now where c.id = :id")
    void updateLastMessageAt(@Param("id") UUID id, @Param("now") Instant now);
}
