package com.fudo.notification.features.handler.query;

import com.fudo.notification.application.dto.query.NotificationQueries.GetUnreadCountQuery;
import com.fudo.notification.interfaces.NotificationRepository;
import com.fudo.shared.cqrs.RequestHandler;
import org.springframework.stereotype.Component;

@Component
public class GetUnreadCountHandler implements RequestHandler<GetUnreadCountQuery, Integer> {

    private final NotificationRepository notifications;

    public GetUnreadCountHandler(NotificationRepository notifications) {
        this.notifications = notifications;
    }

    @Override
    public Integer handle(GetUnreadCountQuery query) {
        return notifications.getUnreadCount(query.getUserId());
    }
}
