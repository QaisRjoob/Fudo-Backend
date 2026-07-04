package com.fudo.notification.features.handler.query;

import com.fudo.notification.application.dto.query.NotificationQueries.GetNotificationsQuery;
import com.fudo.notification.application.dto.query.NotificationQueries.GetUnreadCountQuery;
import com.fudo.notification.application.dto.query.NotificationQueries.NotificationDto;
import com.fudo.notification.entity.Notification;
import com.fudo.notification.interfaces.NotificationRepository;
import com.fudo.shared.pagination.PagedResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificationQueryHandlersTest {

    @Mock
    private NotificationRepository notifications;

    @Test
    void getNotifications_mapsEntitiesToDtos() {
        GetNotificationsHandler handler = new GetNotificationsHandler(notifications);

        UUID userId = UUID.randomUUID();
        Notification n = new Notification();
        n.setRecipientId(userId);
        n.setActorId(UUID.randomUUID());
        n.setType("follow");
        n.setResourceId("user-2");
        n.setResourceType("user");
        n.setMessage("Someone followed you");
        n.setRead(false);
        n.setCreatedAt(Instant.now());

        when(notifications.getByUser(userId, 1, 20))
                .thenReturn(new PagedResult<>(List.of(n), 1, 1, 20));

        GetNotificationsQuery query = new GetNotificationsQuery();
        query.setUserId(userId);
        query.setPage(1);
        query.setPageSize(20);

        PagedResult<NotificationDto> result = handler.handle(query);

        assertThat(result.getTotalCount()).isEqualTo(1);
        assertThat(result.getItems()).hasSize(1);
        NotificationDto dto = result.getItems().get(0);
        assertThat(dto.getRecipientId()).isEqualTo(userId);
        assertThat(dto.getType()).isEqualTo("follow");
        assertThat(dto.isRead()).isFalse();
    }

    @Test
    void getUnreadCount_returnsRepositoryValue() {
        GetUnreadCountHandler handler = new GetUnreadCountHandler(notifications);

        UUID userId = UUID.randomUUID();
        when(notifications.getUnreadCount(userId)).thenReturn(3);

        GetUnreadCountQuery query = new GetUnreadCountQuery();
        query.setUserId(userId);

        assertThat(handler.handle(query)).isEqualTo(3);
    }
}
