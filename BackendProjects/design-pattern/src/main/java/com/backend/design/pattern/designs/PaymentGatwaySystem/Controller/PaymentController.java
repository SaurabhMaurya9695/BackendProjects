package com.backend.design.pattern.designs.PaymentGatwaySystem.Controller;

import com.backend.design.pattern.designs.PaymentGatwaySystem.Factories.GatewayFactory;
import com.backend.design.pattern.designs.PaymentGatwaySystem.Gateway.PaymentGateway;
import com.backend.design.pattern.designs.PaymentGatwaySystem.Service.PaymentService;
import com.backend.xjc.GatewayType;
import com.backend.xjc.PaymentRequest;

/**
 * Singleton controller that coordinates payment processing.
 * <p>
 * - Selects the proper {@link PaymentGateway} via {@link GatewayFactory}. <br>
 * - Configures {@link PaymentService} with the gateway and delegates payment execution.
 */
public class PaymentController {

    private static PaymentController _instance;

    private PaymentController() {
    }

    /**
     * @return the single instance of {@code PaymentController}
     */
    public static PaymentController getInstance() {
        if (_instance == null) {
            _instance = new PaymentController();
        }
        return _instance;
    }

    /**
     * Handles payment by selecting gateway and processing the request.
     */
    public void handlePayment(GatewayType type, PaymentRequest request) {
        PaymentGateway gateway = GatewayFactory.getInstance().getFactory(type);
        PaymentService.getInstance().setGateway(gateway);
        PaymentService.getInstance().processPayment(request);
    }
}
