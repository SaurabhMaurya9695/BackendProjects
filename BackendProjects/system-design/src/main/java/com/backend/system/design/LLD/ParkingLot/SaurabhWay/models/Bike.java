package com.backend.system.design.LLD.ParkingLot.SaurabhWay.models;

import com.backend.system.design.LLD.ParkingLot.SaurabhWay.enums.VehicleType;
import com.backend.system.design.LLD.ParkingLot.SaurabhWay.strategy.PriceStrategy;

public class Bike extends Vehicle {

    private final VehicleSize _vehicleSize;

    public Bike(String registrationNumber, String model, Tyre tyre, ParkingIdentifierSymbol symbol,
            PriceStrategy priceStrategy) {
        super(registrationNumber, model, tyre, symbol, priceStrategy);
        _vehicleSize = new VehicleSize(10, 10);
    }

    @Override
    public String getType() {
        return VehicleType.BIKE.toString();
    }

    public VehicleSize getVehicleSize() {
        return _vehicleSize;
    }
}
