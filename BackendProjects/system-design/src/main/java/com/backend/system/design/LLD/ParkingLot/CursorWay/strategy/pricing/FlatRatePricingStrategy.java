package com.backend.system.design.LLD.ParkingLot.CursorWay.strategy.pricing;

import com.backend.system.design.LLD.ParkingLot.CursorWay.enums.VehicleType;
import com.backend.system.design.LLD.ParkingLot.CursorWay.models.ParkingTicket;

import java.util.HashMap;
import java.util.Map;


/**
 * Flat Rate Pricing Strategy - Fixed price per vehicle type regardless of duration
 * Useful for events, daily passes, etc.
 */
public class FlatRatePricingStrategy implements PricingStrategy {


    private final Map<VehicleType, Double> flatRates;

    public FlatRatePricingStrategy() {
        this.flatRates = new HashMap<>();
        initializeRates();
    }

    private void initializeRates() {
        flatRates.put(VehicleType.BIKE, 50.0);    // ₹50 flat for bikes
        flatRates.put(VehicleType.CAR, 100.0);    // ₹100 flat for cars
        flatRates.put(VehicleType.TRUCK, 200.0);  // ₹200 flat for trucks
    }

    @Override
    public double calculateFee(ParkingTicket ticket) {
        if (ticket == null) {
            throw new IllegalArgumentException("Ticket cannot be null");
        }

        VehicleType vehicleType = ticket.getVehicle().getVehicleType();
        double fee = flatRates.getOrDefault(vehicleType, 100.0);

        System.out.println(String.format("Calculating flat rate fee - Vehicle: %s, Type: %s, Fee: ₹%.2f",
                ticket.getVehicle().getRegistrationNumber(), vehicleType.getDisplayName(), fee));

        return fee;
    }

    @Override
    public String getStrategyName() {
        return "Flat Rate Pricing Strategy";
    }

    public void updateRate(VehicleType vehicleType, double newRate) {
        if (newRate < 0) {
            throw new IllegalArgumentException("Rate cannot be negative");
        }
        flatRates.put(vehicleType, newRate);
        System.out.println(String.format("Updated flat rate for %s to ₹%.2f", vehicleType.getDisplayName(), newRate));
    }
}

