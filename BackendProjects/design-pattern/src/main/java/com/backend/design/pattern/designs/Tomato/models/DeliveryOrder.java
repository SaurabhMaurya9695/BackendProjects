package com.backend.design.pattern.designs.Tomato.models;

public class DeliveryOrder extends Order {

    private String _userAddress;

    public DeliveryOrder() {
        _userAddress = "";
    }

    @Override
    public String getType() {
        return "Delivery";
    }

    public void setUserAddress(String addr) {
        _userAddress = addr;
    }

    public String getUserAddress() {
        return _userAddress;
    }

    // Implement remaining Order methods with actual fields
}

