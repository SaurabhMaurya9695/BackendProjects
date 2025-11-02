package com.backend.system.design.LLD.ParkingLot.CursorWay.strategy.pricing;

import com.backend.system.design.LLD.ParkingLot.CursorWay.enums.VehicleType;
import com.backend.system.design.LLD.ParkingLot.CursorWay.models.ParkingTicket;

import java.util.HashMap;
import java.util.Map;

/**
 * Hourly Pricing Strategy - Charges based on hours parked and vehicle type
 * Following Strategy Pattern
 */
public class HourlyPricingStrategy implements PricingStrategy {

    // Rate per hour for each vehicle type
    private final Map<VehicleType, Double> hourlyRates;
    private final double baseRate;

    public HourlyPricingStrategy() {
        this.baseRate = 10.0; // Base rate for first hour
        this.hourlyRates = new HashMap<>();
        initializeRates();
    }

    public HourlyPricingStrategy(double baseRate) {
        this.baseRate = baseRate;
        this.hourlyRates = new HashMap<>();
        initializeRates();
    }

    private void initializeRates() {
        hourlyRates.put(VehicleType.BIKE, 10.0);   // ₹10 per hour for bikes
        hourlyRates.put(VehicleType.CAR, 20.0);    // ₹20 per hour for cars
        hourlyRates.put(VehicleType.TRUCK, 40.0);  // ₹40 per hour for trucks
    }

    @Override
    public double calculateFee(ParkingTicket ticket) {
        if (ticket == null) {
            throw new IllegalArgumentException("Ticket cannot be null");
        }

        VehicleType vehicleType = ticket.getVehicle().getVehicleType();
        long hours = ticket.getParkingDurationInHours();

        // Minimum charge is for 1 hour
        if (hours < 1) {
            hours = 1;
        }

        double hourlyRate = hourlyRates.getOrDefault(vehicleType, baseRate);
        double totalFee = hours * hourlyRate;

        System.out.println(
                String.format("Calculating hourly fee - Vehicle: %s, Type: %s, Hours: %d, Rate: ₹%.2f/hr, Total: ₹%.2f",
                        ticket.getVehicle().getRegistrationNumber(), vehicleType.getDisplayName(), hours, hourlyRate,
                        totalFee));

        return totalFee;
    }

    @Override
    public String getStrategyName() {
        return "Hourly Pricing Strategy";
    }

    public void updateRate(VehicleType vehicleType, double newRate) {
        if (newRate < 0) {
            throw new IllegalArgumentException("Rate cannot be negative");
        }
        hourlyRates.put(vehicleType, newRate);
        System.out.println(String.format("Updated hourly rate for %s to ₹%.2f", vehicleType.getDisplayName(), newRate));
    }

    public double getRate(VehicleType vehicleType) {
        return hourlyRates.getOrDefault(vehicleType, baseRate);
    }
}

