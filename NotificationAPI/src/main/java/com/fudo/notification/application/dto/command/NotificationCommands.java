package com.fudo.notification.application.dto.command;

import com.fudo.shared.cqrs.Request;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

/**
 * Ports NotificationCommands.cs exactly, preserving the original grouping of
 * all command DTOs in a single file.
 */
public class NotificationCommands {

    private NotificationCommands() {
    }

    @Getter
    @Setter
    public static class CreateNotificationCommand implements Request<Void> {

        private UUID recipientId;

        private UUID actorId;

        @NotBlank
        @Size(max = 64)
        private String type;

        private String resourceId;

        @Size(max = 32)
        private String resourceType;

        @Size(max = 256)
        private String message;
    }

    @Getter
    @Setter
    public static class MarkNotificationReadCommand implements Request<Void> {

        private UUID notificationId;

        private UUID userId;
    }

    @Getter
    @Setter
    public static class MarkAllNotificationsReadCommand implements Request<Void> {

        private UUID userId;
    }
}
