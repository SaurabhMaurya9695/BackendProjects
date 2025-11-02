package com.backend.system.design.LLD.ParkingLot.CursorWay.models;

import com.backend.system.design.LLD.ParkingLot.CursorWay.enums.SpotSize;
import com.backend.system.design.LLD.ParkingLot.CursorWay.enums.VehicleType;

import java.util.Objects;

/**
 * Abstract Vehicle class - Following Open/Closed Principle
 * Open for extension (new vehicle types) but closed for modification
 */
public abstract class Vehicle {

    private final String registrationNumber;
    private final String ownerName;
    private final VehicleType vehicleType;

    protected Vehicle(String registrationNumber, String ownerName, VehicleType vehicleType) {
        if (registrationNumber == null || registrationNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Registration number cannot be null or empty");
        }
        if (ownerName == null || ownerName.trim().isEmpty()) {
            throw new IllegalArgumentException("Owner name cannot be null or empty");
        }

        this.registrationNumber = registrationNumber;
        this.ownerName = ownerName;
        this.vehicleType = vehicleType;

        System.out.println(
                String.format("Vehicle created - Type: %s, Registration: %s, Owner: %s", vehicleType.getDisplayName(),
                        registrationNumber, ownerName));
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    /**
     * Abstract method to get required spot size - Following Template Method Pattern
     * Each vehicle type will define its own size requirement
     */
    public abstract SpotSize getRequiredSpotSize();

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Vehicle vehicle = (Vehicle) o;
        return Objects.equals(registrationNumber, vehicle.registrationNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(registrationNumber);
    }

    @Override
    public String toString() {
        return String.format("%s{registrationNumber='%s', owner='%s'}", vehicleType.getDisplayName(),
                registrationNumber, ownerName);
    }
}

