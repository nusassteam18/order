package com.ss.order.responseDTO;

import com.ss.order.entity.Order;

import java.util.Date;

public class SendOrderStatusUpdateResponse {
    private int returnCode;
    private String errorMessage;
    private Long orderId;
    private Order.Status status;
    private Date updatedAt;

    public SendOrderStatusUpdateResponse() {
    }

    public SendOrderStatusUpdateResponse(int returnCode) {
        this.returnCode = returnCode;
    }

    public SendOrderStatusUpdateResponse(int returnCode, Long orderId, Order.Status status, Date updatedAt) {
        this.returnCode = returnCode;
        this.orderId = orderId;
        this.status = status;
        this.updatedAt = updatedAt;
    }

    public SendOrderStatusUpdateResponse(int returnCode, String errorMessage, Long orderId, Order.Status status, Date updatedAt) {
        this.returnCode = returnCode;
        this.errorMessage = errorMessage;
        this.orderId = orderId;
        this.status = status;
        this.updatedAt = updatedAt;
    }

    public int getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(int returnCode) {
        this.returnCode = returnCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
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

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "SendOrderStatusUpdateResponse{" +
                "returnCode=" + returnCode +
                ", errorMessage='" + errorMessage + '\'' +
                ", orderId=" + orderId +
                ", status=" + status +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
