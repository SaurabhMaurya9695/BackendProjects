package com.backend.system.design.LLD.ParkingLot.SaurabhWay.models;

public class ParkingUser {

    private final String _username;
    private Vehicle _vehicle;

    public ParkingUser(String username, Vehicle vehicle) {
        this._username = username;
        this._vehicle = vehicle;
    }

    public String getUsername() {
        return _username;
    }

    public Vehicle getVehicle() {
        return _vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this._vehicle = vehicle;
    }
}

