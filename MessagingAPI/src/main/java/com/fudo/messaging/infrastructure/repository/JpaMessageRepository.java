package com.fudo.messaging.infrastructure.repository;

import com.fudo.messaging.entity.Message;
import com.fudo.messaging.interfaces.MessageRepository;
import com.fudo.messaging.repository.MessageJpaRepository;
import com.fudo.shared.pagination.CursorHelper;
import com.fudo.shared.pagination.CursorPage;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Repository
public class JpaMessageRepository implements MessageRepository {

    private final MessageJpaRepository jpaRepository;

    public JpaMessageRepository(MessageJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public CursorPage<Message> getMessages(UUID conversationId, int limit, String cursor) {
        List<Message> messages;
        if (cursor != null) {
            CursorHelper.Cursor decoded = CursorHelper.decode(cursor);
            messages = jpaRepository.findPageBeforeCursor(conversationId, decoded.createdAt(), decoded.id(),
                    PageRequest.of(0, limit + 1));
        } else {
            messages = jpaRepository.findPageNoCursor(conversationId, PageRequest.of(0, limit + 1));
        }

        messages = new ArrayList<>(messages);
        String nextCursor = null;
        if (messages.size() > limit) {
            messages.remove(messages.size() - 1);
            Message last = messages.get(messages.size() - 1);
            nextCursor = CursorHelper.encode(last.getCreatedAt(), last.getId());
        }

        Collections.reverse(messages);
        return new CursorPage<>(messages, nextCursor);
    }

    @Override
    @Transactional
    public Message send(Message message) {
        return jpaRepository.save(message);
    }

    @Override
    @Transactional
    public void delete(UUID messageId, UUID requestingUserId) {
        jpaRepository.softDelete(messageId, requestingUserId);
    }
}
