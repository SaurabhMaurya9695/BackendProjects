package com.backend.system.design.LLD.ParkingLot.SaurabhWay.strategy;

import com.backend.system.design.LLD.ParkingLot.SaurabhWay.models.Tyre;
import com.backend.system.design.LLD.ParkingLot.SaurabhWay.models.Vehicle;

public class PriceByTyre implements PriceStrategy {

    private final Vehicle _vehicle;
    private final Tyre _tyre;

    public PriceByTyre(Vehicle vehicle) {
        _vehicle = vehicle;
        _tyre = vehicle.getTyre();
    }

    @Override
    public int calculatePrice() {
        // we can assume one tyre price is 10 rupees;
        System.out.println("[PriceByTyre] Price of the Vehicle " + _vehicle.getType() + " is " + 10 * _tyre.getTyre());
        return 10 * _tyre.getTyre();
    }
}
