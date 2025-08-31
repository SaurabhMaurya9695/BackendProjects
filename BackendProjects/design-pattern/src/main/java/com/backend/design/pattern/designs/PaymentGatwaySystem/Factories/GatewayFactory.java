package com.backend.design.pattern.designs.PaymentGatwaySystem.Factories;

import com.backend.design.pattern.designs.PaymentGatwaySystem.Gateway.ConcreteGateways.PaytmGateway;
import com.backend.design.pattern.designs.PaymentGatwaySystem.Gateway.ConcreteGateways.RazorpayGateway;
import com.backend.design.pattern.designs.PaymentGatwaySystem.Gateway.GatewayProxy;
import com.backend.design.pattern.designs.PaymentGatwaySystem.Gateway.PaymentGateway;
import com.backend.design.pattern.designs.PaymentGatwaySystem.Strategies.ExponentialBackoffStrategy;
import com.backend.design.pattern.designs.PaymentGatwaySystem.Strategies.LinearBackoffStrategy;
import com.backend.xjc.GatewayType;

/**
 * {@code GatewayFactory} is a Singleton Factory responsible for creating instances
 * of {@link PaymentGateway}.
 *
 * <p>
 * This factory ensures that:
 * <ul>
 *   <li>Gateway instantiation logic is centralized.</li>
 *   <li>All gateways are wrapped in a {@link GatewayProxy} for cross-cutting concerns
 *       (like logging, monitoring, security checks).</li>
 *   <li>New payment gateways can be added without changing client code
 *       (only update this factory).</li>
 * </ul>
 * <p>
 * Example usage:
 * <pre>{@code
 * GatewayFactory factory = GatewayFactory.getInstance();
 * PaymentGateway gateway = factory.getGateway(GatewayType.PAYTM);
 * gateway.processPayment(request);
 * }</pre>
 */
public class GatewayFactory {

    /**
     * Singleton instance of the factory
     */
    private static volatile GatewayFactory _instance;

    /**
     * Private constructor to prevent external instantiation
     */
    private GatewayFactory() {
    }

    /**
     * Returns the singleton instance of {@code GatewayFactory}.
     * Uses double-checked locking for thread-safety.
     *
     * @return singleton instance
     */
    public static GatewayFactory getInstance() {
        if (_instance == null) {
            synchronized (GatewayFactory.class) {
                if (_instance == null) {
                    _instance = new GatewayFactory();
                }
            }
        }
        return _instance;
    }

    /**
     * Returns a {@link PaymentGateway} implementation based on the given {@link GatewayType}.
     * <p>
     * Each created gateway is wrapped inside a {@link GatewayProxy} to apply retry and backoff strategies.
     * </p>
     *
     * @param type the type of payment gateway (e.g., PAYTM, RAZORPAY)
     * @return a proxied {@link PaymentGateway} instance with the appropriate backoff strategy
     * @throws IllegalArgumentException if the specified gateway type is not supported
     */
    public PaymentGateway getFactory(GatewayType type) {
        return switch (type) {
            case PAYTM -> new GatewayProxy(new PaytmGateway(), new LinearBackoffStrategy(5, 1000));
            case RAZORPAY -> new GatewayProxy(new RazorpayGateway(), new ExponentialBackoffStrategy(5, 5000, true));
            default -> throw new IllegalArgumentException("Unsupported GatewayType: " + type);
        };
    }
}
