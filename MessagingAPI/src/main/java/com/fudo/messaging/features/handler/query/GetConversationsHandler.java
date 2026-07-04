package com.fudo.messaging.features.handler.query;

import com.fudo.messaging.application.dto.command.MessagingCommands.ConversationDto;
import com.fudo.messaging.application.dto.query.MessagingQueries.GetConversationsQuery;
import com.fudo.messaging.entity.Conversation;
import com.fudo.messaging.interfaces.ConversationRepository;
import com.fudo.shared.cqrs.RequestHandler;
import com.fudo.shared.pagination.PagedResult;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class GetConversationsHandler implements RequestHandler<GetConversationsQuery, PagedResult<ConversationDto>> {

    private final ConversationRepository conversations;

    public GetConversationsHandler(ConversationRepository conversations) {
        this.conversations = conversations;
    }

    @Override
    public PagedResult<ConversationDto> handle(GetConversationsQuery query) {
        PagedResult<Conversation> result = conversations.getUserConversations(query.getUserId(), query.getPage(), query.getPageSize());
        List<ConversationDto> items = result.getItems().stream().map(this::toDto).collect(Collectors.toList());
        return new PagedResult<>(items, result.getTotalCount(), result.getPage(), result.getPageSize());
    }

    private ConversationDto toDto(Conversation c) {
        return new ConversationDto(
                c.getId(),
                c.isGroup(),
                c.getGroupName(),
                c.getParticipants().stream().map(p -> p.getUserId()).collect(Collectors.toList()),
                c.getLastMessageAt());
    }
}
