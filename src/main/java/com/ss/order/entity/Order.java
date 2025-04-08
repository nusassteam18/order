package com.ss.order.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "tbl_order")
public class Order {
    @Id
    @SequenceGenerator(
            name = "tbl_order_sequence",
            sequenceName = "tbl_order_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "tbl_order_sequence"
    )
    @Column(name="order_id")
    private Long orderId;

    @Column(name="customer_id", nullable = false)
    private Long customerId;

    @Column(name="restaurant_id", nullable = false)
    private Long restaurantId;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('Pending', 'Ready', 'Completed', 'Cancelled') NOT NULL DEFAULT 'Pending' CHECK (status IN ('Pending', 'Ready', 'Completed', 'Cancelled'))")
    private Status status;

    @CreationTimestamp
    @Column(name="created_at", nullable = false, updatable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Column(name="updated_at", nullable = false)
    private Date updatedAt;

    @Formula("(SELECT SUM(oi.price) FROM order_item oi WHERE oi.order_id = order_id)")
    private BigDecimal total;

    public enum Status {
        Pending,
        Ready,
        Completed,
        Cancelled
    }

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "order_id",
            foreignKey = @ForeignKey(name = "FK_ORDER_ITEM_ORDER_ID",
                    foreignKeyDefinition = "FOREIGN KEY (order_id) REFERENCES tbl_order(order_id) ON UPDATE CASCADE ON DELETE CASCADE"))
    private List<OrderItem> orderItems = new ArrayList<>();

    public Order() {
    }

    public Order(Long orderId, Long customerId, Long restaurantId, Status status, List<OrderItem> orderItems) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.restaurantId = restaurantId;
        this.status = status;
        this.orderItems = orderItems;
    }

    public Order(Long orderId, Long customerId, Long restaurantId, Status status, Date createdAt, Date updatedAt, List<OrderItem> orderItems) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.restaurantId = restaurantId;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.orderItems = orderItems;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

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

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
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

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", customerId=" + customerId +
                ", restaurantId=" + restaurantId +
                ", status=" + status +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", total=" + total +
                ", orderItems=" + orderItems +
                '}';
    }
}
