package com.ss.order.requestDTO;

import com.ss.order.entity.OrderItem;

import java.util.List;

public class SendOrderRequest {
    private Long customerId;
    private Long restaurantId;
    private List<OrderItem> orderItems;

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Long restaurantId) {
        this.restaurantId = restaurantId;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    @Override
    public String toString() {
        return "SendOrderRequest{" +
                "customerId=" + customerId +
                ", restaurantId=" + restaurantId +
                ", orderItems=" + orderItems +
                '}';
    }
}
