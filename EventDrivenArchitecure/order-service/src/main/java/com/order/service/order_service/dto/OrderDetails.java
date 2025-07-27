package com.order.service.order_service.dto;

public class OrderDetails {

    private String userId;
    private String orderId;
    private String userEmailId;

    private String amount;
    private String userPhoneNo;
    private boolean orderPaymentStatus = false;
    private boolean orderStatus = false;

    public OrderDetails() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getUserEmailId() {
        return userEmailId;
    }

    public void setUserEmailId(String userEmailId) {
        this.userEmailId = userEmailId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getUserPhoneNo() {
        return userPhoneNo;
    }

    public void setUserPhoneNo(String userPhoneNo) {
        this.userPhoneNo = userPhoneNo;
    }

    public boolean isOrderPaymentStatus() {
        return orderPaymentStatus;
    }

    public void setOrderPaymentStatus(boolean orderPaymentStatus) {
        this.orderPaymentStatus = orderPaymentStatus;
    }

    public boolean isOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(boolean orderStatus) {
        this.orderStatus = orderStatus;
    }
}
