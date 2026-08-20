package com.urbanfleet.notification_service.controller;

import com.urbanfleet.notification_service.entity.Notification;
import com.urbanfleet.notification_service.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    // Get all notifications for an order
    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<Notification>> getByOrderId(
            @PathVariable UUID orderId) {

        return ResponseEntity.ok(
                notificationService.getByOrderId(orderId)
        );
    }

    // Get all unread notifications
    @GetMapping("/unread")
    public ResponseEntity<List<Notification>> getUnread() {

        return ResponseEntity.ok(
                notificationService.getUnread()
        );
    }

    // Mark one notification as read
    @PutMapping("/{notificationId}/read")
    public ResponseEntity<Notification> markAsRead(
            @PathVariable UUID notificationId) {

        return ResponseEntity.ok(
                notificationService.markAsRead(notificationId)
        );
    }

    @GetMapping("/order/{orderId}/unread")
    public ResponseEntity<List<Notification>> getUnreadByOrderId(
            @PathVariable UUID orderId) {

        return ResponseEntity.ok(
                notificationService.getUnreadByOrderId(orderId)
        );
    }

    @PutMapping("/order/{orderId}/read-all")
    public ResponseEntity<Void> markAllAsRead(
            @PathVariable UUID orderId) {

        notificationService.markAllAsRead(orderId);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/order/{orderId}/unread/count")
    public ResponseEntity<Long> getUnreadCount(
            @PathVariable UUID orderId) {

        return ResponseEntity.ok(
                notificationService.getUnreadCount(orderId)
        );
    }
}