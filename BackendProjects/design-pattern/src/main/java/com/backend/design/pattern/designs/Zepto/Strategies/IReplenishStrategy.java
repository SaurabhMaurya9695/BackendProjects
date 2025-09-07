package com.backend.design.pattern.designs.Zepto.Strategies;

import com.backend.design.pattern.designs.Zepto.Managers.InventoryManager;

import java.util.Map;

/**
 * Strategy interface for replenishing inventory in a store.
 */
public interface IReplenishStrategy {

    /**
     * Replenishes items in the inventory using a given strategy.
     *
     * @param manager         the inventory manager to update
     * @param itemToReplenish map of SKU → quantity to replenish
     */
    void replenish(InventoryManager manager, Map<Integer, Integer> itemToReplenish);
}
