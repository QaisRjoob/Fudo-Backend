package com.fudo.notification.features.handler.query;

import com.fudo.notification.application.dto.query.NotificationQueries.GetNotificationsQuery;
import com.fudo.notification.application.dto.query.NotificationQueries.NotificationDto;
import com.fudo.notification.entity.Notification;
import com.fudo.notification.interfaces.NotificationRepository;
import com.fudo.shared.cqrs.RequestHandler;
import com.fudo.shared.pagination.PagedResult;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GetNotificationsHandler implements RequestHandler<GetNotificationsQuery, PagedResult<NotificationDto>> {

    private final NotificationRepository notifications;

    public GetNotificationsHandler(NotificationRepository notifications) {
        this.notifications = notifications;
    }

    @Override
    public PagedResult<NotificationDto> handle(GetNotificationsQuery query) {
        PagedResult<Notification> result = notifications.getByUser(query.getUserId(), query.getPage(), query.getPageSize());

        List<NotificationDto> items = result.getItems().stream()
                .map(n -> new NotificationDto(
                        n.getId(),
                        n.getRecipientId(),
                        n.getActorId(),
                        n.getType(),
                        n.getResourceId(),
                        n.getResourceType(),
                        n.getMessage(),
                        n.isRead(),
                        n.getCreatedAt()))
                .toList();

        return new PagedResult<>(items, result.getTotalCount(), result.getPage(), result.getPageSize());
    }
}
