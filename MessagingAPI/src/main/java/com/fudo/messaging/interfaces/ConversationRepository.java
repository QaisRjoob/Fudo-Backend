package com.fudo.messaging.interfaces;

import com.fudo.messaging.entity.Conversation;
import com.fudo.shared.pagination.PagedResult;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ConversationRepository {

    Optional<Conversation> getById(UUID id);

    Optional<Conversation> getDirectConversation(UUID user1, UUID user2);

    PagedResult<Conversation> getUserConversations(UUID userId, int page, int pageSize);

    Conversation create(List<UUID> participantIds, boolean isGroup, String groupName);

    void updateLastMessageAt(UUID conversationId);

    void markRead(UUID conversationId, UUID userId);

    boolean isParticipant(UUID conversationId, UUID userId);
}
