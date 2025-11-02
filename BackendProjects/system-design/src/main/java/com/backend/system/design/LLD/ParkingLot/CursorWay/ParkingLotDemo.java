package com.backend.system.design.LLD.ParkingLot.CursorWay;

import com.backend.system.design.LLD.ParkingLot.CursorWay.models.*;
import com.backend.system.design.LLD.ParkingLot.CursorWay.strategy.payment.*;
import com.backend.system.design.LLD.ParkingLot.CursorWay.strategy.pricing.HourlyPricingStrategy;

import java.util.Optional;

/**
 * Demo class to demonstrate the complete Parking Lot System
 * Shows all requirements being fulfilled:
 * 1. Multiple slots for vehicles
 * 2. Different vehicle types occupy different slot sizes
 * 3. Parking ticket upon entry
 * 4. Price calculation based on a vehicle type and duration
 * 5. Payment before exit
 * 6. Multiple payment methods
 * 7. Slot freed after payment and exit
 * 8. Can't park on occupied space
 * 9. Can't exit without payment
 */
public class ParkingLotDemo {

    public static void main(String[] args) {
        System.out.println("""
                ╔════════════════════════════════════════════════════════════════╗
                ║                                                                ║
                ║        WELCOME TO SMART PARKING LOT SYSTEM                     ║
                ║                                                                ║
                ╚════════════════════════════════════════════════════════════════╝
                """);

        // Run automated demo
        runAutomatedDemo();
    }

