package com.backend.system.design.LLD.ParkingLot.CursorWay.enums;

/**
 * Enum representing different types of vehicles
 */
public enum VehicleType {
    BIKE("Bike"),
    CAR("Car"),
    TRUCK("Truck");

    private final String displayName;

    VehicleType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

