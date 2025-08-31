package com.backend.design.pattern.designs.PaymentGatwaySystem.Service;

import com.backend.design.pattern.designs.PaymentGatwaySystem.Gateway.PaymentGateway;
import com.backend.xjc.PaymentRequest;

/**
 * Singleton service for managing and processing payments via a {@link PaymentGateway}.
 * <p>
 * - Holds the configured gateway instance. <br>
 * - Provides methods to set, get, and process payments.
 */
public class PaymentService {

    private static PaymentService _instance;
    private PaymentGateway _gateway;

    private PaymentService() {
    }

    /**
     * @return the single instance of {@code PaymentService}
     */
    public static PaymentService getInstance() {
        if (_instance == null) {
            _instance = new PaymentService();
        }
        return _instance;
    }

    /**
     * Sets the active payment gateway.
     */
    public void setGateway(PaymentGateway gateway) {
        _gateway = gateway;
    }

    /**
     * @return the active payment gateway
     */
    public PaymentGateway getGateway() {
        return _gateway;
    }

    /**
     * Delegates payment processing to the configured gateway.
     */
    public void processPayment(PaymentRequest request) {
        _gateway.processPayment(request);
    }
}
