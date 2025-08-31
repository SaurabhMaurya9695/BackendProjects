package com.backend.design.pattern.designs.PaymentGatwaySystem.Strategies;

import com.backend.xjc.PaymentRequest;

import java.util.function.Supplier;

/**
 * Strategy interface for retry mechanisms.
 */
public interface RetryStrategy {

    /**
     * Executes an operation with retry logic.
     *
     * @param operation the operation to retry (e.g., gateway.processPayment)
     * @param request   the payment request
     * @return {@code true} if operation eventually succeeds, {@code false} otherwise
     */
    boolean executeWithRetry(Supplier<Boolean> operation, PaymentRequest request);
}
