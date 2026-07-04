package com.fudo.messaging.features.handler.command;

import com.fudo.messaging.application.dto.command.MessagingCommands.MarkMessagesReadCommand;
import com.fudo.messaging.interfaces.ConversationRepository;
import com.fudo.shared.cqrs.RequestHandler;
import org.springframework.stereotype.Component;

@Component
public class MarkMessagesReadHandler implements RequestHandler<MarkMessagesReadCommand, Void> {

    private final ConversationRepository conversations;

    public MarkMessagesReadHandler(ConversationRepository conversations) {
        this.conversations = conversations;
    }

    @Override
    public Void handle(MarkMessagesReadCommand cmd) {
        conversations.markRead(cmd.getConversationId(), cmd.getUserId());
        return null;
    }
}
