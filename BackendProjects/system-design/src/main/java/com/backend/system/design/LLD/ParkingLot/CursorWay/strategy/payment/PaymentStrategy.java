package com.backend.system.design.LLD.ParkingLot.CursorWay.strategy.payment;

/**
 * Strategy Pattern for Payment Processing
 * Interface Segregation Principle - Simple, focused interface
 * Dependency Inversion Principle - High-level modules depend on this abstraction
 */
public interface PaymentStrategy {
    /**
     * Process payment for the given amount
     * @param amount Amount to be paid
     * @param ticketId Ticket ID for reference
     * @return true if payment successful, false otherwise
     */
    boolean processPayment(double amount, String ticketId);
    
    /**
     * Get the payment method name
     * @return Payment method name
     */
    String getPaymentMethodName();
    
    /**
     * Validate if payment details are correct before processing
     * @return true if valid, false otherwise
     */
    boolean validatePaymentDetails();
}

