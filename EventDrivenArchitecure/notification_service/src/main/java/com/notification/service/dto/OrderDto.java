package com.notification.service.dto;

import java.util.Date;

public class OrderDto {

    private String userId;
    private String orderId;
    private Date createdDate;
    private String price;
    private String userEmailId;
    private String userPhoneNo;

    public OrderDto() {
    }

    public String getUserId() {
        return userId;
    }

    public String getOrderId() {
        return orderId;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public String getPrice() {
        return price;
    }

    public String getUserEmailId() {
        return userEmailId;
    }

    public String getUserPhoneNo() {
        return userPhoneNo;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setUserEmailId(String userEmailId) {
        this.userEmailId = userEmailId;
    }

    public void setUserPhoneNo(String userPhoneNo) {
        this.userPhoneNo = userPhoneNo;
    }

    @Override
    public String toString() {
        return "OrderDto{" + "userId='" + userId + '\'' + ", orderId='" + orderId + '\'' + ", createdDate="
                + createdDate + ", price='" + price + '\'' + ", userEmailId='" + userEmailId + '\'' + ", userPhoneNo='"
                + userPhoneNo + '\'' + '}';
    }
}
