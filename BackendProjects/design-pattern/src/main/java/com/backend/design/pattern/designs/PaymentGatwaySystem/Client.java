package com.backend.design.pattern.designs.PaymentGatwaySystem;

import com.backend.design.pattern.designs.PaymentGatwaySystem.Controller.PaymentController;
import com.backend.xjc.CurrencyType;
import com.backend.xjc.GatewayType;
import com.backend.xjc.PaymentRequest;

/**
 * Client demonstrating payment flow using {@link PaymentController}.
 * <p>
 * - Creates a {@link PaymentRequest}. <br>
 * - Processes payment through different gateways (Paytm, Razorpay). <br>
 * - Handles exceptions gracefully.
 */
public class Client {

    public static void main(String[] args) {
        try {
            // Build payment request
            PaymentRequest request = new PaymentRequest();
            request.setAmount(1500);
            request.setCurrency(CurrencyType.INR);
            request.setReceiverName("Priyanka");
            request.setSenderName("Saurabh");

            // Get controller
            PaymentController controller = PaymentController.getInstance();

            // First: Paytm
            System.out.println("Processing payment via Paytm...");
            controller.handlePayment(GatewayType.PAYTM, request);

            // Second: Razorpay
            System.out.println("\nProcessing payment via Razorpay...");
            controller.handlePayment(GatewayType.RAZORPAY, request);
        } catch (Exception e) {
            System.err.println("[Client] OOPS, exception occurred: " + e.getMessage());
        }
    }
}
