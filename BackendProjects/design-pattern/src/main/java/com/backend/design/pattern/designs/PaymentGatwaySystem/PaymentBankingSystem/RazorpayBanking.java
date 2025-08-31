package com.backend.design.pattern.designs.PaymentGatwaySystem.PaymentBankingSystem;

/**
 * Concrete implementation of {@link IBankingSystem} that simulates
 * payment processing through Razorpay's banking system.
 * <p>
 * This class demonstrates how Razorpay integrates with the Payment Gateway
 * System to handle financial transactions.
 * </p>
 */
public class RazorpayBanking implements IBankingSystem {

    /**
     * Processes the payment request using Razorpay's banking service.
     * <p>
     * For demo purposes, this implementation logs the transaction
     * details and always returns {@code true}.
     * </p>
     *
     * @param amount the transaction amount to be processed
     * @return {@code true} if the payment succeeds,
     * {@code false} otherwise
     */
    @Override
    public boolean processPayment(double amount) {
        System.out.println("[RazorpayBanking] Processing amount: " + amount);
        return true;
    }
}
