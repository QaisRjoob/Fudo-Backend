package com.fudo.notification.features.handler.command;

import com.fudo.notification.application.dto.command.NotificationCommands.CreateNotificationCommand;
import com.fudo.notification.entity.Notification;
import com.fudo.notification.interfaces.NotificationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CreateNotificationHandlerTest {

    @Mock
    private NotificationRepository notifications;

    @Test
    void handle_addsNotification_whenRecipientDiffersFromActor() {
        CreateNotificationHandler handler = new CreateNotificationHandler(notifications);

        CreateNotificationCommand cmd = new CreateNotificationCommand();
        cmd.setRecipientId(UUID.randomUUID());
        cmd.setActorId(UUID.randomUUID());
        cmd.setType("comment");
        cmd.setResourceId("post-1");
        cmd.setResourceType("post");
        cmd.setMessage("Someone commented on your post");

        handler.handle(cmd);

        ArgumentCaptor<Notification> captor = ArgumentCaptor.forClass(Notification.class);
        verify(notifications).add(captor.capture());

        Notification saved = captor.getValue();
        assertThat(saved.getRecipientId()).isEqualTo(cmd.getRecipientId());
        assertThat(saved.getActorId()).isEqualTo(cmd.getActorId());
        assertThat(saved.getType()).isEqualTo("comment");
        assertThat(saved.getResourceId()).isEqualTo("post-1");
        assertThat(saved.getResourceType()).isEqualTo("post");
        assertThat(saved.getMessage()).isEqualTo("Someone commented on your post");
    }

    @Test
    void handle_doesNothing_whenRecipientIsActor() {
        CreateNotificationHandler handler = new CreateNotificationHandler(notifications);

        UUID selfId = UUID.randomUUID();
        CreateNotificationCommand cmd = new CreateNotificationCommand();
        cmd.setRecipientId(selfId);
        cmd.setActorId(selfId);
        cmd.setType("like");

        handler.handle(cmd);

        verify(notifications, never()).add(org.mockito.ArgumentMatchers.any());
    }
}
