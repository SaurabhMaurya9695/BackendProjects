package com.backend.design.pattern.designs.PaymentGatwaySystem.Gateway;

import com.backend.design.pattern.designs.PaymentGatwaySystem.Gateway.ConcreteGateways.PaytmGateway;
import com.backend.design.pattern.designs.PaymentGatwaySystem.Gateway.ConcreteGateways.RazorpayGateway;
import com.backend.design.pattern.designs.PaymentGatwaySystem.Strategies.RetryStrategy;
import com.backend.xjc.PaymentRequest;

/**
 * Proxy class for {@link PaymentGateway} implementations (e.g., Paytm, Razorpay).
 * <p>
 * Ensures every payment request goes through the {@link PaymentGateway}
 * template method while adding retry and logging behavior.
 */
public class GatewayProxy extends PaymentGateway {

    private final PaymentGateway _gateway;
    private RetryStrategy _retryStrategy;

    /**
     * Creates a proxy for a specific {@link PaymentGateway}.
     *
     * @param paymentGateway the real gateway (e.g., {@code PaytmGateway}, {@code RazorpayGateway})
     * @param strategy        number of retries for failed payment attempts
     */
    public GatewayProxy(PaymentGateway paymentGateway, RetryStrategy strategy) {
        this._gateway = paymentGateway;
        _retryStrategy = strategy;
    }

    /**
     * Processes a payment using the template method with retry support.
     *
     * @param request the payment request
     * @return {@code true} if payment succeeds, {@code false} otherwise
     */
    @Override
    public final boolean processPayment(PaymentRequest request) {
        return _retryStrategy.executeWithRetry(() -> _gateway.processPayment(request), request);
    }

    @Override
    protected boolean validatePayment(PaymentRequest req) {
        logDelegation("validatePayment");
        return _gateway.validatePayment(req);
    }

    @Override
    protected boolean initiatePayment(PaymentRequest request) {
        logDelegation("initiatePayment");
        return _gateway.initiatePayment(request);
    }

    @Override
    protected boolean confirmPayment(PaymentRequest request) {
        logDelegation("confirmPayment");
        return _gateway.confirmPayment(request);
    }

    private void logDelegation(String method) {
        String gatewayName = (_gateway instanceof PaytmGateway) ? "PaytmGateway"
                : (_gateway instanceof RazorpayGateway) ? "RazorpayGateway" : _gateway.getClass().getSimpleName();

        System.out.println("[GatewayProxy] Delegating Req to " + gatewayName + " -> " + method);
    }
}
