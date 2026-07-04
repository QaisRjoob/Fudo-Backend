package com.fudo.notification.features.handler.command;

import com.fudo.notification.application.dto.command.NotificationCommands.CreateNotificationCommand;
import com.fudo.notification.entity.Notification;
import com.fudo.notification.interfaces.NotificationRepository;
import com.fudo.shared.cqrs.RequestHandler;
import org.springframework.stereotype.Component;

@Component
public class CreateNotificationHandler implements RequestHandler<CreateNotificationCommand, Void> {

    private final NotificationRepository notifications;

    public CreateNotificationHandler(NotificationRepository notifications) {
        this.notifications = notifications;
    }

    @Override
    public Void handle(CreateNotificationCommand cmd) {
        if (cmd.getRecipientId() != null && cmd.getRecipientId().equals(cmd.getActorId())) {
            return null;
        }

        Notification notification = new Notification();
        notification.setRecipientId(cmd.getRecipientId());
        notification.setActorId(cmd.getActorId());
        notification.setType(cmd.getType());
        notification.setResourceId(cmd.getResourceId());
        notification.setResourceType(cmd.getResourceType());
        notification.setMessage(cmd.getMessage());

        notifications.add(notification);
        return null;
    }
}
