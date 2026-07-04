package com.fudo.messaging.features.handler.command;

import com.fudo.messaging.application.dto.command.MessagingCommands.ConversationDto;
import com.fudo.messaging.application.dto.command.MessagingCommands.CreateConversationCommand;
import com.fudo.messaging.entity.Conversation;
import com.fudo.messaging.interfaces.ConversationRepository;
import com.fudo.shared.cqrs.RequestHandler;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CreateConversationHandler implements RequestHandler<CreateConversationCommand, ConversationDto> {

    private final ConversationRepository conversations;

    public CreateConversationHandler(ConversationRepository conversations) {
        this.conversations = conversations;
    }

    @Override
    public ConversationDto handle(CreateConversationCommand cmd) {
        return conversations.getDirectConversation(cmd.getRequestingUserId(), cmd.getOtherUserId())
                .map(this::toDto)
                .orElseGet(() -> toDto(conversations.create(
                        List.of(cmd.getRequestingUserId(), cmd.getOtherUserId()), false, null)));
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
