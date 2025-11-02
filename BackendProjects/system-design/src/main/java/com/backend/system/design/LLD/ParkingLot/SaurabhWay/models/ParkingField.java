package com.backend.system.design.LLD.ParkingLot.SaurabhWay.models;

public class ParkingField {

    private Character[][] _fields;

    public ParkingField() {
        _fields = new Character[50][50];

        // Initialize all slots as 'E' (Empty) or null
        for (int i = 0; i < 50; i++) {
            for (int j = 0; j < 50; j++) {
                _fields[i][j] = new ParkingIdentifierSymbol().getIdentifier();
            }
        }
    }

    public Character[][] getFields() {
        return _fields;
    }

    public void setSlot(int row, int col, Character value) {
        _fields[row][col] = value;
    }

    public Character getSlot(int row, int col) {
        return _fields[row][col];
    }
}
