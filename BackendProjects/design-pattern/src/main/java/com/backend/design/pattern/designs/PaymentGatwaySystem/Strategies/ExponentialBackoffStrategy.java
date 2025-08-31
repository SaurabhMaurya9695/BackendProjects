package com.backend.design.pattern.designs.PaymentGatwaySystem.Strategies;

import com.backend.xjc.PaymentRequest;

import java.util.function.Supplier;

/**
 * {@code ExponentialBackoffStrategy} is an implementation of {@link RetryStrategy}
 * that retries a given operation using an exponential backoff delay.
 * <p>
 * Each retry attempt waits for a delay calculated as:
 * <pre>
 * delay = baseDelayMillis * (2 ^ (attempt - 1))
 * </pre>
 * Optionally, a random jitter can be added to avoid collision when multiple clients
 * retry at the same time.
 *
 * <p><b>Example:</b>
 * <ul>
 *   <li>Base delay = 1000 ms</li>
 *   <li>Retries = 3</li>
 *   <li>Delays will be: 1000, 2000, 4000 ms (plus jitter if enabled)</li>
 * </ul>
 *
 * <p>This strategy is particularly useful for network-related or transient failures
 * where exponential backoff helps reduce server load during retries.
 */
public class ExponentialBackoffStrategy implements RetryStrategy {

    private final int _maxRetries;
    private final long _baseDelayMillis;
    private final boolean _withJitter;

    /**
     * Constructs a new {@code ExponentialBackoffStrategy}.
     *
     * @param maxRetries      the maximum number of retry attempts
     * @param baseDelayMillis the base delay (in milliseconds) for the first attempt
     * @param withJitter      whether to add random jitter to the delay
     */
    public ExponentialBackoffStrategy(int maxRetries, long baseDelayMillis, boolean withJitter) {
        this._maxRetries = maxRetries;
        this._baseDelayMillis = baseDelayMillis;
        this._withJitter = withJitter;
    }

    /**
     * Executes an operation with exponential backoff retry strategy.
     * <p>
     * The operation will be retried up to {@code maxRetries} times until it succeeds.
     * Each retry waits for an exponentially increasing delay (optionally with jitter).
     *
     * @param operation the operation to execute, represented as a {@link Supplier<Boolean>}
     *                  where {@code true} indicates success and {@code false} indicates failure
     * @param request   the payment request containing contextual information for logging
     * @return {@code true} if the operation succeeds within the allowed retries,
     * {@code false} otherwise
     */
    @Override
    public boolean executeWithRetry(Supplier<Boolean> operation, PaymentRequest request) {
        boolean success = false;
        for (int attempt = 1; attempt <= _maxRetries; attempt++) {
            success = operation.get();
            if (success) {
                System.out.printf("[ExponentialBackoff] ✅ Success on attempt %d | Sender=%s%n", attempt,
                        request.getSenderName());
                return true;
            }

            long delay = _baseDelayMillis * (1L << (attempt - 1)); // 2^(n-1)
            if (_withJitter) {
                delay += (long) (Math.random() * _baseDelayMillis);
            }

            System.out.printf("[ExponentialBackoff] ❌ Failed attempt %d/%d | Retrying in %d ms | Sender=%s%n", attempt,
                    _maxRetries, delay, request.getSenderName());

            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            }
        }
        return false;
    }
}
