package com.fudo.messaging.interfaces;

import com.fudo.messaging.entity.Message;
import com.fudo.shared.pagination.CursorPage;

import java.util.UUID;

public interface MessageRepository {

    CursorPage<Message> getMessages(UUID conversationId, int limit, String cursor);

    Message send(Message message);

    void delete(UUID messageId, UUID requestingUserId);
}
