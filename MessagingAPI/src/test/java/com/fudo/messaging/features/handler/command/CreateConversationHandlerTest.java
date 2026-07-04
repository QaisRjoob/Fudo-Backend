package com.fudo.messaging.features.handler.command;

import com.fudo.messaging.application.dto.command.MessagingCommands.ConversationDto;
import com.fudo.messaging.application.dto.command.MessagingCommands.CreateConversationCommand;
import com.fudo.messaging.entity.Conversation;
import com.fudo.messaging.interfaces.ConversationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateConversationHandlerTest {

    @Mock
    private ConversationRepository conversations;

    @InjectMocks
    private CreateConversationHandler handler;

    @Test
    void returnsExistingDirectConversationWithoutCreatingANewOne() {
        UUID requestingUserId = UUID.randomUUID();
        UUID otherUserId = UUID.randomUUID();
        Conversation existing = new Conversation();

        when(conversations.getDirectConversation(requestingUserId, otherUserId)).thenReturn(Optional.of(existing));

        CreateConversationCommand cmd = new CreateConversationCommand();
        cmd.setRequestingUserId(requestingUserId);
        cmd.setOtherUserId(otherUserId);

        ConversationDto result = handler.handle(cmd);

        assertThat(result.getId()).isEqualTo(existing.getId());
        verify(conversations, never()).create(any(), anyBoolean(), any());
    }

    @Test
    void createsNewDirectConversationWhenNoneExists() {
        UUID requestingUserId = UUID.randomUUID();
        UUID otherUserId = UUID.randomUUID();
        Conversation created = new Conversation();

        when(conversations.getDirectConversation(requestingUserId, otherUserId)).thenReturn(Optional.empty());
        when(conversations.create(eq(List.of(requestingUserId, otherUserId)), eq(false), eq(null))).thenReturn(created);

        CreateConversationCommand cmd = new CreateConversationCommand();
        cmd.setRequestingUserId(requestingUserId);
        cmd.setOtherUserId(otherUserId);

        ConversationDto result = handler.handle(cmd);

        assertThat(result.getId()).isEqualTo(created.getId());
    }
}
