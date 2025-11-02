package com.backend.system.design.LLD.ParkingLot.CursorWay.managers;

import com.backend.system.design.LLD.ParkingLot.CursorWay.enums.PaymentStatus;
import com.backend.system.design.LLD.ParkingLot.CursorWay.models.ParkingTicket;
import com.backend.system.design.LLD.ParkingLot.CursorWay.strategy.payment.PaymentStrategy;
import com.backend.system.design.LLD.ParkingLot.CursorWay.strategy.pricing.PricingStrategy;



/**
 * PaymentManager - Manages payment processing
 * Following Single Responsibility Principle - Only handles payment operations
 * Dependency Inversion Principle - Depends on PaymentStrategy and PricingStrategy abstractions
 */
public class PaymentManager {

    private final PricingStrategy pricingStrategy;

    public PaymentManager(PricingStrategy pricingStrategy) {
        if (pricingStrategy == null) {
            throw new IllegalArgumentException("Pricing strategy cannot be null");
        }
        this.pricingStrategy = pricingStrategy;
        
        System.out.println(String.format("PaymentManager initialized with %s", 
            pricingStrategy.getStrategyName()));
    }

    /**
     * Calculate parking fee for a ticket
     */
    public double calculateFee(ParkingTicket ticket) {
        if (ticket == null) {
            throw new IllegalArgumentException("Ticket cannot be null");
        }

        // Mark exit time if not already marked
        if (ticket.getExitTime() == null) {
            ticket.markExit();
        }

        double fee = pricingStrategy.calculateFee(ticket);
        
        System.out.println(String.format("Fee calculated for ticket %s - Amount: ₹%.2f using %s", 
            ticket.getTicketId(), fee, pricingStrategy.getStrategyName()));
        
        return fee;
    }

    /**
     * Process payment for a ticket using the provided payment strategy
     */
    public boolean processPayment(ParkingTicket ticket, PaymentStrategy paymentStrategy) {
        if (ticket == null) {
            throw new IllegalArgumentException("Ticket cannot be null");
        }
        if (paymentStrategy == null) {
            throw new IllegalArgumentException("Payment strategy cannot be null");
        }

        // Check if payment already completed
        if (ticket.isPaymentCompleted()) {
            System.out.println("⚠️  " + String.format("Payment already completed for ticket %s", 
                ticket.getTicketId()));
            return false;
        }

        // Calculate fee
        double fee = calculateFee(ticket);

        System.out.println(String.format("Processing payment for ticket %s - Amount: ₹%.2f using %s", 
            ticket.getTicketId(), fee, paymentStrategy.getPaymentMethodName()));

        // Validate payment details
        if (!paymentStrategy.validatePaymentDetails()) {
            System.out.println("❌ " + String.format("Payment validation failed for ticket %s", 
                ticket.getTicketId()));
            return false;
        }

        // Process payment
        boolean paymentSuccess = paymentStrategy.processPayment(fee, ticket.getTicketId());

        if (paymentSuccess) {
            ticket.completePayment(fee);
            System.out.println(String.format("✅ Payment successful for ticket %s - Method: %s, Amount: ₹%.2f", 
                ticket.getTicketId(), paymentStrategy.getPaymentMethodName(), fee));
            
            System.out.println(String.format("""
                
                ╔════════════════════════════════════════════════════════╗
                ║              PAYMENT SUCCESSFUL                        ║
                ╠════════════════════════════════════════════════════════╣
                ║ Ticket ID      : %-37s ║
                ║ Vehicle        : %-37s ║
                ║ Amount Paid    : ₹%-36.2f ║
                ║ Payment Method : %-37s ║
                ╚════════════════════════════════════════════════════════╝
                """,
                ticket.getTicketId(),
                ticket.getVehicle().getRegistrationNumber(),
                fee,
                paymentStrategy.getPaymentMethodName()
            ));
        } else {
            System.out.println("❌ " + String.format("❌ Payment failed for ticket %s", ticket.getTicketId()));
            System.out.println("❌ Payment failed. Please try again.");
        }

        return paymentSuccess;
    }

    /**
     * Get parking fee preview without processing payment
     */
    public double getFeePrevirew(ParkingTicket ticket) {
        if (ticket == null) {
            throw new IllegalArgumentException("Ticket cannot be null");
        }

        // Create a temporary copy to calculate fee without marking exit
        double fee = pricingStrategy.calculateFee(ticket);
        
        System.out.println(String.format("Fee preview for ticket %s - Amount: ₹%.2f", 
            ticket.getTicketId(), fee));
        
        return fee;
    }

    /**
     * Validate if payment can be processed
     */
    public boolean canProcessPayment(ParkingTicket ticket) {
        return ticket != null && 
               !ticket.isPaymentCompleted() && 
               ticket.getPaymentStatus() != PaymentStatus.COMPLETED;
    }

    /**
     * Display fee details
     */
    public void displayFeeDetails(ParkingTicket ticket) {
        if (ticket == null) {
            System.out.println("Invalid ticket");
            return;
        }

        double fee = calculateFee(ticket);
        
        System.out.println(String.format("""
            
            ╔════════════════════════════════════════════════════════╗
            ║              PARKING FEE DETAILS                       ║
            ╠════════════════════════════════════════════════════════╣
            ║ Ticket ID      : %-37s ║
            ║ Vehicle        : %-37s ║
            ║ Vehicle Type   : %-37s ║
            ║ Entry Time     : %-37s ║
            ║ Exit Time      : %-37s ║
            ║ Duration       : %-37s ║
            ║ Pricing Method : %-37s ║
            ║ Total Fee      : ₹%-36.2f ║
            ╚════════════════════════════════════════════════════════╝
            """,
            ticket.getTicketId(),
            ticket.getVehicle().getRegistrationNumber(),
            ticket.getVehicle().getVehicleType().getDisplayName(),
            ticket.getEntryTime(),
            ticket.getExitTime() != null ? ticket.getExitTime() : "Not marked",
            ticket.getParkingDurationInMinutes() + " minutes (" + 
                ticket.getParkingDurationInHours() + " hours)",
            pricingStrategy.getStrategyName(),
            fee
        ));
    }

    public PricingStrategy getPricingStrategy() {
        return pricingStrategy;
    }
}

