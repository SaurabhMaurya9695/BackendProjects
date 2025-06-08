package com.backend.design.pattern.designs.Tomato.models;

public class PickupOrder extends Order {

    private String _restaurantAddress;

    public PickupOrder() {
        _restaurantAddress = "";
    }

    @Override
    public String getType() {
        return "Pickup";
    }

    public void setRestaurantAddress(String addr) {
        _restaurantAddress = addr;
    }

    public String getRestaurantAddress() {
        return _restaurantAddress;
    }

    // Implement remaining Order methods with actual fields
}
