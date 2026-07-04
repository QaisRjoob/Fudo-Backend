package com.fudo.notification.repository;

import com.fudo.notification.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface NotificationJpaRepository extends JpaRepository<Notification, UUID> {

    Page<Notification> findByRecipientIdOrderByCreatedAtDesc(UUID recipientId, Pageable pageable);

    int countByRecipientIdAndIsReadFalse(UUID recipientId);

    @Modifying
    @Query("update Notification n set n.isRead = true where n.id = :notificationId and n.recipientId = :userId")
    void markRead(@Param("notificationId") UUID notificationId, @Param("userId") UUID userId);

    @Modifying
    @Query("update Notification n set n.isRead = true where n.recipientId = :userId and n.isRead = false")
    void markAllRead(@Param("userId") UUID userId);
}
