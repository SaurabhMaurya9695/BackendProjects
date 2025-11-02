package com.backend.system.design.LLD.ParkingLot.CursorWay.managers;

import com.backend.system.design.LLD.ParkingLot.CursorWay.enums.SpotSize;
import com.backend.system.design.LLD.ParkingLot.CursorWay.models.ParkingSpot;
import com.backend.system.design.LLD.ParkingLot.CursorWay.models.Vehicle;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


/**
 * ParkingSpotManager - Manages all parking spots
 * Following Single Responsibility Principle - Only handles parking spot operations
 * Thread-safe implementation using ConcurrentHashMap
 */
public class ParkingSpotManager {

    // Using ConcurrentHashMap for thread-safety
    private final Map<String, ParkingSpot> allSpots;
    private final Map<SpotSize, List<ParkingSpot>> spotsBySize;

    public ParkingSpotManager() {
        this.allSpots = new ConcurrentHashMap<>();
        this.spotsBySize = new ConcurrentHashMap<>();
        initializeSpotsBySize();
        
        System.out.println("ParkingSpotManager initialized");
    }

    private void initializeSpotsBySize() {
        for (SpotSize size : SpotSize.values()) {
            spotsBySize.put(size, Collections.synchronizedList(new ArrayList<>()));
        }
    }

    /**
     * Add a parking spot to the system
     */
    public void addParkingSpot(ParkingSpot spot) {
        if (spot == null) {
            throw new IllegalArgumentException("Parking spot cannot be null");
        }

        if (allSpots.containsKey(spot.getSpotId())) {
            System.out.println("⚠️  " + String.format("Spot %s already exists", spot.getSpotId()));
            return;
        }

        allSpots.put(spot.getSpotId(), spot);
        spotsBySize.get(spot.getSpotSize()).add(spot);
        
        System.out.println(String.format("Added parking spot - ID: %s, Size: %s, Floor: %d", 
            spot.getSpotId(), spot.getSpotSize().getDescription(), spot.getFloor()));
    }

    /**
     * Find an available spot for the given vehicle
     * Strategy: Find the smallest available spot that can accommodate the vehicle
     */
    public Optional<ParkingSpot> findAvailableSpot(Vehicle vehicle) {
        if (vehicle == null) {
            System.out.println("⚠️  " + "Cannot find spot for null vehicle");
            return Optional.empty();
        }

        SpotSize requiredSize = vehicle.getRequiredSpotSize();
        System.out.println(String.format("Searching for available spot for vehicle %s (required size: %s)", 
            vehicle.getRegistrationNumber(), requiredSize.getDescription()));

        // Try exact size match first
        Optional<ParkingSpot> spot = findAvailableSpotBySize(requiredSize, vehicle);
        if (spot.isPresent()) {
            return spot;
        }

        // If no exact match, try larger spots
        for (SpotSize size : SpotSize.values()) {
            if (size.getSizeUnits() > requiredSize.getSizeUnits()) {
                spot = findAvailableSpotBySize(size, vehicle);
                if (spot.isPresent()) {
                    System.out.println(String.format("Assigned larger spot (size: %s) for vehicle %s", 
                        size.getDescription(), vehicle.getRegistrationNumber()));
                    return spot;
                }
            }
        }

        System.out.println("⚠️  " + String.format("No available spot found for vehicle %s", 
            vehicle.getRegistrationNumber()));
        return Optional.empty();
    }

    private Optional<ParkingSpot> findAvailableSpotBySize(SpotSize size, Vehicle vehicle) {
        List<ParkingSpot> spots = spotsBySize.get(size);
        if (spots == null) {
            return Optional.empty();
        }

        synchronized (spots) {
            return spots.stream()
                .filter(spot -> spot.canAccommodate(vehicle))
                .findFirst();
        }
    }

    /**
     * Get spot by ID
     */
    public Optional<ParkingSpot> getSpotById(String spotId) {
        return Optional.ofNullable(allSpots.get(spotId));
    }

    /**
     * Get total number of spots
     */
    public int getTotalSpots() {
        return allSpots.size();
    }

    /**
     * Get number of available spots
     */
    public int getAvailableSpots() {
        return (int) allSpots.values().stream()
            .filter(ParkingSpot::isAvailable)
            .count();
    }

    /**
     * Get available spots by size
     */
    public int getAvailableSpotsBySize(SpotSize size) {
        List<ParkingSpot> spots = spotsBySize.get(size);
        if (spots == null) {
            return 0;
        }

        synchronized (spots) {
            return (int) spots.stream()
                .filter(ParkingSpot::isAvailable)
                .count();
        }
    }

    /**
     * Get parking lot status summary
     */
    public String getStatusSummary() {
        StringBuilder summary = new StringBuilder();
        summary.append("\n╔════════════════════════════════════════════════════════╗\n");
        summary.append("║           PARKING LOT STATUS SUMMARY                   ║\n");
        summary.append("╠════════════════════════════════════════════════════════╣\n");
        summary.append(String.format("║ Total Spots    : %-36d ║\n", getTotalSpots()));
        summary.append(String.format("║ Available      : %-36d ║\n", getAvailableSpots()));
        summary.append(String.format("║ Occupied       : %-36d ║\n", getTotalSpots() - getAvailableSpots()));
        summary.append("╠════════════════════════════════════════════════════════╣\n");
        
        for (SpotSize size : SpotSize.values()) {
            int available = getAvailableSpotsBySize(size);
            int total = spotsBySize.get(size).size();
            summary.append(String.format("║ %-13s: %2d / %-2d available                  ║\n", 
                size.name(), available, total));
        }
        
        summary.append("╚════════════════════════════════════════════════════════╝\n");
        return summary.toString();
    }

    /**
     * Get all spots
     */
    public Collection<ParkingSpot> getAllSpots() {
        return Collections.unmodifiableCollection(allSpots.values());
    }

    /**
     * Display detailed spot information
     */
    public void displayAllSpots() {
        System.out.println("\n=== All Parking Spots ===");
        allSpots.values().forEach(spot -> 
            System.out.println(String.format("Spot: %s | Size: %s | Floor: %d | Status: %s | Vehicle: %s",
                spot.getSpotId(),
                spot.getSpotSize().name(),
                spot.getFloor(),
                spot.getStatus().getDisplayName(),
                spot.getParkedVehicle() != null ? spot.getParkedVehicle().getRegistrationNumber() : "None"
            ))
        );
    }
}

