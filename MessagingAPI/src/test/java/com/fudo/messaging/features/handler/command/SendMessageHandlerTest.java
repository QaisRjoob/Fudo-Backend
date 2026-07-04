package com.fudo.messaging.features.handler.command;

import com.fudo.messaging.application.dto.command.MessagingCommands.MessageDto;
import com.fudo.messaging.application.dto.command.MessagingCommands.SendMessageCommand;
import com.fudo.messaging.entity.Message;
import com.fudo.messaging.interfaces.ConversationRepository;
import com.fudo.messaging.interfaces.MessageRepository;
import com.fudo.shared.exception.ForbiddenException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SendMessageHandlerTest {

    @Mock
    private MessageRepository messages;

    @Mock
    private ConversationRepository conversations;

    @InjectMocks
    private SendMessageHandler handler;

    @Test
    void rejectsSenderNotInConversation() {
        SendMessageCommand cmd = new SendMessageCommand();
        cmd.setConversationId(UUID.randomUUID());
        cmd.setSenderId(UUID.randomUUID());
        cmd.setContent("hi");

        when(conversations.isParticipant(cmd.getConversationId(), cmd.getSenderId())).thenReturn(false);

        assertThatThrownBy(() -> handler.handle(cmd)).isInstanceOf(ForbiddenException.class);
        verify(messages, never()).send(any());
    }

    @Test
    void sendsMessageAndUpdatesConversation() {
        SendMessageCommand cmd = new SendMessageCommand();
        cmd.setConversationId(UUID.randomUUID());
        cmd.setSenderId(UUID.randomUUID());
        cmd.setContent("hello");
        cmd.setMessageType("text");

        when(conversations.isParticipant(cmd.getConversationId(), cmd.getSenderId())).thenReturn(true);
        when(messages.send(any(Message.class))).thenAnswer(invocation -> invocation.getArgument(0));

        MessageDto result = handler.handle(cmd);

        assertThat(result.getContent()).isEqualTo("hello");
        assertThat(result.getConversationId()).isEqualTo(cmd.getConversationId());
        verify(conversations).updateLastMessageAt(cmd.getConversationId());
    }
}
