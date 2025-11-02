package com.backend.system.design.LLD.ParkingLot.CursorWay.strategy.payment;



/**
 * Cash Payment Strategy - Concrete implementation of PaymentStrategy
 */
public class CashPayment implements PaymentStrategy {

    private double cashReceived;
    private double changeToReturn;

    public CashPayment(double cashReceived) {
        this.cashReceived = cashReceived;
        this.changeToReturn = 0.0;
    }

    @Override
    public boolean processPayment(double amount, String ticketId) {
        System.out.println(String.format("Processing cash payment - Ticket: %s, Amount: ₹%.2f, Cash Received: ₹%.2f",
            ticketId, amount, cashReceived));

        if (cashReceived < amount) {
            System.out.println("⚠️  " + String.format("Insufficient cash - Required: ₹%.2f, Received: ₹%.2f", 
                amount, cashReceived));
            return false;
        }

        changeToReturn = cashReceived - amount;
        System.out.println(String.format("Cash payment successful - Change to return: ₹%.2f", changeToReturn));
        
        if (changeToReturn > 0) {
            System.out.println(String.format("💵 Please collect your change: ₹%.2f", changeToReturn));
        }
        
        return true;
    }

    @Override
    public String getPaymentMethodName() {
        return "Cash";
    }

    @Override
    public boolean validatePaymentDetails() {
        return cashReceived > 0;
    }

    public double getChangeToReturn() {
        return changeToReturn;
    }

    public void setCashReceived(double cashReceived) {
        this.cashReceived = cashReceived;
    }
}

