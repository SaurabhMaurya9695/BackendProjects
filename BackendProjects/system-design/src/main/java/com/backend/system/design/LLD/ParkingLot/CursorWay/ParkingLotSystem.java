package com.backend.system.design.LLD.ParkingLot.CursorWay;

import com.backend.system.design.LLD.ParkingLot.CursorWay.enums.SpotSize;
import com.backend.system.design.LLD.ParkingLot.CursorWay.managers.ParkingSpotManager;
import com.backend.system.design.LLD.ParkingLot.CursorWay.managers.PaymentManager;
import com.backend.system.design.LLD.ParkingLot.CursorWay.managers.TicketManager;
import com.backend.system.design.LLD.ParkingLot.CursorWay.models.ParkingSpot;
import com.backend.system.design.LLD.ParkingLot.CursorWay.models.ParkingTicket;
import com.backend.system.design.LLD.ParkingLot.CursorWay.models.Vehicle;
import com.backend.system.design.LLD.ParkingLot.CursorWay.strategy.payment.PaymentStrategy;
import com.backend.system.design.LLD.ParkingLot.CursorWay.strategy.pricing.PricingStrategy;

import java.util.Optional;


/**
 * Main ParkingLotSystem class - Facade Pattern
 * Provides a simplified interface to the complex parking lot subsystem
 * Following:
 * - Facade Pattern: Simplifies interaction with multiple managers
 * - Singleton Pattern: Only one instance of parking lot
 * - Dependency Inversion: Depends on abstractions (PricingStrategy)
 */
public class ParkingLotSystem {
    
    private static ParkingLotSystem instance;
    
    private final String parkingLotName;
    private final ParkingSpotManager spotManager;
    private final TicketManager ticketManager;
    private final PaymentManager paymentManager;
    
    /**
     * Private constructor for Singleton pattern
     */
    private ParkingLotSystem(String name, PricingStrategy pricingStrategy) {
        this.parkingLotName = name;
        this.spotManager = new ParkingSpotManager();
        this.ticketManager = new TicketManager();
        this.paymentManager = new PaymentManager(pricingStrategy);
        
        System.out.println(String.format("Parking Lot System '%s' initialized", name));
    }
    
    /**
     * Get instance of ParkingLotSystem (Singleton)
     */
    public static synchronized ParkingLotSystem getInstance(String name, PricingStrategy pricingStrategy) {
        if (instance == null) {
            instance = new ParkingLotSystem(name, pricingStrategy);
        }
        return instance;
    }
    
    /**
     * Initialize parking lot with spots
     */
    public void initializeParkingSpots(int smallSpots, int mediumSpots, int largeSpots, int floors) {
        System.out.println(String.format("Initializing parking spots - Small: %d, Medium: %d, Large: %d, Floors: %d",
            smallSpots, mediumSpots, largeSpots, floors));
        
        int spotCounter = 1;
        
        for (int floor = 1; floor <= floors; floor++) {
            // Add small spots (for bikes)
            for (int i = 0; i < smallSpots; i++) {
                String spotId = String.format("S-%d-%03d", floor, spotCounter++);
                spotManager.addParkingSpot(new ParkingSpot(spotId, SpotSize.SMALL, floor));
            }
            
            // Add medium spots (for cars)
            for (int i = 0; i < mediumSpots; i++) {
                String spotId = String.format("M-%d-%03d", floor, spotCounter++);
                spotManager.addParkingSpot(new ParkingSpot(spotId, SpotSize.MEDIUM, floor));
            }
            
            // Add large spots (for trucks)
            for (int i = 0; i < largeSpots; i++) {
                String spotId = String.format("L-%d-%03d", floor, spotCounter++);
                spotManager.addParkingSpot(new ParkingSpot(spotId, SpotSize.LARGE, floor));
            }
            
            spotCounter = 1; // Reset for next floor
        }
        
        System.out.println(String.format("Parking lot initialized with %d total spots", spotManager.getTotalSpots()));
        System.out.println(spotManager.getStatusSummary());
    }
    
