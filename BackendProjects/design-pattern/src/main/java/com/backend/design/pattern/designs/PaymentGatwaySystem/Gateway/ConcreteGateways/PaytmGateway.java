package com.backend.design.pattern.designs.PaymentGatwaySystem.Gateway.ConcreteGateways;

import com.backend.design.pattern.designs.PaymentGatwaySystem.Gateway.PaymentGateway;
import com.backend.design.pattern.designs.PaymentGatwaySystem.PaymentBankingSystem.IBankingSystem;
import com.backend.design.pattern.designs.PaymentGatwaySystem.PaymentBankingSystem.PaytmBanking;
import com.backend.xjc.CurrencyType;
import com.backend.xjc.PaymentRequest;

/**
 * Concrete implementation of {@link PaymentGateway} that simulates
 * payment processing via the Paytm system.
 *
 * <p>This class follows the <b>Template Method</b> pattern defined in
 * {@link PaymentGateway}, and provides Paytm-specific implementations
 * for validating, initiating, and confirming payments.</p>
 *
 * <p><b>Key Responsibilities:</b></p>
 * <ul>
 *   <li>Validate that the payment request is correct and in INR.</li>
 *   <li>Delegate transaction initiation to {@link PaytmBanking} for
 *       banking operations.</li>
 *   <li>Confirm the payment once the transaction is successful.</li>
 * </ul>
 *
 * <p>Note: In this simulation, confirmation always succeeds.</p>
 * <p>
 * Example:
 * <pre>
 *     PaymentRequest request = new PaymentRequest("Alice", "Bob", 500, CurrencyType.INR);
 *     PaymentGateway gateway = new PaytmGateway();
 *     boolean result = gateway.processPayment(request);
 * </pre>
 *
 * @see PaymentGateway
 * @see PaytmBanking
 */
public class PaytmGateway extends PaymentGateway {

    private final IBankingSystem _iBankingSystem;

    /**
     * Initializes a PaytmGateway with a {@link PaytmBanking} instance
     * to handle the underlying banking operations.
     */
    public PaytmGateway() {
        _iBankingSystem = new PaytmBanking();
    }

    /**
     * Validates the payment request by checking:
     * <ul>
     *   <li>Amount is greater than 0</li>
     *   <li>Currency is {@link CurrencyType#INR}</li>
     * </ul>
     *
     * @param req the {@link PaymentRequest} containing payment details
     * @return true if the request is valid; false otherwise
     */
    @Override
    protected boolean validatePayment(PaymentRequest req) {
        System.out.println("[PaytmGateway] Validating payment for " + req.getSenderName());
        return !(req.getAmount() <= 0) && req.getCurrency().equals(CurrencyType.INR);
    }

    /**
     * Initiates the payment by logging transaction details and
     * delegating to the {@link IBankingSystem}.
     *
     * @param request the {@link PaymentRequest} containing payment details
     * @return true if the banking system approves the payment; false otherwise
     */
    @Override
    protected boolean initiatePayment(PaymentRequest request) {
        System.out.println(String.format(
                "[PaytmGateway] Transaction started | Sender=%s | Receiver=%s | Amount=%.2f %s | Status=INITIATED",
                request.getSenderName(), request.getReceiverName(), request.getAmount(), request.getCurrency()));

        return _iBankingSystem.processPayment(request.getAmount());
    }

    /**
     * Confirms the payment. In this simulation, confirmation is always successful.
     *
     * @param paymentRequest the {@link PaymentRequest} containing payment details
     * @return true (always)
     */
    @Override
    protected boolean confirmPayment(PaymentRequest paymentRequest) {
        System.out.println("[PaytmGateway] Paytm Confirmation is done ");
        return true;
    }
}
