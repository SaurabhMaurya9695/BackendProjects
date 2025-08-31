package com.backend.design.pattern.designs.PaymentGatwaySystem.Gateway.ConcreteGateways;

import com.backend.design.pattern.designs.PaymentGatwaySystem.Gateway.PaymentGateway;
import com.backend.design.pattern.designs.PaymentGatwaySystem.PaymentBankingSystem.IBankingSystem;
import com.backend.design.pattern.designs.PaymentGatwaySystem.PaymentBankingSystem.RazorpayBanking;
import com.backend.xjc.CurrencyType;
import com.backend.xjc.PaymentRequest;

/**
 * RazorpayGateway is a concrete implementation of PaymentGateway
 * following the Template Method Design Pattern.
 * <p>
 * Responsibilities:
 * - Validate payment request details
 * - Initiate transaction through RazorpayBanking
 * - Confirm payment once processed
 */
public class RazorpayGateway extends PaymentGateway {

    private final IBankingSystem bankingSystem;

    public RazorpayGateway() {
        this.bankingSystem = new RazorpayBanking();
    }

    @Override
    protected boolean validatePayment(PaymentRequest req) {
        System.out.println("[RazorpayGateway] Validating payment for " + req.getSenderName());

        // Business rule: Amount must be positive and currency should be INR
        if (req.getAmount() <= 0) {
            System.out.println("[RazorpayGateway] Validation failed: Invalid amount " + req.getAmount());
            return false;
        }

        if (!CurrencyType.INR.equals(req.getCurrency())) {
            System.out.println("[RazorpayGateway] Validation failed: Unsupported currency " + req.getCurrency());
            return false;
        }

        return true;
    }

    @Override
    protected boolean initiatePayment(PaymentRequest request) {
        System.out.printf(
                "[RazorpayGateway] Transaction started | Sender=%s | Receiver=%s | Amount=%.2f %s | Status=INITIATED%n",
                request.getSenderName(), request.getReceiverName(), request.getAmount(), request.getCurrency());

        // Call underlying Razorpay banking system
        return bankingSystem.processPayment(request.getAmount());
    }

    @Override
    protected boolean confirmPayment(PaymentRequest paymentRequest) {
        System.out.println("[RazorpayGateway] Razorpay confirmation completed successfully.");
        // Always true in simulation/mock implementation
        return false;
    }
}
