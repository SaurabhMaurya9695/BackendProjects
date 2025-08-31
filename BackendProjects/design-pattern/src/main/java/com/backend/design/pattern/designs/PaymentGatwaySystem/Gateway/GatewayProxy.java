package com.backend.design.pattern.designs.PaymentGatwaySystem.Gateway;

import com.backend.design.pattern.designs.PaymentGatwaySystem.Gateway.ConcreteGateways.PaytmGateway;
import com.backend.design.pattern.designs.PaymentGatwaySystem.Gateway.ConcreteGateways.RazorpayGateway;
import com.backend.xjc.PaymentRequest;

/**
 * Proxy class for {@link PaymentGateway} implementations (e.g., Paytm, Razorpay).
 * <p>
 * Ensures every payment request goes through the {@link PaymentGateway}
 * template method while adding retry and logging behavior.
 */
public class GatewayProxy extends PaymentGateway {

    private final PaymentGateway _gateway;
    private final int _retries;

    /**
     * Creates a proxy for a specific {@link PaymentGateway}.
     *
     * @param paymentGateway the real gateway (e.g., {@code PaytmGateway}, {@code RazorpayGateway})
     * @param retries        number of retries for failed payment attempts
     */
    public GatewayProxy(PaymentGateway paymentGateway, int retries) {
        this._gateway = paymentGateway;
        this._retries = retries;
    }

    /**
     * Processes a payment using the template method with retry support.
     *
     * @param request the payment request
     * @return {@code true} if payment succeeds, {@code false} otherwise
     */
    @Override
    public final boolean processPayment(PaymentRequest request) {
        boolean result = false;
        int attempt = 0;

        while (attempt < _retries && !result) {
            if (attempt > 0) {
                System.out.printf("[GatewayProxy] Retrying payment (attempt %d/%d) for Sender=%s%n", attempt + 1,
                        _retries, request.getSenderName());
            }
            result = _gateway.processPayment(request);
            attempt++;
        }

        if (!result) {
            System.out.printf("[GatewayProxy] ❌ Failed to process payment after %d attempts | Sender=%s%n", attempt,
                    request.getSenderName());
        } else {
            System.out.printf("[GatewayProxy] ✅ Payment succeeded after %d attempt(s) | Sender=%s%n", attempt,
                    request.getSenderName());
        }

        return result;
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
