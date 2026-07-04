package com.fudo.notification.features.handler.command;

import com.fudo.notification.application.dto.command.NotificationCommands.MarkAllNotificationsReadCommand;
import com.fudo.notification.application.dto.command.NotificationCommands.MarkNotificationReadCommand;
import com.fudo.notification.interfaces.NotificationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MarkNotificationReadHandlerTest {

    @Mock
    private NotificationRepository notifications;

    @Test
    void handle_marksSingleNotificationRead() {
        MarkNotificationReadHandler handler = new MarkNotificationReadHandler(notifications);

        UUID notificationId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        MarkNotificationReadCommand cmd = new MarkNotificationReadCommand();
        cmd.setNotificationId(notificationId);
        cmd.setUserId(userId);

        handler.handle(cmd);

        verify(notifications).markRead(notificationId, userId);
    }

    @Test
    void handle_marksAllNotificationsRead() {
        MarkAllNotificationsReadHandler handler = new MarkAllNotificationsReadHandler(notifications);

        UUID userId = UUID.randomUUID();
        MarkAllNotificationsReadCommand cmd = new MarkAllNotificationsReadCommand();
        cmd.setUserId(userId);

        handler.handle(cmd);

        verify(notifications).markAllRead(userId);
    }
}
