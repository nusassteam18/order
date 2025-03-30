package com.ss.order.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "order_item")
public class OrderItem {
    @Id
    @SequenceGenerator(
            name = "order_item_sequence",
            sequenceName = "order_item_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "order_item_sequence"
    )
    @Column(name="order_item_id")
    private Long orderItemId;
    @Column(name="product_id", nullable = false)
    private Long productId;
    @Column(nullable = false)
    private int quantity;
    private String description;
    @Column(nullable = false)
    private BigDecimal price;

    public OrderItem() {
    }

    public OrderItem(Long orderItemId, Long productId, int quantity, String description, BigDecimal price) {
        this.orderItemId = orderItemId;
        this.productId = productId;
        this.quantity = quantity;
        this.description = description;
        this.price = price;
    }

    public Long getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(Long orderItemId) {
        this.orderItemId = orderItemId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "orderItemId=" + orderItemId +
                ", productId=" + productId +
                ", quantity=" + quantity +
                ", description='" + description + '\'' +
                ", price=" + price +
                '}';
    }
}