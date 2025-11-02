package com.backend.system.design.LLD.ParkingLot.SaurabhWay.factory;

import com.backend.system.design.LLD.ParkingLot.SaurabhWay.models.Vehicle;
import com.backend.system.design.LLD.ParkingLot.SaurabhWay.strategy.PriceByStartTimeAndEndTime;
import com.backend.system.design.LLD.ParkingLot.SaurabhWay.strategy.PriceByTyre;
import com.backend.system.design.LLD.ParkingLot.SaurabhWay.strategy.PriceStrategy;

public class ParkingFactory {

    private static ParkingFactory _instance = null;

    public static ParkingFactory getInstance() {
        if (_instance == null) {
            _instance = new ParkingFactory();
        }
        return _instance;
    }

    public PriceStrategy createPriceStrategy(Vehicle vehicle) {
        if (vehicle == null || vehicle.getType() == null) {
            throw new IllegalArgumentException("Vehicle or vehicle type cannot be null");
        }

        switch (vehicle.getType()) {
            case "CAR" -> {
                return new PriceByStartTimeAndEndTime(vehicle);
            }
            default -> {
                // Default strategy for other vehicles (e.g., Bike, Truck)
                return new PriceByTyre(vehicle);
            }
        }
    }
}
