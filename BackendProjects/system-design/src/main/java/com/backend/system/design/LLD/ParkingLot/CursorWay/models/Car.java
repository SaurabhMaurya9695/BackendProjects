package com.backend.system.design.LLD.ParkingLot.CursorWay.models;

import com.backend.system.design.LLD.ParkingLot.CursorWay.enums.SpotSize;
import com.backend.system.design.LLD.ParkingLot.CursorWay.enums.VehicleType;

/**
 * Car class - Concrete implementation of Vehicle
 */
public class Car extends Vehicle {

    public Car(String registrationNumber, String ownerName) {
        super(registrationNumber, ownerName, VehicleType.CAR);
    }

    @Override
    public SpotSize getRequiredSpotSize() {
        return SpotSize.MEDIUM;
    }
}

