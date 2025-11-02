package com.backend.system.design.LLD.ParkingLot.CursorWay.enums;

/**
 * Enum representing the status of a parking spot
 */
public enum SpotStatus {
    AVAILABLE("Available"),
    OCCUPIED("Occupied"),
    RESERVED("Reserved"),
    OUT_OF_SERVICE("Out of Service");

    private final String displayName;

    SpotStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

