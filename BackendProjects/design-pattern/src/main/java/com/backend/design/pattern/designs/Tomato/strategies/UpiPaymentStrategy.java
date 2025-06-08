package com.backend.design.pattern.designs.Tomato.strategies;

public class UpiPaymentStrategy implements PaymentStrategy {

    private final String _mobile;

    public UpiPaymentStrategy(String mob) {
        this._mobile = mob;
    }

    @Override
    public void pay(double amount) {
        System.out.println("Paid ₹" + amount + " using UPI (" + _mobile + ")");
    }
}
