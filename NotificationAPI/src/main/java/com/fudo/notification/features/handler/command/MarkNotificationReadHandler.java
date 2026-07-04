package com.fudo.notification.features.handler.command;

import com.fudo.notification.application.dto.command.NotificationCommands.MarkNotificationReadCommand;
import com.fudo.notification.interfaces.NotificationRepository;
import com.fudo.shared.cqrs.RequestHandler;
import org.springframework.stereotype.Component;

@Component
public class MarkNotificationReadHandler implements RequestHandler<MarkNotificationReadCommand, Void> {

    private final NotificationRepository notifications;

    public MarkNotificationReadHandler(NotificationRepository notifications) {
        this.notifications = notifications;
    }

    @Override
    public Void handle(MarkNotificationReadCommand cmd) {
        notifications.markRead(cmd.getNotificationId(), cmd.getUserId());
        return null;
    }
}
