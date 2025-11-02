package com.backend.system.design.LLD.ParkingLot.CursorWay.models;

import com.backend.system.design.LLD.ParkingLot.CursorWay.enums.SpotSize;
import com.backend.system.design.LLD.ParkingLot.CursorWay.enums.SpotStatus;

import java.util.Objects;

/**
 * ParkingSpot class - Represents a single parking spot
 * Following Single Responsibility Principle - Only manages spot state and properties
 */
public class ParkingSpot {
    private final String spotId;
    private final SpotSize spotSize;
    private final int floor;
    private SpotStatus status;
    private Vehicle parkedVehicle;

    public ParkingSpot(String spotId, SpotSize spotSize, int floor) {
        if (spotId == null || spotId.trim().isEmpty()) {
            throw new IllegalArgumentException("Spot ID cannot be null or empty");
        }
        if (floor < 0) {
            throw new IllegalArgumentException("Floor number cannot be negative");
        }

        this.spotId = spotId;
        this.spotSize = spotSize;
        this.floor = floor;
        this.status = SpotStatus.AVAILABLE;
        this.parkedVehicle = null;

        System.out.println(String.format("Parking spot created - ID: %s, Size: %s, Floor: %d", 
            spotId, spotSize.getDescription(), floor));
    }

    /**
     * Check if this spot can accommodate the given vehicle
     * Following Liskov Substitution Principle - works with any Vehicle subtype
     */
    public boolean canAccommodate(Vehicle vehicle) {
        if (vehicle == null) {
            return false;
        }
        return this.status == SpotStatus.AVAILABLE && 
               this.spotSize.getSizeUnits() >= vehicle.getRequiredSpotSize().getSizeUnits();
    }

    /**
     * Park a vehicle in this spot
     */
    public boolean parkVehicle(Vehicle vehicle) {
        if (!canAccommodate(vehicle)) {
            System.out.println(String.format("⚠️  Cannot park vehicle %s in spot %s - Spot not available or size mismatch", 
                vehicle.getRegistrationNumber(), spotId));
            return false;
        }

        this.parkedVehicle = vehicle;
        this.status = SpotStatus.OCCUPIED;
        System.out.println(String.format("Vehicle %s parked in spot %s (Floor: %d)", 
            vehicle.getRegistrationNumber(), spotId, floor));
        return true;
    }

    /**
     * Remove vehicle from this spot
     */
    public Vehicle removeVehicle() {
        if (this.status != SpotStatus.OCCUPIED || this.parkedVehicle == null) {
            System.out.println(String.format("⚠️  Cannot remove vehicle from spot %s - No vehicle parked", spotId));
            return null;
        }

        Vehicle vehicle = this.parkedVehicle;
        this.parkedVehicle = null;
        this.status = SpotStatus.AVAILABLE;
        System.out.println(String.format("Vehicle %s removed from spot %s", 
            vehicle.getRegistrationNumber(), spotId));
        return vehicle;
    }

    // Getters
    public String getSpotId() {
        return spotId;
    }

    public SpotSize getSpotSize() {
        return spotSize;
    }

    public int getFloor() {
        return floor;
    }

    public SpotStatus getStatus() {
        return status;
    }

    public Vehicle getParkedVehicle() {
        return parkedVehicle;
    }

    public boolean isAvailable() {
        return status == SpotStatus.AVAILABLE;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParkingSpot that = (ParkingSpot) o;
        return Objects.equals(spotId, that.spotId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(spotId);
    }

    @Override
    public String toString() {
        return String.format("ParkingSpot{id='%s', size=%s, floor=%d, status=%s, vehicle=%s}", 
            spotId, spotSize, floor, status.getDisplayName(), 
            parkedVehicle != null ? parkedVehicle.getRegistrationNumber() : "None");
    }
}

