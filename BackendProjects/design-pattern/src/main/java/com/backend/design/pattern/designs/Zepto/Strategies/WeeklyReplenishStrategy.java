package com.backend.design.pattern.designs.Zepto.Strategies;

import com.backend.design.pattern.designs.Zepto.Managers.InventoryManager;

import java.util.Map;

/**
 * Replenishment strategy that triggers weekly.
 * Currently acts as a placeholder for scheduled stock updates.
 */
public class WeeklyReplenishStrategy implements IReplenishStrategy {

    /**
     * Creates a new weekly replenishment strategy.
     */
    public WeeklyReplenishStrategy() {

    }

    @Override
    public void replenish(InventoryManager manager, Map<Integer, Integer> itemToReplenish) {
        System.out.println("[WeeklyReplenishStrategy] Weekly replenishment triggered...");
        // Future: Add logic for time-based replenishment
    }
}
