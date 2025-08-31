package com.backend.design.pattern.designs.PaymentGatwaySystem.PaymentBankingSystem;

/**
 * Represents a banking system abstraction in the Payment Gateway System.
 * <p>
 * This interface defines the contract for processing payments
 * through a banking system implementation.
 * </p>
 */
public interface IBankingSystem {

    /**
     * Processes a payment request with the specified amount.
     *
     * @param amount the transaction amount to be processed
     * @return {@code true} if the payment is successful,
     * {@code false} otherwise
     */
    boolean processPayment(double amount);
}
