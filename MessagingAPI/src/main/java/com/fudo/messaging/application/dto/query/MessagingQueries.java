package com.fudo.messaging.application.dto.query;

import com.fudo.messaging.application.dto.command.MessagingCommands.ConversationDto;
import com.fudo.messaging.application.dto.command.MessagingCommands.MessageDto;
import com.fudo.shared.cqrs.Request;
import com.fudo.shared.pagination.CursorPage;
import com.fudo.shared.pagination.PagedResult;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

/** Mirrors MessagingQueries.cs. */
public final class MessagingQueries {

    private MessagingQueries() {
    }

    @Getter
    @Setter
    public static class GetConversationsQuery implements Request<PagedResult<ConversationDto>> {
        private UUID userId;
        private int page = 1;
        private int pageSize = 20;
    }

    @Getter
    @Setter
    public static class GetMessagesQuery implements Request<CursorPage<MessageDto>> {
        private UUID conversationId;
        private UUID requestingUserId;
        private int limit = 30;
        private String cursor;
    }
}
