package com.fudo.notification.infrastructure.repository;

import com.fudo.notification.entity.Notification;
import com.fudo.notification.interfaces.NotificationRepository;
import com.fudo.notification.repository.NotificationJpaRepository;
import com.fudo.shared.pagination.PagedResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class JpaNotificationRepository implements NotificationRepository {

    private final NotificationJpaRepository jpaRepository;

    public JpaNotificationRepository(NotificationJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public PagedResult<Notification> getByUser(UUID userId, int page, int pageSize) {
        PageRequest pageRequest = PageRequest.of(Math.max(page - 1, 0), pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Notification> result = jpaRepository.findByRecipientIdOrderByCreatedAtDesc(userId, pageRequest);
        return new PagedResult<>(result.getContent(), (int) result.getTotalElements(), page, pageSize);
    }

    @Override
    public int getUnreadCount(UUID userId) {
        return jpaRepository.countByRecipientIdAndIsReadFalse(userId);
    }

    @Override
    public void add(Notification notification) {
        jpaRepository.save(notification);
    }

    @Override
    public void markRead(UUID notificationId, UUID userId) {
        jpaRepository.markRead(notificationId, userId);
    }

    @Override
    public void markAllRead(UUID userId) {
        jpaRepository.markAllRead(userId);
    }
}
