package com.backend.system.design.LLD.ParkingLot.SaurabhWay.models;

public class ParkingIdentifierSymbol {

    private final Character _identifier;

    public ParkingIdentifierSymbol(Character identifier) {
        this._identifier = identifier;
    }

    public ParkingIdentifierSymbol() {
        this._identifier = '-';
    }

    public Character getIdentifier() {
        return _identifier;
    }
}
