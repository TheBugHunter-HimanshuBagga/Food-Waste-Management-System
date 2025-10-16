package com.foodwaste.dto;

public class OrderResponse {
    
    private String orderId;
    private String qrCode;
    private String status;
    private String message;

    // Constructors
    public OrderResponse() {}

    public OrderResponse(String orderId, String qrCode, String status, String message) {
        this.orderId = orderId;
        this.qrCode = qrCode;
        this.status = status;
        this.message = message;
    }

    // Getters and Setters
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getQrCode() { return qrCode; }
    public void setQrCode(String qrCode) { this.qrCode = qrCode; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}