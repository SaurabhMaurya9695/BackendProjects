package com.backend.system.design.LLD.ParkingLot.SaurabhWay;

import com.backend.system.design.LLD.ParkingLot.SaurabhWay.factory.ParkingFactory;
import com.backend.system.design.LLD.ParkingLot.SaurabhWay.models.ParkingField;
import com.backend.system.design.LLD.ParkingLot.SaurabhWay.models.ParkingUser;
import com.backend.system.design.LLD.ParkingLot.SaurabhWay.models.Vehicle;
import com.backend.system.design.LLD.ParkingLot.SaurabhWay.state.ParkingState;
import com.backend.system.design.LLD.ParkingLot.SaurabhWay.strategy.PriceStrategy;

public class ParkingLot {

    private final ParkingField _field;
    private final ParkingUser _parkingUser;
    private final ParkingFactory _factory;

    public ParkingLot(ParkingUser parkingUser) {
        _field = new ParkingField();
        _parkingUser = parkingUser;
        _factory = new ParkingFactory();
    }

    public void parkVehicle(Vehicle vehicle) {
        if (vehicle.getState() == ParkingState.NOT_PARKED) {
            System.out.println("[ParkingLot] Vehicle is not parked yet");
            _factory.createPriceStrategy(vehicle);
        } else {
            System.out.println("[ParkingLot] Vehicle is already parked, Can't parked again");
        }
    }
}
