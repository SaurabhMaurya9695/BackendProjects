package com.backend.design.pattern.prototypePattern;

public class FourWheeler extends Vehicle {

    private boolean isPetrol;

    public FourWheeler(String engine, String model, long price, boolean isPetrol) {
        super(engine, model, price);
        this.isPetrol = isPetrol;
    }

    protected FourWheeler clone() throws CloneNotSupportedException {
        return (FourWheeler) super.clone();
    }
}
