package com.backend.system.design.LLD.ParkingLot.CursorWay.strategy.payment;


import java.util.regex.Pattern;

/**
 * UPI Payment Strategy - Concrete implementation of PaymentStrategy
 */
public class UPIPayment implements PaymentStrategy {
    // UPI ID pattern: username@bankname
    private static final Pattern UPI_ID_PATTERN = Pattern.compile("^[a-zA-Z0-9.\\-_]{3,}@[a-zA-Z]{3,}$");

    private final String upiId;
    private String transactionId;

    public UPIPayment(String upiId) {
        this.upiId = upiId;
    }

    @Override
    public boolean processPayment(double amount, String ticketId) {
        System.out.println(String.format("Processing UPI payment - Ticket: %s, Amount: ₹%.2f, UPI ID: %s",
            ticketId, amount, upiId));

        if (!validatePaymentDetails()) {
            System.out.println("⚠️  " + "Invalid UPI ID: " + upiId);
            return false;
        }

        // Simulate UPI payment processing
        try {
            System.out.println("Initiating UPI payment request...");
            Thread.sleep(100); // Simulate network delay
            
            System.out.println("Waiting for UPI authorization...");
            Thread.sleep(100); // Simulate user authorization on phone
            
            // Generate transaction ID
            transactionId = generateTransactionId();
            
            System.out.println(String.format("UPI payment successful - UPI ID: %s, Amount: ₹%.2f, Transaction ID: %s", 
                upiId, amount, transactionId));
            
            System.out.println(String.format("📱 UPI payment successful! Transaction ID: %s", transactionId));
            return true;
            
        } catch (InterruptedException e) {
            System.out.println("❌ " + "UPI payment processing interrupted: " + e.getMessage());
            Thread.currentThread().interrupt();
            return false;
        }
    }

    @Override
    public String getPaymentMethodName() {
        return "UPI";
    }

    @Override
    public boolean validatePaymentDetails() {
        if (upiId == null || upiId.trim().isEmpty()) {
            return false;
        }
        return UPI_ID_PATTERN.matcher(upiId).matches();
    }

    private String generateTransactionId() {
        return "UPI" + System.currentTimeMillis() + (int)(Math.random() * 1000);
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getUpiId() {
        return upiId;
    }
}

