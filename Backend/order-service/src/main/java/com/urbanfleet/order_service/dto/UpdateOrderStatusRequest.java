package com.urbanfleet.order_service.dto;


import com.urbanfleet.order_service.constants.OrderStatus;

public class UpdateOrderStatusRequest {

    private OrderStatus status;

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}
