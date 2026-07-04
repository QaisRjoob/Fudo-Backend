package com.fudo.notification.features.handler.command;

import com.fudo.notification.application.dto.command.NotificationCommands.MarkAllNotificationsReadCommand;
import com.fudo.notification.interfaces.NotificationRepository;
import com.fudo.shared.cqrs.RequestHandler;
import org.springframework.stereotype.Component;

@Component
public class MarkAllNotificationsReadHandler implements RequestHandler<MarkAllNotificationsReadCommand, Void> {

    private final NotificationRepository notifications;

    public MarkAllNotificationsReadHandler(NotificationRepository notifications) {
        this.notifications = notifications;
    }

    @Override
    public Void handle(MarkAllNotificationsReadCommand cmd) {
        notifications.markAllRead(cmd.getUserId());
        return null;
    }
}
