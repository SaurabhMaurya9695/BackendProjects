package com.backend.design.pattern.designs.PaymentGatwaySystem.PaymentBankingSystem;

/**
 * Concrete implementation of {@link IBankingSystem} that simulates
 * payment processing through Paytm's banking system.
 * <p>
 * This class demonstrates how a specific banking provider
 * (Paytm) can integrate with the Payment Gateway System.
 * </p>
 */
public class PaytmBanking implements IBankingSystem {

    /**
     * Processes the payment request using Paytm's banking service.
     * <p>
     * For demo purposes, this implementation logs the transaction
     * details and always returns {@code false}.
     * </p>
     *
     * @param amount the transaction amount to be processed
     * @return {@code true} if the payment succeeds,
     * {@code false} otherwise
     */
    @Override
    public boolean processPayment(double amount) {
        System.out.println("[PaytmBanking] Processing amount: " + amount);
        return false;
    }
}
