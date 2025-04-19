package com.backend.design.pattern.prototypePattern;

public class TwoWheeler extends Vehicle {

    private final boolean isEngine;

    public TwoWheeler(String engine, String model, long price, boolean isEngine) {
        super(engine, model, price);
        this.isEngine = isEngine;
    }
}
