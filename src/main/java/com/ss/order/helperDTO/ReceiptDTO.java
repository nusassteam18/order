package com.ss.order.helperDTO;

import com.ss.order.entity.Order;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class ReceiptDTO {
    private Long restaurantId;
    private String restaurantName;
    private CustomerTruncatedDTO customerTruncatedDTO;
    private Long orderId;
    private Date createdAt;
    private Date updatedAt;
    private BigDecimal total;
    private Order.Status status;
    private List<OrderItemDTO> orderItemDTOs;

    public ReceiptDTO() {
    }

    public Long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Long restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public CustomerTruncatedDTO getCustomerTruncatedDTO() {
        return customerTruncatedDTO;
    }

    public void setCustomerTruncatedDTO(CustomerTruncatedDTO customerTruncatedDTO) {
        this.customerTruncatedDTO = customerTruncatedDTO;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public Order.Status getStatus() {
        return status;
    }

    public void setStatus(Order.Status status) {
        this.status = status;
    }

    public List<OrderItemDTO> getOrderItemDTOs() {
        return orderItemDTOs;
    }

    public void setOrderItemDTOs(List<OrderItemDTO> orderItemDTOs) {
        this.orderItemDTOs = orderItemDTOs;
    }

    @Override
    public String toString() {
        return "ReceiptDTO{" +
                "restaurantId=" + restaurantId +
                ", restaurantName='" + restaurantName + '\'' +
                ", customerTruncatedDTO=" + customerTruncatedDTO +
                ", orderId=" + orderId +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", total=" + total +
                ", status=" + status +
                ", orderItemDTOs=" + orderItemDTOs +
                '}';
    }
}
