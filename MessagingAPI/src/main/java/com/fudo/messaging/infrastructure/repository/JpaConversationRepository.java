package com.fudo.messaging.infrastructure.repository;

import com.fudo.messaging.entity.Conversation;
import com.fudo.messaging.entity.ConversationParticipant;
import com.fudo.messaging.interfaces.ConversationRepository;
import com.fudo.messaging.repository.ConversationJpaRepository;
import com.fudo.messaging.repository.ConversationParticipantJpaRepository;
import com.fudo.shared.pagination.PagedResult;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class JpaConversationRepository implements ConversationRepository {

    private final ConversationJpaRepository conversationJpaRepository;
    private final ConversationParticipantJpaRepository participantJpaRepository;

    public JpaConversationRepository(ConversationJpaRepository conversationJpaRepository,
                                      ConversationParticipantJpaRepository participantJpaRepository) {
        this.conversationJpaRepository = conversationJpaRepository;
        this.participantJpaRepository = participantJpaRepository;
    }

    @Override
    public Optional<Conversation> getById(UUID id) {
        return conversationJpaRepository.findById(id);
    }

    @Override
    public Optional<Conversation> getDirectConversation(UUID user1, UUID user2) {
        return conversationJpaRepository.findDirectConversation(user1, user2);
    }

    @Override
    public PagedResult<Conversation> getUserConversations(UUID userId, int page, int pageSize) {
        long total = conversationJpaRepository.countForUser(userId);
        List<UUID> pageIds = conversationJpaRepository.findIdsForUser(userId, PageRequest.of(page - 1, pageSize));
        List<Conversation> items = conversationJpaRepository.findByIdInOrderByLastMessageAtDesc(pageIds);
        return new PagedResult<>(items, (int) total, page, pageSize);
    }

    @Override
    @Transactional
    public Conversation create(List<UUID> participantIds, boolean isGroup, String groupName) {
        Conversation conversation = new Conversation();
        conversation.setGroup(isGroup);
        conversation.setGroupName(groupName);
        conversation.setParticipants(participantIds.stream().map(id -> {
            ConversationParticipant participant = new ConversationParticipant();
            participant.setUserId(id);
            participant.setConversation(conversation);
            participant.setConversationId(conversation.getId());
            return participant;
        }).collect(Collectors.toList()));
        return conversationJpaRepository.save(conversation);
    }

    @Override
    @Transactional
    public void updateLastMessageAt(UUID conversationId) {
        conversationJpaRepository.updateLastMessageAt(conversationId, Instant.now());
    }

    @Override
    @Transactional
    public void markRead(UUID conversationId, UUID userId) {
        participantJpaRepository.markRead(conversationId, userId, Instant.now());
    }

    @Override
    public boolean isParticipant(UUID conversationId, UUID userId) {
        return participantJpaRepository.existsByConversationIdAndUserId(conversationId, userId);
    }
}
