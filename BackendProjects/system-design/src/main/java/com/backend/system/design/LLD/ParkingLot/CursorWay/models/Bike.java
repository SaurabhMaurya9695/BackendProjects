package com.backend.system.design.LLD.ParkingLot.CursorWay.models;

import com.backend.system.design.LLD.ParkingLot.CursorWay.enums.SpotSize;
import com.backend.system.design.LLD.ParkingLot.CursorWay.enums.VehicleType;

/**
 * Bike class - Concrete implementation of Vehicle
 */
public class Bike extends Vehicle {

    public Bike(String registrationNumber, String ownerName) {
        super(registrationNumber, ownerName, VehicleType.BIKE);
    }

    @Override
    public SpotSize getRequiredSpotSize() {
        return SpotSize.SMALL;
    }
}

