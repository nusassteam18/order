package com.ss.order.responseDTO;

public class SendOrderResponse {
    private int returnCode;
    private String errorMessage;
    private Long orderId;

    public SendOrderResponse() {
    }

    public SendOrderResponse(int returnCode, Long orderId) {
        this.returnCode = returnCode;
        this.orderId = orderId;
    }

    public SendOrderResponse(int returnCode, String errorMessage, Long orderId) {
        this.returnCode = returnCode;
        this.errorMessage = errorMessage;
        this.orderId = orderId;
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

    @Override
    public String toString() {
        return "SendOrderResponse{" +
                "returnCode=" + returnCode +
                ", errorMessage='" + errorMessage + '\'' +
                ", orderId=" + orderId +
                '}';
    }
}
