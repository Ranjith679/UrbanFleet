package com.urbanfleet.order_service.service;

import com.urbanfleet.order_service.constants.OrderAction;
import com.urbanfleet.order_service.constants.OrderStatus;
import org.springframework.stereotype.Service;

@Service
public class OrderStateMachine {

    // service used for changing states in correct order like real apps

    /*

 CREATED
   |
   | CONFIRM
   v
 PAID
   |
   | START_PREPARING
   v
PREPARING
   |
   | MARK_READY
   v
 PICKED_UP
   |
   | COMPLETE
   v
 DELIVERED

CANCEL can happen from:
CREATED
PAID
PREPARING

     */

    // Decides what the next order status should be
    public OrderStatus nextState(
            OrderStatus currentStatus,
            OrderAction action
    ) {

        return switch (currentStatus) {

            // ==========================
            // Order just created
            // ==========================
            case CREATED -> switch (action) {

                // Customer paid / restaurant accepted
                case CONFIRM -> OrderStatus.PAID;

                // Order cancelled before processing
                case CANCEL -> OrderStatus.CANCELLED;

                default -> throw new RuntimeException(
                        "Invalid action for CREATED order"
                );
            };

            // ==========================
            // Payment completed
            // ==========================
            case PAID -> switch (action) {

                // Restaurant starts cooking
                case START_PREPARING -> OrderStatus.PREPARING;

                // Customer cancels
                case CANCEL -> OrderStatus.CANCELLED;

                default -> throw new RuntimeException(
                        "Invalid action for PAID order"
                );
            };

            // ==========================
            // Food is being prepared
            // ==========================
            case PREPARING -> switch (action) {

                // Food ready and handed to rider
                case MARK_READY -> OrderStatus.PICKED_UP;

                // Optional:
                // Some companies allow cancellation until food is ready
                case CANCEL -> OrderStatus.CANCELLED;

                default -> throw new RuntimeException(
                        "Invalid action for PREPARING order"
                );
            };

            // ==========================
            // Rider picked up order
            // ==========================
            case PICKED_UP -> switch (action) {

                // Delivered successfully
                case COMPLETE -> OrderStatus.DELIVERED;

                default -> throw new RuntimeException(
                        "Invalid action for PICKED_UP order"
                );
            };

            // ==========================
            // Final states
            // ==========================
            case DELIVERED ->
                    throw new RuntimeException(
                            "Order already delivered"
                    );

            case CANCELLED ->
                    throw new RuntimeException(
                            "Order already cancelled"
                    );
        };
    }
}