    /**
     * Park a vehicle
     * Requirement: Vehicle gets a parking ticket upon entry
     * Requirement: Vehicle can't park on occupied space
     */
    public Optional<ParkingTicket> parkVehicle(Vehicle vehicle) {
        if (vehicle == null) {
            System.out.println("⚠️  " + "Cannot park null vehicle");
            System.out.println("❌ Invalid vehicle!");
            return Optional.empty();
        }
        
        System.out.println(String.format("Attempting to park vehicle: %s (%s)", 
            vehicle.getRegistrationNumber(), vehicle.getVehicleType().getDisplayName()));
        
        // Check if vehicle already has an active ticket
        if (ticketManager.hasActiveTicket(vehicle.getRegistrationNumber())) {
            System.out.println("⚠️  " + String.format("Vehicle %s is already parked", vehicle.getRegistrationNumber()));
            System.out.println(String.format("❌ Vehicle %s is already parked in the lot!", 
                vehicle.getRegistrationNumber()));
            return Optional.empty();
        }
        
        // Find available spot
        Optional<ParkingSpot> availableSpot = spotManager.findAvailableSpot(vehicle);
        
        if (availableSpot.isEmpty()) {
            System.out.println("⚠️  " + "No available parking spot found");
            System.out.println("❌ Sorry, no parking spots available for your vehicle type!");
            return Optional.empty();
        }
        
        ParkingSpot spot = availableSpot.get();
        
        // Park vehicle in spot
        if (!spot.parkVehicle(vehicle)) {
            System.out.println("❌ " + "Failed to park vehicle in allocated spot");
            System.out.println("❌ Failed to park vehicle. Please try again.");
            return Optional.empty();
        }
        
        // Issue parking ticket
        ParkingTicket ticket = new ParkingTicket(vehicle, spot);
        ticketManager.issueTicket(ticket);
        
        System.out.println(String.format("✅ Vehicle %s successfully parked at spot %s", 
            vehicle.getRegistrationNumber(), spot.getSpotId()));
        
        System.out.println(String.format("""
            
            ✅ VEHICLE PARKED SUCCESSFULLY!
            ════════════════════════════════════════════════════════
            Vehicle        : %s
            Type           : %s
            Parking Spot   : %s (Floor %d)
            Ticket ID      : %s
            Entry Time     : %s
            ════════════════════════════════════════════════════════
            ⚠️  Please keep your ticket for payment and exit.
            """,
            vehicle.getRegistrationNumber(),
            vehicle.getVehicleType().getDisplayName(),
            spot.getSpotId(),
            spot.getFloor(),
            ticket.getTicketId(),
            ticket.getEntryTime()
        ));
        
        return Optional.of(ticket);
    }
    
