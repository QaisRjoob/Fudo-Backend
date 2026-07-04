package com.fudo.messaging.features.handler.command;

import com.fudo.messaging.application.dto.command.MessagingCommands.MessageDto;
import com.fudo.messaging.application.dto.command.MessagingCommands.SendMessageCommand;
import com.fudo.messaging.entity.Message;
import com.fudo.messaging.interfaces.ConversationRepository;
import com.fudo.messaging.interfaces.MessageRepository;
import com.fudo.shared.cqrs.RequestHandler;
import com.fudo.shared.exception.ForbiddenException;
import org.springframework.stereotype.Component;

@Component
public class SendMessageHandler implements RequestHandler<SendMessageCommand, MessageDto> {

    private final MessageRepository messages;
    private final ConversationRepository conversations;

    public SendMessageHandler(MessageRepository messages, ConversationRepository conversations) {
        this.messages = messages;
        this.conversations = conversations;
    }

    @Override
    public MessageDto handle(SendMessageCommand cmd) {
        if (!conversations.isParticipant(cmd.getConversationId(), cmd.getSenderId())) {
            throw new ForbiddenException("You are not a participant in this conversation.");
        }

        Message message = new Message();
        message.setConversationId(cmd.getConversationId());
        message.setSenderId(cmd.getSenderId());
        message.setContent(cmd.getContent());
        message.setMessageType(cmd.getMessageType());
        message.setMediaUrl(cmd.getMediaUrl());
        message = messages.send(message);

        conversations.updateLastMessageAt(cmd.getConversationId());

        return new MessageDto(message.getId(), message.getConversationId(), message.getSenderId(),
                message.getContent(), message.getMessageType(), message.getMediaUrl(), message.getCreatedAt());
    }
}
