package com.backend.system.design.LLD.ParkingLot.SaurabhWay.models;

import com.backend.system.design.LLD.ParkingLot.SaurabhWay.enums.VehicleType;
import com.backend.system.design.LLD.ParkingLot.SaurabhWay.strategy.PriceStrategy;

public class Truck extends Vehicle {

    public Truck(String registrationNumber, String model, Tyre tyre, ParkingIdentifierSymbol symbol,
            PriceStrategy priceStrategy) {
        super(registrationNumber, model, tyre, symbol, priceStrategy);
    }

    @Override
    public String getType() {
        return VehicleType.TRUCK.toString();
    }
}

