package com.fudo.messaging.features.handler.query;

import com.fudo.messaging.application.dto.command.MessagingCommands.MessageDto;
import com.fudo.messaging.application.dto.query.MessagingQueries.GetMessagesQuery;
import com.fudo.messaging.entity.Message;
import com.fudo.messaging.interfaces.ConversationRepository;
import com.fudo.messaging.interfaces.MessageRepository;
import com.fudo.shared.cqrs.RequestHandler;
import com.fudo.shared.exception.ForbiddenException;
import com.fudo.shared.pagination.CursorPage;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class GetMessagesHandler implements RequestHandler<GetMessagesQuery, CursorPage<MessageDto>> {

    private final MessageRepository messages;
    private final ConversationRepository conversations;

    public GetMessagesHandler(MessageRepository messages, ConversationRepository conversations) {
        this.messages = messages;
        this.conversations = conversations;
    }

    @Override
    public CursorPage<MessageDto> handle(GetMessagesQuery query) {
        if (!conversations.isParticipant(query.getConversationId(), query.getRequestingUserId())) {
            throw new ForbiddenException("You are not a participant in this conversation.");
        }

        CursorPage<Message> result = messages.getMessages(query.getConversationId(), query.getLimit(), query.getCursor());
        List<MessageDto> items = result.getItems().stream()
                .map(m -> new MessageDto(m.getId(), m.getConversationId(), m.getSenderId(), m.getContent(),
                        m.getMessageType(), m.getMediaUrl(), m.getCreatedAt()))
                .collect(Collectors.toList());
        return new CursorPage<>(items, result.getNextCursor());
    }
}
