package com.backend.system.design.LLD.ParkingLot.CursorWay.enums;

/**
 * Enum representing different parking spot sizes
 * Following Single Responsibility Principle - each size has its own definition
 */
public enum SpotSize {
    SMALL(1, "Small - For Bikes"),
    MEDIUM(2, "Medium - For Cars"),
    LARGE(4, "Large - For Trucks");

    private final int sizeUnits;
    private final String description;

    SpotSize(int sizeUnits, String description) {
        this.sizeUnits = sizeUnits;
        this.description = description;
    }

    public int getSizeUnits() {
        return sizeUnits;
    }

    public String getDescription() {
        return description;
    }
}

