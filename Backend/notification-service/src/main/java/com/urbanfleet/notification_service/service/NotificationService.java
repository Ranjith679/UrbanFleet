package com.urbanfleet.notification_service.service;

import com.urbanfleet.notification_service.entity.Notification;
import com.urbanfleet.notification_service.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository repository;

    public Notification notify(
            UUID orderId,
            String type,
            String message
    ) {

        Notification notification = Notification.builder()
                .orderId(orderId)
                .type(type)
                .message(message)
                .read(false)
                .createdAt(LocalDateTime.now())
                .build();

        Notification saved = repository.save(notification);

        log.info(
                "Notification created for order {} : {}",
                orderId,
                message
        );

        return saved;
    }

    public List<Notification> getByOrderId(UUID orderId) {
        return repository.findByOrderIdOrderByCreatedAtDesc(orderId);
    }

    public List<Notification> getUnread() {
        return repository.findByReadFalseOrderByCreatedAtDesc();
    }

    public Notification markAsRead(UUID notificationId) {

        Notification notification = repository.findById(notificationId)
                .orElseThrow(() ->
                        new RuntimeException("Notification not found"));

        notification.setRead(true);

        return repository.save(notification);
    }

    public List<Notification> getUnreadByOrderId(UUID orderId) {

        return repository
                .findByOrderIdAndReadFalseOrderByCreatedAtDesc(orderId);
    }

    public void markAllAsRead(UUID orderId) {

        List<Notification> notifications =
                repository.findByOrderIdAndReadFalseOrderByCreatedAtDesc(orderId);

        notifications.forEach(notification ->
                notification.setRead(true)
        );

        repository.saveAll(notifications);
    }

    public long getUnreadCount(UUID orderId) {

        return repository.countByOrderIdAndReadFalse(orderId);
    }

    // =====================================================
    // ORDER
    // =====================================================

    public void orderCreated(UUID orderId) {

        save(
                orderId,
                "Your order " + orderId +
                        " has been created successfully."
        );
    }


    // =====================================================
    // PAYMENT
    // =====================================================

    public void paymentSuccessful(UUID orderId) {

        save(
                orderId,
                "Payment successful for order " + orderId + "."
        );
    }

    public void paymentFailed(UUID orderId) {

        save(
                orderId,
                "Payment failed for order " + orderId + "."
        );
    }


    // =====================================================
    // DELIVERY
    // =====================================================

    public void deliveryAssigned(UUID orderId) {

        save(
                orderId,
                "A rider has been assigned to your order "
                        + orderId + "."
        );
    }

    public void deliveryAccepted(UUID orderId) {

        save(
                orderId,
                "The rider has accepted your delivery for order "
                        + orderId + "."
        );
    }

    public void deliveryRejected(UUID orderId) {

        save(
                orderId,
                "The rider rejected the delivery for order "
                        + orderId + "."
        );
    }

    public void orderPickedUp(UUID orderId) {

        save(
                orderId,
                "Your order " + orderId +
                        " has been picked up and is on the way."
        );
    }

    public void orderDelivered(UUID orderId) {

        save(
                orderId,
                "Your order " + orderId +
                        " has been delivered successfully."
        );
    }

    public void deliveryCancelled(UUID orderId) {

        save(
                orderId,
                "Delivery for order " + orderId +
                        " has been cancelled."
        );
    }


    // =====================================================
    // COMMON SAVE
    // =====================================================

    private void save(UUID orderId, String message) {

        Notification notification = new Notification();

        notification.setOrderId(orderId);
        notification.setMessage(message);
        notification.setRead(false);

        repository.save(notification);

        log.info(
                "Notification saved | orderId={} | message={}",
                orderId,
                message
        );
    }
}