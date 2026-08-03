package com.urbanfleet.order_service.service;

import com.urbanfleet.order_service.constants.OrderAction;
import com.urbanfleet.order_service.constants.OrderStatus;
import org.springframework.stereotype.Service;

@Service
public class OrderStateMachine {

    public OrderStatus nextState(OrderStatus currentStatus,
                                 OrderAction action) {

        return switch (currentStatus) {

            // =====================================================
            // Order Created
            // =====================================================
            case CREATED -> switch (action) {

                case CONFIRM_PAYMENT -> OrderStatus.PAID;

                case CANCEL -> OrderStatus.CANCELLED;

                default -> throw new RuntimeException(
                        "Invalid action " + action + " for CREATED");
            };

            // =====================================================
            // Payment Successful
            // =====================================================
            case PAID -> switch (action) {

                case START_PREPARING -> OrderStatus.PREPARING;

                case ASSIGN_DELIVERY -> OrderStatus.DELIVERY_ASSIGNED;

                case CANCEL -> OrderStatus.CANCELLED;

                default -> throw new RuntimeException(
                        "Invalid action " + action + " for PAID");
            };

            // =====================================================
            // Restaurant Preparing Food
            // =====================================================
            case PREPARING -> switch (action) {

                case MARK_READY -> OrderStatus.READY;

                case CANCEL -> OrderStatus.CANCELLED;

                default -> throw new RuntimeException(
                        "Invalid action " + action + " for PREPARING");
            };

            // =====================================================
            // Food Ready
            // =====================================================
            case READY -> switch (action) {

                case ASSIGN_DELIVERY -> OrderStatus.DELIVERY_ASSIGNED;

                case CANCEL -> OrderStatus.CANCELLED;

                default -> throw new RuntimeException(
                        "Invalid action " + action + " for READY");
            };

            // =====================================================
            // Rider Assigned
            // =====================================================
            case DELIVERY_ASSIGNED -> switch (action) {

                /*
                 * Rider accepted the delivery.
                 * Order status remains DELIVERY_ASSIGNED because
                 * customer still sees "Rider Assigned".
                 */
                case DELIVERY_ACCEPTED -> OrderStatus.DELIVERY_ASSIGNED;

                /*
                 * Rider collected the food.
                 */
                case PICKUP_ORDER -> OrderStatus.OUT_FOR_DELIVERY;

                /*
                 * Rider rejected.
                 * Another rider will be assigned.
                 * Status doesn't change.
                 */
                case ASSIGN_DELIVERY -> OrderStatus.DELIVERY_ASSIGNED;

                case CANCEL -> OrderStatus.CANCELLED;

                default -> throw new RuntimeException(
                        "Invalid action " + action + " for DELIVERY_ASSIGNED");
            };

            // =====================================================
            // Rider is delivering
            // =====================================================
            case OUT_FOR_DELIVERY -> switch (action) {

                case COMPLETE -> OrderStatus.DELIVERED;

                default -> throw new RuntimeException(
                        "Invalid action " + action + " for OUT_FOR_DELIVERY");
            };

            // =====================================================
            // Final States
            // =====================================================
            case DELIVERED ->
                    throw new RuntimeException(
                            "Order already delivered");

            case CANCELLED ->
                    throw new RuntimeException(
                            "Order already cancelled");
        };
    }
}