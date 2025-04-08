package com.ss.order.requestDTO;

import com.ss.order.entity.Order;

public class SendOrderStatusUpdateRequest {
    private Long orderId;
    private Order.Status status;
    private Long restaurantId;

    public SendOrderStatusUpdateRequest() {
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Order.Status getStatus() {
        return status;
    }

    public void setStatus(Order.Status status) {
        this.status = status;
    }

    public Long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Long restaurantId) {
        this.restaurantId = restaurantId;
    }

    @Override
    public String toString() {
        return "SendOrderStatusUpdateRequest{" +
                "orderId=" + orderId +
                ", status=" + status +
                ", restaurantId=" + restaurantId +
                '}';
    }
}
