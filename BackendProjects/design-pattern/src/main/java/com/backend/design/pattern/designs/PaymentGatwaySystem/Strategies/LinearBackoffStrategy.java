package com.backend.design.pattern.designs.PaymentGatwaySystem.Strategies;

import com.backend.xjc.PaymentRequest;

import java.util.function.Supplier;

/**
 * A retry strategy that retries an operation with a fixed (linear) delay between attempts.
 * <p>
 * Example: If configured with <code>maxRetries=5</code> and <code>delayMillis=1000</code>,
 * the operation will be retried up to 5 times, waiting 1000ms between each attempt.
 * </p>
 */
public class LinearBackoffStrategy implements RetryStrategy {

    private final int _maxRetries;
    private final long _delayMillis;

    /**
     * Creates a new {@link LinearBackoffStrategy}.
     *
     * @param maxRetries  the maximum number of retry attempts
     * @param delayMillis the fixed delay (in milliseconds) between attempts
     */
    public LinearBackoffStrategy(int maxRetries, long delayMillis) {
        this._maxRetries = maxRetries;
        this._delayMillis = delayMillis;
    }

    /**
     * Executes the given operation with retry logic using linear backoff.
     * <p>
     * The operation is retried until it succeeds or the maximum retry count is reached.
     * After each failure, a fixed delay is applied before retrying.
     * </p>
     *
     * @param operation the operation to execute, returning {@code true} on success, {@code false} on failure
     * @param request   the {@link PaymentRequest} used for logging context (e.g., sender info)
     * @return {@code true} if the operation succeeds within the retry limit, {@code false} otherwise
     */
    @Override
    public boolean executeWithRetry(Supplier<Boolean> operation, PaymentRequest request) {
        boolean success = false;
        for (int attempt = 1; attempt <= _maxRetries; attempt++) {
            success = operation.get();
            if (success) {
                System.out.printf("[LinearBackoff] ✅ Success on attempt %d | Sender=%s%n", attempt,
                        request.getSenderName());
                return true;
            }
            System.out.printf("[LinearBackoff] ❌ Failed attempt %d/%d | Retrying in %d ms | Sender=%s%n", attempt,
                    _maxRetries, _delayMillis, request.getSenderName());
            try {
                Thread.sleep(_delayMillis);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            }
        }
        return false;
    }
}

