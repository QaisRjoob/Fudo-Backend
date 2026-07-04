package com.fudo.notification.application.dto.query;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fudo.shared.cqrs.Request;
import com.fudo.shared.pagination.PagedResult;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

/**
 * Ports NotificationQueries.cs exactly, preserving the original grouping of
 * GetNotificationsQuery + GetUnreadCountQuery + NotificationDto in one file.
 */
public class NotificationQueries {

    private NotificationQueries() {
    }

    @Getter
    @Setter
    public static class GetNotificationsQuery implements Request<PagedResult<NotificationDto>> {

        private UUID userId;
        private int page = 1;
        private int pageSize = 20;
    }

    @Getter
    @Setter
    public static class GetUnreadCountQuery implements Request<Integer> {

        private UUID userId;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NotificationDto {
        private UUID id;
        private UUID recipientId;
        private UUID actorId;
        private String type;
        private String resourceId;
        private String resourceType;
        private String message;
        @JsonProperty("isRead")
        private boolean isRead;
        private Instant createdAt;
    }
}
