package com.backend.design.pattern.designs.PaymentGatwaySystem.Gateway;

import com.backend.design.pattern.designs.PaymentGatwaySystem.Gateway.ConcreteGateways.PaytmGateway;
import com.backend.xjc.PaymentRequest;

/**
 * The {@code GatewayProxy} class serves as a proxy between clients and
 * concrete {@link PaymentGateway} implementations (e.g., Paytm, Razorpay).
 * <p>
 * It forwards payment operations (validation, initiation, confirmation)
 * to the actual {@code PaymentGateway} instance, while adding extra
 * behavior such as logging, monitoring, or custom checks.
 * <p>
 * This design allows extending functionalities without altering
 * the original gateway implementations, following the Proxy pattern.
 */
public class GatewayProxy extends PaymentGateway {

    private final PaymentGateway _gateway;

    /**
     * Creates a proxy for a specific {@link PaymentGateway}.
     *
     * @param paymentGateway the real gateway (e.g., {@code PaytmGateway}, {@code RazorpayGateway})
     *                       that handles the actual payment logic
     */
    public GatewayProxy(PaymentGateway paymentGateway) {
        _gateway = paymentGateway;
    }

    /**
     * Delegates payment validation to the underlying gateway while logging
     * the delegation details.
     *
     * @param req the payment request to validate
     * @return {@code true} if the request is valid, {@code false} otherwise
     */
    @Override
    protected boolean validatePayment(PaymentRequest req) {
        if (_gateway instanceof PaytmGateway) {
            System.out.println("[GatewayProxy] Delegating Req to PaytmGateway -> validatePayment");
        } else {
            System.out.println("[GatewayProxy] Delegating Req to RazorpayGateway -> validatePayment");
        }
        return _gateway.validatePayment(req);
    }

    /**
     * Delegates payment initiation to the underlying gateway while logging
     * the delegation details.
     *
     * @param request the payment request to initiate
     * @return {@code true} if the initiation is successful, {@code false} otherwise
     */
    @Override
    protected boolean initiatePayment(PaymentRequest request) {
        if (_gateway instanceof PaytmGateway) {
            System.out.println("[GatewayProxy] Delegating Req to PaytmGateway -> initiatePayment");
        } else {
            System.out.println("[GatewayProxy] Delegating Req to RazorpayGateway -> initiatePayment");
        }
        return _gateway.initiatePayment(request);
    }

    /**
     * Delegates payment confirmation to the underlying gateway while logging
     * the delegation details.
     *
     * @param paymentRequest the payment request to confirm
     * @return {@code true} if the confirmation succeeds, {@code false} otherwise
     */
    @Override
    protected boolean confirmPayment(PaymentRequest paymentRequest) {
        if (_gateway instanceof PaytmGateway) {
            System.out.println("[GatewayProxy] Delegating Req to PaytmGateway -> confirmPayment");
        } else {
            System.out.println("[GatewayProxy] Delegating Req to RazorpayGateway -> confirmPayment");
        }
        return _gateway.confirmPayment(paymentRequest);
    }
}
