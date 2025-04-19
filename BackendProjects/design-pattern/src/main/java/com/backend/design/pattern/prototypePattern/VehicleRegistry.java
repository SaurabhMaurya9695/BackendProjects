package com.backend.design.pattern.prototypePattern;

import java.util.HashMap;
import java.util.Map;

public class VehicleRegistry {

    private static final Map<String, Vehicle> mapVehicles = new HashMap<>();

    // whenever this class calls , these prototype were ready to use
    static {
        mapVehicles.put("TWO", new TwoWheeler("120", "royal", 100000, false));
        mapVehicles.put("FOUR", new FourWheeler("120", "bmw", 8799000, true));
    }

    public Vehicle getVehicle(String vehicle) throws CloneNotSupportedException {
        System.out.println("hashcode in getVehicle: " + mapVehicles.get(vehicle).hashCode());
        return mapVehicles.get(vehicle).clone();
    }
}
