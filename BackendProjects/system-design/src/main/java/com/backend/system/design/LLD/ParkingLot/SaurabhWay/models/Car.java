package com.backend.system.design.LLD.ParkingLot.SaurabhWay.models;

import com.backend.system.design.LLD.ParkingLot.SaurabhWay.enums.VehicleType;
import com.backend.system.design.LLD.ParkingLot.SaurabhWay.strategy.PriceStrategy;

public class Car extends Vehicle {

    private final VehicleSize _vehicleSize;

    public Car(String registrationNumber, String model, Tyre tyre, ParkingIdentifierSymbol symbol,
            PriceStrategy priceStrategy) {
        super(registrationNumber, model, tyre, symbol, priceStrategy);
        _vehicleSize = new VehicleSize(20, 20);
    }

    @Override
    public String getType() {
        return VehicleType.CAR.toString();
    }

    public VehicleSize getVehicleSize() {
        return _vehicleSize;
    }
}
