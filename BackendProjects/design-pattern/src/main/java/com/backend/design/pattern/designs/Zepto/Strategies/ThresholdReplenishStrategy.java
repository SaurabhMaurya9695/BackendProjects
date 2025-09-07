package com.backend.design.pattern.designs.Zepto.Strategies;

import com.backend.design.pattern.designs.Zepto.Managers.InventoryManager;

import java.util.Map;

/**
 * Replenishment strategy that adds stock only if
 * current stock falls below a defined threshold.
 */
public class ThresholdReplenishStrategy implements IReplenishStrategy {

    private final int threshold;

    /**
     * Creates a new threshold-based replenishment strategy.
     *
     * @param threshold minimum stock level before replenishment is triggered
     */
    public ThresholdReplenishStrategy(int threshold) {
        this.threshold = threshold;
    }

    @Override
    public void replenish(InventoryManager manager, Map<Integer, Integer> itemToReplenish) {
        System.out.println("[ThresholdReplenishStrategy] Checking stock against threshold...");

        itemToReplenish.forEach((sku, quantityToAdd) -> {
            int currentStock = manager.checkStock(sku);

            if (currentStock < threshold) {
                manager.addStock(sku, quantityToAdd);
                System.out.println(
                        "[ThresholdReplenishStrategy] SKU " + sku + " had stock " + currentStock + " → replenished by "
                                + quantityToAdd);
            }
        });
    }
}