    /**
     * Unpark a vehicle
     * Requirement: Vehicle must complete payment before exiting
     * Requirement: Once payment is paid, vehicle can exit and slot is free
     */
    public boolean unparkVehicle(String ticketId, PaymentStrategy paymentStrategy) {
        if (ticketId == null || ticketId.trim().isEmpty()) {
            System.out.println("⚠️  " + "Invalid ticket ID");
            System.out.println("❌ Invalid ticket ID!");
            return false;
        }
        
        System.out.println(String.format("Processing exit for ticket: %s", ticketId));
        
        // Get ticket
        Optional<ParkingTicket> ticketOpt = ticketManager.getTicketById(ticketId);
        if (ticketOpt.isEmpty()) {
            System.out.println("⚠️  " + String.format("Ticket %s not found", ticketId));
            System.out.println("❌ Ticket not found!");
            return false;
        }
        
        ParkingTicket ticket = ticketOpt.get();
        
        // Requirement: Vehicle can't be vacant without payment
        if (!ticket.isPaymentCompleted()) {
            System.out.println("⚠️  " + String.format("Payment not completed for ticket %s", ticketId));
            System.out.println("❌ Payment not completed! Please complete payment before exiting.");
            
            // Show fee details and attempt payment
            paymentManager.displayFeeDetails(ticket);
            
            if (paymentStrategy == null) {
                System.out.println("⚠️  Please provide payment method to complete payment.");
                return false;
            }
            
            // Process payment
            boolean paymentSuccess = paymentManager.processPayment(ticket, paymentStrategy);
            if (!paymentSuccess) {
                System.out.println("❌ " + "Payment processing failed");
                return false;
            }
        }
        
        // Remove vehicle from spot
        ParkingSpot spot = ticket.getParkingSpot();
        Vehicle vehicle = spot.removeVehicle();
        
        if (vehicle == null) {
            System.out.println("❌ " + "Failed to remove vehicle from spot");
            System.out.println("❌ Error: Failed to remove vehicle from spot!");
            return false;
        }
        
        // Complete ticket
        ticketManager.completeTicket(ticketId);
        
        System.out.println(String.format("✅ Vehicle %s successfully exited from spot %s", 
            vehicle.getRegistrationNumber(), spot.getSpotId()));
        
        System.out.println(String.format("""
            
            ✅ VEHICLE EXIT SUCCESSFUL!
            ════════════════════════════════════════════════════════
            Vehicle        : %s
            Parking Spot   : %s
            Duration       : %d minutes (%d hours)
            Amount Paid    : ₹%.2f
            ════════════════════════════════════════════════════════
            Thank you for using %s!
            Drive safely! 🚗
            """,
            vehicle.getRegistrationNumber(),
            spot.getSpotId(),
            ticket.getParkingDurationInMinutes(),
            ticket.getParkingDurationInHours(),
            ticket.getAmountPaid(),
            parkingLotName
        ));
        
        return true;
    }
    
    /**
     * Get parking fee for a ticket
     */
    public double getParkingFee(String ticketId) {
        Optional<ParkingTicket> ticketOpt = ticketManager.getTicketById(ticketId);
        if (ticketOpt.isEmpty()) {
            System.out.println("⚠️  " + String.format("Ticket %s not found", ticketId));
            return 0.0;
        }
        
        return paymentManager.calculateFee(ticketOpt.get());
    }
    
    /**
     * Display fee details for a ticket
     */
    public void displayFeeDetails(String ticketId) {
        Optional<ParkingTicket> ticketOpt = ticketManager.getTicketById(ticketId);
        if (ticketOpt.isEmpty()) {
            System.out.println("❌ Ticket not found!");
            return;
        }
        
        paymentManager.displayFeeDetails(ticketOpt.get());
    }
    
    /**
     * Process payment for a ticket
     */
    public boolean processPayment(String ticketId, PaymentStrategy paymentStrategy) {
        Optional<ParkingTicket> ticketOpt = ticketManager.getTicketById(ticketId);
        if (ticketOpt.isEmpty()) {
            System.out.println("❌ Ticket not found!");
            return false;
        }
        
        return paymentManager.processPayment(ticketOpt.get(), paymentStrategy);
    }
    
    /**
     * Display parking lot status
     */
    public void displayStatus() {
        System.out.println(spotManager.getStatusSummary());
    }
    
    /**
     * Display all active tickets
     */
    public void displayActiveTickets() {
        ticketManager.displayActiveTickets();
    }
    
    /**
     * Get ticket by vehicle registration
     */
    public Optional<ParkingTicket> getTicketByVehicle(String registrationNumber) {
        return ticketManager.getActiveTicketByVehicle(registrationNumber);
    }
    
    /**
     * Check if parking lot has available spots
     */
    public boolean hasAvailableSpots() {
        return spotManager.getAvailableSpots() > 0;
    }
    
    /**
     * Get parking lot name
     */
    public String getParkingLotName() {
        return parkingLotName;
    }
    
    // Getters for managers (useful for testing and extensions)
    public ParkingSpotManager getSpotManager() {
        return spotManager;
    }
    
    public TicketManager getTicketManager() {
        return ticketManager;
    }
    
    public PaymentManager getPaymentManager() {
        return paymentManager;
    }
}

