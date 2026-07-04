package com.fudo.messaging.repository;

import com.fudo.messaging.entity.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface MessageJpaRepository extends JpaRepository<Message, UUID> {

    @Query("""
            select m from Message m where m.conversationId = :conversationId and m.isDeleted = false
            order by m.createdAt desc, m.id desc
            """)
    List<Message> findPageNoCursor(@Param("conversationId") UUID conversationId, Pageable pageable);

    @Query("""
            select m from Message m where m.conversationId = :conversationId and m.isDeleted = false
            and (m.createdAt < :cursorDate or (m.createdAt = :cursorDate and m.id < :cursorId))
            order by m.createdAt desc, m.id desc
            """)
    List<Message> findPageBeforeCursor(@Param("conversationId") UUID conversationId,
                                        @Param("cursorDate") Instant cursorDate,
                                        @Param("cursorId") UUID cursorId,
                                        Pageable pageable);

    @Query("select m from Message m where m.conversationId in :conversationIds and m.isDeleted = false order by m.createdAt desc")
    List<Message> findLastMessagesForConversations(@Param("conversationIds") List<UUID> conversationIds, Pageable pageable);

    @Modifying
    @Query("update Message m set m.isDeleted = true where m.id = :id and m.senderId = :requestingUserId")
    void softDelete(@Param("id") UUID id, @Param("requestingUserId") UUID requestingUserId);
}
