package com.backend.system.design.LLD.ParkingLot.CursorWay.managers;

import com.backend.system.design.LLD.ParkingLot.CursorWay.models.ParkingTicket;
import com.backend.system.design.LLD.ParkingLot.CursorWay.models.Vehicle;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


/**
 * TicketManager - Manages all parking tickets
 * Following Single Responsibility Principle - Only handles ticket operations
 * Thread-safe implementation
 */
public class TicketManager {

    private final Map<String, ParkingTicket> ticketsByTicketId;
    private final Map<String, ParkingTicket> ticketsByVehicleRegistration;
    private final Set<String> completedTicketIds;

    public TicketManager() {
        this.ticketsByTicketId = new ConcurrentHashMap<>();
        this.ticketsByVehicleRegistration = new ConcurrentHashMap<>();
        this.completedTicketIds = Collections.synchronizedSet(new HashSet<>());
        
        System.out.println("TicketManager initialized");
    }

    /**
     * Issue a new parking ticket
     */
    public ParkingTicket issueTicket(ParkingTicket ticket) {
        if (ticket == null) {
            throw new IllegalArgumentException("Ticket cannot be null");
        }

        String vehicleReg = ticket.getVehicle().getRegistrationNumber();
        
        // Check if vehicle already has an active ticket
        if (ticketsByVehicleRegistration.containsKey(vehicleReg)) {
            System.out.println("⚠️  " + String.format("Vehicle %s already has an active ticket", vehicleReg));
            throw new IllegalStateException("Vehicle already has an active parking ticket");
        }

        ticketsByTicketId.put(ticket.getTicketId(), ticket);
        ticketsByVehicleRegistration.put(vehicleReg, ticket);
        
        System.out.println(String.format("Parking ticket issued - ID: %s, Vehicle: %s", 
            ticket.getTicketId(), vehicleReg));
        
        return ticket;
    }

    /**
     * Get ticket by ticket ID
     */
    public Optional<ParkingTicket> getTicketById(String ticketId) {
        return Optional.ofNullable(ticketsByTicketId.get(ticketId));
    }

    /**
     * Get active ticket by vehicle registration number
     */
    public Optional<ParkingTicket> getActiveTicketByVehicle(String registrationNumber) {
        return Optional.ofNullable(ticketsByVehicleRegistration.get(registrationNumber));
    }

    /**
     * Get active ticket by vehicle object
     */
    public Optional<ParkingTicket> getActiveTicketByVehicle(Vehicle vehicle) {
        if (vehicle == null) {
            return Optional.empty();
        }
        return getActiveTicketByVehicle(vehicle.getRegistrationNumber());
    }

    /**
     * Complete a ticket (after payment and exit)
     */
    public void completeTicket(String ticketId) {
        ParkingTicket ticket = ticketsByTicketId.get(ticketId);
        if (ticket == null) {
            System.out.println("⚠️  " + String.format("Ticket %s not found", ticketId));
            return;
        }

        String vehicleReg = ticket.getVehicle().getRegistrationNumber();
        ticketsByVehicleRegistration.remove(vehicleReg);
        completedTicketIds.add(ticketId);
        
        System.out.println(String.format("Ticket completed - ID: %s, Vehicle: %s, Duration: %d minutes", 
            ticketId, vehicleReg, ticket.getParkingDurationInMinutes()));
    }

    /**
     * Check if a ticket is completed
     */
    public boolean isTicketCompleted(String ticketId) {
        return completedTicketIds.contains(ticketId);
    }

    /**
     * Get number of active tickets
     */
    public int getActiveTicketsCount() {
        return ticketsByVehicleRegistration.size();
    }

    /**
     * Get number of completed tickets
     */
    public int getCompletedTicketsCount() {
        return completedTicketIds.size();
    }

    /**
     * Get all active tickets
     */
    public Collection<ParkingTicket> getAllActiveTickets() {
        return Collections.unmodifiableCollection(ticketsByVehicleRegistration.values());
    }

    /**
     * Check if vehicle has active ticket
     */
    public boolean hasActiveTicket(String registrationNumber) {
        return ticketsByVehicleRegistration.containsKey(registrationNumber);
    }

    /**
     * Display all active tickets
     */
    public void displayActiveTickets() {
        if (ticketsByVehicleRegistration.isEmpty()) {
            System.out.println("No active tickets");
            return;
        }

        System.out.println("\n=== Active Parking Tickets ===");
        ticketsByVehicleRegistration.values().forEach(ticket -> 
            System.out.println(String.format("Ticket: %s | Vehicle: %s | Spot: %s | Entry: %s | Duration: %d min",
                ticket.getTicketId(),
                ticket.getVehicle().getRegistrationNumber(),
                ticket.getParkingSpot().getSpotId(),
                ticket.getEntryTime(),
                ticket.getParkingDurationInMinutes()
            ))
        );
    }

    /**
     * Get ticket statistics
     */
    public String getStatistics() {
        return String.format("""
            Ticket Statistics:
            - Active Tickets: %d
            - Completed Tickets: %d
            - Total Tickets Issued: %d
            """,
            getActiveTicketsCount(),
            getCompletedTicketsCount(),
            ticketsByTicketId.size()
        );
    }
}

