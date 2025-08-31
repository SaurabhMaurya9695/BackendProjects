package com.backend.design.pattern.designs.PaymentGatwaySystem.Gateway;

import com.backend.xjc.PaymentRequest;

/**
 * Abstract base class representing a generic Payment Gateway.
 * <p>
 * This class defines the <b>Template Method pattern</b> for processing payments.
 * The {@link #processPayment(PaymentRequest)} method enforces a standard flow:
 * <ol>
 *   <li>Validate the payment request</li>
 *   <li>Initiate the payment</li>
 *   <li>Confirm the payment</li>
 * </ol>
 * Each step is defined as an abstract method and must be implemented
 * by concrete gateway providers (e.g., Paytm, Razorpay).
 * </p>
 */
public abstract class PaymentGateway {

    /**
     * Template method for processing a payment.
     *
     * @param request the {@link PaymentRequest} containing payment details
     * @return {@code true} if the entire process completes successfully,
     * {@code false} otherwise
     */
    public boolean processPayment(PaymentRequest request) {
        System.out.printf("[PaymentGateway] Starting payment flow | Sender=%s | Receiver=%s | Amount=%.2f %s%n",
                request.getSenderName(), request.getReceiverName(), request.getAmount(), request.getCurrency());

        if (!validatePayment(request)) {
            System.out.printf("[PaymentGateway] ❌ Validation failed for Sender=%s%n", request.getSenderName() + "\n");
            return false;
        }

        if (!initiatePayment(request)) {
            System.out.printf("[PaymentGateway] ❌ Initiation failed for Sender=%s%n", request.getSenderName() + "\n");
            return false;
        }

        if (!confirmPayment(request)) {
            System.out.printf("[PaymentGateway] ❌ Confirmation failed for Sender=%s%n", request.getSenderName() + "\n");
            return false;
        }

        System.out.printf("[PaymentGateway] ✅ Payment SUCCESS | Sender=%s | Receiver=%s | Amount=%.2f %s%n",
                request.getSenderName(), request.getReceiverName(), request.getAmount(), request.getCurrency());
        return true;
    }

    protected abstract boolean validatePayment(PaymentRequest paymentRequest);

    protected abstract boolean initiatePayment(PaymentRequest paymentRequest);

    protected abstract boolean confirmPayment(PaymentRequest paymentRequest);
}