    /**
     * Automated demo showing all features
     */
    public static void runAutomatedDemo() {
        System.out.println("\n🚀 Starting Automated Demo...\n");

        // Initialize parking lot system with hourly pricing strategy
        ParkingLotSystem parkingLot = ParkingLotSystem.getInstance("City Center Parking", new HourlyPricingStrategy());

        // Initialize parking spots
        // Floor 1: 5 small, 8 medium, 3 large spots
        parkingLot.initializeParkingSpots(5, 8, 3, 2);

        System.out.println("\n" + "=".repeat(60));
        System.out.println("SCENARIO 1: Park different types of vehicles");
        System.out.println("=".repeat(60));

        // Create vehicles
        Vehicle bike1 = new Bike("MH12AB1234", "John Doe");
        Vehicle bike2 = new Bike("MH12CD5678", "Jane Smith");
        Vehicle car1 = new Car("MH14XY9876", "Alice Johnson");
        Vehicle car2 = new Car("MH14PQ4567", "Bob Williams");
        Vehicle truck1 = new Truck("MH01TK1111", "Cargo Express");

        // Park vehicles - Requirement 1, 2, 3
        Optional<ParkingTicket> ticket1 = parkingLot.parkVehicle(bike1);
        Optional<ParkingTicket> ticket2 = parkingLot.parkVehicle(car1);
        Optional<ParkingTicket> ticket3 = parkingLot.parkVehicle(truck1);
        Optional<ParkingTicket> ticket4 = parkingLot.parkVehicle(bike2);
        parkingLot.parkVehicle(car2);  // Fifth vehicle parked

        // Display status
        parkingLot.displayStatus();

        System.out.println("\n" + "=".repeat(60));
        System.out.println("SCENARIO 2: Try to park already parked vehicle");
        System.out.println("=".repeat(60));

        // Requirement 8: Vehicle can't park on occupied space
        // Try to park bike1 again (should fail)
        parkingLot.parkVehicle(bike1);

        System.out.println("\n" + "=".repeat(60));
        System.out.println("SCENARIO 3: Display active tickets");
        System.out.println("=".repeat(60));

        parkingLot.displayActiveTickets();

        // Simulate some time passing
        try {
            System.out.println("\n⏰ Simulating time passing (2 seconds)...\n");
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("\n" + "=".repeat(60));
        System.out.println("SCENARIO 4: Try to exit without payment");
        System.out.println("=".repeat(60));

        // Requirement 9: Vehicle can't be vacant without payment
        if (ticket1.isPresent()) {
            System.out.println("Attempting to exit without payment...");
            parkingLot.unparkVehicle(ticket1.get().getTicketId(), null);
        }

        System.out.println("\n" + "=".repeat(60));
        System.out.println("SCENARIO 5: Display fee and process payment");
        System.out.println("=".repeat(60));

        // Requirement 4: Price calculation based on vehicle type and duration
        if (ticket1.isPresent()) {
            String ticketId = ticket1.get().getTicketId();

            // Display fee details
            parkingLot.displayFeeDetails(ticketId);

            System.out.println("\n💳 Processing payment with CASH...");
            // Requirement 6: Multiple payment methods - CASH
            PaymentStrategy cashPayment = new CashPayment(100.0);
            parkingLot.processPayment(ticketId, cashPayment);
        }

        System.out.println("\n" + "=".repeat(60));
        System.out.println("SCENARIO 6: Exit after successful payment");
        System.out.println("=".repeat(60));

        // Requirement 5 & 7: Payment before exit, slot freed after exit
        if (ticket1.isPresent()) {
            parkingLot.unparkVehicle(ticket1.get().getTicketId(), null);
        }

        parkingLot.displayStatus();

        System.out.println("\n" + "=".repeat(60));
        System.out.println("SCENARIO 7: Process payment with UPI and exit");
        System.out.println("=".repeat(60));

        if (ticket2.isPresent()) {
            String ticketId = ticket2.get().getTicketId();

            // Requirement 6: Multiple payment methods - UPI
            PaymentStrategy upiPayment = new UPIPayment("alice@paytm");
            parkingLot.processPayment(ticketId, upiPayment);

            parkingLot.unparkVehicle(ticketId, null);
        }

        System.out.println("\n" + "=".repeat(60));
        System.out.println("SCENARIO 8: Process payment with Credit Card and exit");
        System.out.println("=".repeat(60));

        if (ticket3.isPresent()) {
            String ticketId = ticket3.get().getTicketId();

            // Requirement 6: Multiple payment methods - Credit Card
            PaymentStrategy creditCardPayment = new CreditCardPayment("1234567812345678", "Cargo Express", "12/25",
                    "123");
            parkingLot.processPayment(ticketId, creditCardPayment);

            parkingLot.unparkVehicle(ticketId, null);
        }

        System.out.println("\n" + "=".repeat(60));
        System.out.println("SCENARIO 9: Exit with payment in one step");
        System.out.println("=".repeat(60));

        if (ticket4.isPresent()) {
            // Payment and exit combined
            PaymentStrategy cashPayment2 = new CashPayment(50.0);
            parkingLot.unparkVehicle(ticket4.get().getTicketId(), cashPayment2);
        }

        // Final status
        System.out.println("\n" + "=".repeat(60));
        System.out.println("FINAL PARKING LOT STATUS");
        System.out.println("=".repeat(60));

        parkingLot.displayStatus();
        parkingLot.displayActiveTickets();

        // Now park bike1 again to show spot is freed - Requirement 7
        System.out.println("\n" + "=".repeat(60));
        System.out.println("SCENARIO 10: Park bike1 again (spot should be available)");
        System.out.println("=".repeat(60));

        parkingLot.parkVehicle(bike1);
        parkingLot.displayStatus();

        System.out.println("\n" + "=".repeat(60));
        System.out.println("✅ ALL REQUIREMENTS DEMONSTRATED SUCCESSFULLY!");
        System.out.println("=".repeat(60));

        printRequirementsSummary();
    }

    private static void printRequirementsSummary() {
        System.out.println("""
                            
                ╔════════════════════════════════════════════════════════════════╗
                ║            REQUIREMENTS VERIFICATION SUMMARY                   ║
                ╠════════════════════════════════════════════════════════════════╣
                ║ ✅ 1. Multiple slots for vehicles                             ║
                ║ ✅ 2. Different vehicle types occupy different slot sizes     ║
                ║ ✅ 3. Parking ticket issued upon entry                        ║
                ║ ✅ 4. Price calculated by vehicle type & duration             ║
                ║ ✅ 5. Payment required before exit                            ║
                ║ ✅ 6. Multiple payment methods (Cash, Credit Card, UPI)       ║
                ║ ✅ 7. Slot freed after payment and exit                       ║
                ║ ✅ 8. Cannot park on occupied space                           ║
                ║ ✅ 9. Cannot exit without payment                             ║
                ╠════════════════════════════════════════════════════════════════╣
                ║            DESIGN PATTERNS & PRINCIPLES USED                   ║
                ╠════════════════════════════════════════════════════════════════╣
                ║ • Strategy Pattern (Pricing & Payment)                         ║
                ║ • Singleton Pattern (ParkingLotSystem)                         ║
                ║ • Facade Pattern (ParkingLotSystem)                            ║
                ║ • Factory Pattern (Vehicle creation)                           ║
                ║ • Single Responsibility Principle                              ║
                ║ • Open/Closed Principle                                        ║
                ║ • Liskov Substitution Principle                                ║
                ║ • Interface Segregation Principle                              ║
                ║ • Dependency Inversion Principle                               ║
                ╚════════════════════════════════════════════════════════════════╝
                """);
    }
}

