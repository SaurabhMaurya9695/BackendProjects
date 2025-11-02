package com.backend.system.design.LLD.ParkingLot.CursorWay.strategy.payment;


import java.util.regex.Pattern;

/**
 * Credit Card Payment Strategy - Concrete implementation of PaymentStrategy
 */
public class CreditCardPayment implements PaymentStrategy {
    private static final Pattern CARD_NUMBER_PATTERN = Pattern.compile("\\d{16}");
    private static final Pattern CVV_PATTERN = Pattern.compile("\\d{3}");

    private final String cardNumber;
    private final String cardHolderName;
    private final String expiryDate;
    private final String cvv;

    public CreditCardPayment(String cardNumber, String cardHolderName, String expiryDate, String cvv) {
        this.cardNumber = cardNumber;
        this.cardHolderName = cardHolderName;
        this.expiryDate = expiryDate;
        this.cvv = cvv;
    }

    @Override
    public boolean processPayment(double amount, String ticketId) {
        System.out.println(String.format("Processing credit card payment - Ticket: %s, Amount: ₹%.2f, Card: ****%s",
            ticketId, amount, getMaskedCardNumber()));

        if (!validatePaymentDetails()) {
            System.out.println("⚠️  " + "Invalid card details");
            return false;
        }

        // Simulate payment processing
        try {
            System.out.println("Connecting to payment gateway...");
            Thread.sleep(100); // Simulate network delay
            
            System.out.println("Authorizing payment...");
            Thread.sleep(100); // Simulate authorization
            
            System.out.println(String.format("Credit card payment successful - Card: ****%s, Amount: ₹%.2f", 
                getMaskedCardNumber(), amount));
            
            System.out.println("💳 Credit card payment processed successfully!");
            return true;
            
        } catch (InterruptedException e) {
            System.out.println("❌ " + "Payment processing interrupted: " + e.getMessage());
            Thread.currentThread().interrupt();
            return false;
        }
    }

    @Override
    public String getPaymentMethodName() {
        return "Credit Card";
    }

    @Override
    public boolean validatePaymentDetails() {
        if (cardNumber == null || !CARD_NUMBER_PATTERN.matcher(cardNumber).matches()) {
            System.out.println("⚠️  " + "Invalid card number format");
            return false;
        }

        if (cardHolderName == null || cardHolderName.trim().isEmpty()) {
            System.out.println("⚠️  " + "Card holder name is required");
            return false;
        }

        if (cvv == null || !CVV_PATTERN.matcher(cvv).matches()) {
            System.out.println("⚠️  " + "Invalid CVV format");
            return false;
        }

        if (expiryDate == null || expiryDate.trim().isEmpty()) {
            System.out.println("⚠️  " + "Expiry date is required");
            return false;
        }

        return true;
    }

    private String getMaskedCardNumber() {
        if (cardNumber != null && cardNumber.length() >= 4) {
            return cardNumber.substring(cardNumber.length() - 4);
        }
        return "****";
    }
}

