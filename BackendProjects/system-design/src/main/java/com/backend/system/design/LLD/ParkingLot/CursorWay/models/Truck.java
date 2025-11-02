package com.backend.system.design.LLD.ParkingLot.CursorWay.models;

import com.backend.system.design.LLD.ParkingLot.CursorWay.enums.SpotSize;
import com.backend.system.design.LLD.ParkingLot.CursorWay.enums.VehicleType;

/**
 * Truck class - Concrete implementation of Vehicle
 */
public class Truck extends Vehicle {

    public Truck(String registrationNumber, String ownerName) {
        super(registrationNumber, ownerName, VehicleType.TRUCK);
    }

    @Override
    public SpotSize getRequiredSpotSize() {
        return SpotSize.LARGE;
    }
}

