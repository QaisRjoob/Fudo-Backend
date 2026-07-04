package com.fudo.notification.controller;

import com.fudo.notification.application.dto.command.NotificationCommands.CreateNotificationCommand;
import com.fudo.notification.application.dto.command.NotificationCommands.MarkAllNotificationsReadCommand;
import com.fudo.notification.application.dto.command.NotificationCommands.MarkNotificationReadCommand;
import com.fudo.notification.application.dto.query.NotificationQueries.GetNotificationsQuery;
import com.fudo.notification.application.dto.query.NotificationQueries.GetUnreadCountQuery;
import com.fudo.notification.application.dto.query.NotificationQueries.NotificationDto;
import com.fudo.shared.cqrs.Mediator;
import com.fudo.shared.pagination.PagedResult;
import com.fudo.shared.security.CurrentUser;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final Mediator mediator;

    public NotificationController(Mediator mediator) {
        this.mediator = mediator;
    }

    private UUID currentUserId() {
        return CurrentUser.require().userId();
    }

    @GetMapping
    public ResponseEntity<PagedResult<NotificationDto>> getNotifications(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int pageSize) {
        GetNotificationsQuery query = new GetNotificationsQuery();
        query.setUserId(currentUserId());
        query.setPage(page);
        query.setPageSize(pageSize);
        return ResponseEntity.ok(mediator.send(query));
    }

    @GetMapping("/unread-count")
    public ResponseEntity<Map<String, Integer>> getUnreadCount() {
        GetUnreadCountQuery query = new GetUnreadCountQuery();
        query.setUserId(currentUserId());
        Integer count = mediator.send(query);
        return ResponseEntity.ok(Map.of("count", count));
    }

    @PostMapping("/{notificationId}/read")
    public ResponseEntity<Void> markRead(@PathVariable UUID notificationId) {
        MarkNotificationReadCommand cmd = new MarkNotificationReadCommand();
        cmd.setNotificationId(notificationId);
        cmd.setUserId(currentUserId());
        mediator.send(cmd);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/read-all")
    public ResponseEntity<Void> markAllRead() {
        MarkAllNotificationsReadCommand cmd = new MarkAllNotificationsReadCommand();
        cmd.setUserId(currentUserId());
        mediator.send(cmd);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/internal/create")
    public ResponseEntity<Void> createNotification(@Valid @RequestBody CreateNotificationCommand cmd) {
        mediator.send(cmd);
        return ResponseEntity.noContent().build();
    }
}
