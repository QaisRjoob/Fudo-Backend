package com.fudo.notification.interfaces;

import com.fudo.notification.entity.Notification;
import com.fudo.shared.pagination.PagedResult;

import java.util.UUID;

/**
 * Mirrors {@code INotificationRepository.cs}. Synchronous signatures since
 * Spring MVC handlers run on dedicated request threads (no async/await
 * equivalent needed here).
 */
public interface NotificationRepository {

    PagedResult<Notification> getByUser(UUID userId, int page, int pageSize);

    int getUnreadCount(UUID userId);

    void add(Notification notification);

    void markRead(UUID notificationId, UUID userId);

    void markAllRead(UUID userId);
}
