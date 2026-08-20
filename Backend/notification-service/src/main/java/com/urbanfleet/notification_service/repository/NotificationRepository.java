package com.urbanfleet.notification_service.repository;

import com.urbanfleet.notification_service.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface NotificationRepository
        extends JpaRepository<Notification, UUID> {

    List<Notification> findByOrderIdOrderByCreatedAtDesc(UUID orderId);

    List<Notification> findByReadFalseOrderByCreatedAtDesc();

    long countByOrderIdAndReadFalse(UUID orderId);

    List<Notification> findByOrderIdAndReadFalseOrderByCreatedAtDesc(
            UUID orderId
    );
}