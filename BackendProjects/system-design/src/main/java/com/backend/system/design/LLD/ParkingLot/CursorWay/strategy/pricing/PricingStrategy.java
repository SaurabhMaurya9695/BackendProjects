package com.backend.system.design.LLD.ParkingLot.CursorWay.strategy.pricing;

import com.backend.system.design.LLD.ParkingLot.CursorWay.models.ParkingTicket;

/**
 * Strategy Pattern for Pricing
 * Interface Segregation Principle - Small, focused interface
 * Open/Closed Principle - Open for extension (new pricing strategies) but closed for modification
 */
public interface PricingStrategy {

    /**
     * Calculate parking fee based on the ticket information
     *
     * @param ticket The parking ticket containing vehicle and time information
     * @return The calculated fee amount
     */
    double calculateFee(ParkingTicket ticket);

    /**
     * Get the name of this pricing strategy
     *
     * @return Strategy name
     */
    String getStrategyName();
}

