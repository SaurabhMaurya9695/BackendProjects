package com.backend.system.design.LLD.ParkingLot.CursorWay.enums;

/**
 * Enum representing different payment methods
 */
public enum PaymentMethod {
    CASH("Cash"),
    CREDIT_CARD("Credit Card"),
    UPI("UPI");

    private final String displayName;

    PaymentMethod(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

