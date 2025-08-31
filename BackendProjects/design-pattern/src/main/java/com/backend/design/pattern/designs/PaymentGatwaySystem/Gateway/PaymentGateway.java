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
     * <p>
     * Executes the complete payment flow by delegating step-specific logic
     * to subclasses. If any step fails, the process is aborted and {@code false}
     * is returned.
     * </p>
     *
     * @param request the {@link PaymentRequest} containing payment details
     * @return {@code true} if the entire process completes successfully,
     * {@code false} otherwise
     */
    public final boolean processPayment(PaymentRequest request) {
        if (!validatePayment(request)) {
            System.out.println("[PaymentGateway] Validation failed for sender " + request.getSenderName());
            return false;
        }

        if (!initiatePayment(request)) {
            System.out.println("[PaymentGateway] Initiation failed for sender " + request.getSenderName());
            return false;
        }

        if (!confirmPayment(request)) {
            System.out.println("[PaymentGateway] Confirmation failed for sender " + request.getSenderName());
            return false;
        }

        System.out.println("[PaymentGateway] Process Payment is Successful for sender " + request.getSenderName());
        return true;
    }

    /**
     * Validates the payment request before processing.
     *
     * @param paymentRequest the payment request to validate
     * @return {@code true} if validation succeeds, {@code false} otherwise
     */
    protected abstract boolean validatePayment(PaymentRequest paymentRequest);

    /**
     * Initiates the payment after successful validation.
     *
     * @param paymentRequest the payment request to initiate
     * @return {@code true} if initiation succeeds, {@code false} otherwise
     */
    protected abstract boolean initiatePayment(PaymentRequest paymentRequest);

    /**
     * Confirms the payment after initiation.
     *
     * @param paymentRequest the payment request to confirm
     * @return {@code true} if confirmation succeeds, {@code false} otherwise
     */
    protected abstract boolean confirmPayment(PaymentRequest paymentRequest);
}
