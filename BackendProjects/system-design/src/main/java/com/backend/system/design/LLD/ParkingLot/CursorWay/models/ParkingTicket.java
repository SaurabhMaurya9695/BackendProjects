package com.backend.system.design.LLD.ParkingLot.CursorWay.models;

import com.backend.system.design.LLD.ParkingLot.CursorWay.enums.PaymentStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.UUID;

/**
 * ParkingTicket class - Represents a parking ticket issued to a vehicle
 * Following Single Responsibility Principle - Only manages ticket information
 */
public class ParkingTicket {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final String ticketId;
    private final Vehicle vehicle;
    private final ParkingSpot parkingSpot;
    private final LocalDateTime entryTime;
    private LocalDateTime exitTime;
    private PaymentStatus paymentStatus;
    private double amountPaid;

    public ParkingTicket(Vehicle vehicle, ParkingSpot parkingSpot) {
        if (vehicle == null) {
            throw new IllegalArgumentException("Vehicle cannot be null");
        }
        if (parkingSpot == null) {
            throw new IllegalArgumentException("Parking spot cannot be null");
        }

        this.ticketId = generateTicketId();
        this.vehicle = vehicle;
        this.parkingSpot = parkingSpot;
        this.entryTime = LocalDateTime.now();
        this.exitTime = null;
        this.paymentStatus = PaymentStatus.PENDING;
        this.amountPaid = 0.0;

        System.out.println(String.format("Parking ticket generated - ID: %s, Vehicle: %s, Spot: %s, Entry: %s", ticketId,
                vehicle.getRegistrationNumber(), parkingSpot.getSpotId(), entryTime.format(FORMATTER)));
    }

    private String generateTicketId() {
        return "TKT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    /**
     * Mark the exit time when vehicle wants to leave
     */
    public void markExit() {
        this.exitTime = LocalDateTime.now();
        System.out.println(String.format("Exit marked for ticket %s at %s", ticketId, exitTime.format(FORMATTER)));
    }

    /**
     * Complete the payment
     */
    public void completePayment(double amount) {
        this.amountPaid = amount;
        this.paymentStatus = PaymentStatus.COMPLETED;
        System.out.println(String.format("Payment completed for ticket %s - Amount: ₹%.2f", ticketId, amount));
    }

    /**
     * Check if payment is completed
     */
    public boolean isPaymentCompleted() {
        return paymentStatus == PaymentStatus.COMPLETED;
    }

    /**
     * Get parking duration in minutes
     */
    public long getParkingDurationInMinutes() {
        LocalDateTime endTime = exitTime != null ? exitTime : LocalDateTime.now();
        return java.time.Duration.between(entryTime, endTime).toMinutes();
    }

    /**
     * Get parking duration in hours (rounded up)
     */
    public long getParkingDurationInHours() {
        long minutes = getParkingDurationInMinutes();
        return (minutes + 59) / 60; // Round up to nearest hour
    }

    // Getters
    public String getTicketId() {
        return ticketId;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public ParkingSpot getParkingSpot() {
        return parkingSpot;
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    public LocalDateTime getExitTime() {
        return exitTime;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public double getAmountPaid() {
        return amountPaid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ParkingTicket that = (ParkingTicket) o;
        return Objects.equals(ticketId, that.ticketId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ticketId);
    }

    @Override
    public String toString() {
        return String.format("""
                        ╔═══════════════════════════════════════════════════════════╗
                        ║              PARKING TICKET                               ║
                        ╠═══════════════════════════════════════════════════════════╣
                        ║ Ticket ID      : %-40s ║
                        ║ Vehicle        : %-40s ║
                        ║ Spot           : %-40s ║
                        ║ Entry Time     : %-40s ║
                        ║ Exit Time      : %-40s ║
                        ║ Duration       : %-40s ║
                        ║ Payment Status : %-40s ║
                        ║ Amount Paid    : ₹%-39.2f ║
                        ╚═══════════════════════════════════════════════════════════╝
                        """, ticketId, vehicle.getRegistrationNumber(), parkingSpot.getSpotId(),
                entryTime.format(FORMATTER),
                exitTime != null ? exitTime.format(FORMATTER) : "Still Parked",
                getParkingDurationInMinutes() + " minutes", paymentStatus.getDisplayName(), amountPaid);
    }
}

