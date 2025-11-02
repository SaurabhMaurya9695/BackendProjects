package com.backend.system.design.LLD.ParkingLot.SaurabhWay.models;

import com.backend.design.pattern.designs.Tomato.utils.TimeUtils;
import com.backend.system.design.LLD.ParkingLot.SaurabhWay.state.ParkingState;
import com.backend.system.design.LLD.ParkingLot.SaurabhWay.strategy.PriceStrategy;

public abstract class Vehicle {

    private final String _model;
    private final String _registrationNumber;
    private final Tyre _tyre;
    private final String _startTime;
    private final String _endTime;
    private final ParkingIdentifierSymbol _symbol;
    private final PriceStrategy _priceStrategy;
    private ParkingState _state;

    public Vehicle(String registrationNumber, String model, Tyre tyre, ParkingIdentifierSymbol symbol,
            PriceStrategy _priceStrategy) {
        this._registrationNumber = registrationNumber;
        this._model = model;
        this._tyre = tyre;
        this._startTime = TimeUtils.getCurrentTime();
        this._endTime = "";
        this._symbol = symbol;
        this._priceStrategy = _priceStrategy;
        this._state = ParkingState.NOT_PARKED;
        System.out.println("Settings Start Time for the Vehicle " + getType() + " " + _startTime);
    }

    public String getRegistrationNumber() {
        return _registrationNumber;
    }

    public String getModel() {
        return _model;
    }

    public String getStartTime() {
        return _startTime;
    }

    public Tyre getTyre() {
        return _tyre;
    }

    // Optional: Each vehicle can describe itself
    public abstract String getType();

    public String getEndTime() {
        return _endTime;
    }

    public ParkingIdentifierSymbol getSymbol() {
        return _symbol;
    }

    public PriceStrategy getPriceStrategy() {
        return _priceStrategy;
    }

    public ParkingState getState() {
        return _state;
    }

    public void setState(ParkingState state) {
        _state = _state;
    }
}
