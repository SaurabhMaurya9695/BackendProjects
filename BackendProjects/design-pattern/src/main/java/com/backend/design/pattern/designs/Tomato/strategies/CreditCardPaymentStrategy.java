package com.backend.design.pattern.designs.Tomato.strategies;

public class CreditCardPaymentStrategy implements PaymentStrategy {

    private final String _cardNumber;

    public CreditCardPaymentStrategy(String card) {
        this._cardNumber = card;
    }

    @Override
    public void pay(double amount) {
        System.out.println("Paid ₹" + amount + " using Credit Card (" + _cardNumber + ")");
    }
}
