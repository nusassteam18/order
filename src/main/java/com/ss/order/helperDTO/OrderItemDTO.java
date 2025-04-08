package com.ss.order.helperDTO;

import java.math.BigDecimal;

public class OrderItemDTO {
    private Long id;
    private String dish;
    private int quantity;
    private BigDecimal subtotal;
    private String specialInstructions;

    public OrderItemDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDish() {
        return dish;
    }

    public void setDish(String dish) {
        this.dish = dish;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public String getSpecialInstructions() {
        return specialInstructions;
    }

    public void setSpecialInstructions(String specialInstructions) {
        this.specialInstructions = specialInstructions;
    }

    @Override
    public String toString() {
        return "OrderItemDTO{" +
                "id=" + id +
                ", dish='" + dish + '\'' +
                ", quantity=" + quantity +
                ", subtotal=" + subtotal +
                ", specialInstructions='" + specialInstructions + '\'' +
                '}';
    }
}
