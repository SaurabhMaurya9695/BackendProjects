package com.backend.system.design.LLD.ParkingLot.CursorWay.enums;

/**
 * Enum representing payment status
 */
public enum PaymentStatus {
    PENDING("Payment Pending"),
    COMPLETED("Payment Completed"),
    FAILED("Payment Failed"),
    REFUNDED("Payment Refunded");

    private final String displayName;

    PaymentStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

