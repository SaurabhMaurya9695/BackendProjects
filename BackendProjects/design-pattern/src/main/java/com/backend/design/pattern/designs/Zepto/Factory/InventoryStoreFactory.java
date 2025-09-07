package com.backend.design.pattern.designs.Zepto.Factory;

import com.backend.design.pattern.designs.Zepto.Enum.InventoryType;
import com.backend.design.pattern.designs.Zepto.Inventory.DBInventoryStore;
import com.backend.design.pattern.designs.Zepto.Managers.InventoryManager;

/**
 * Factory class to create {@link InventoryManager} instances
 * based on the provided {@link InventoryType}.
 */
public class InventoryStoreFactory {

    /**
     * Returns an {@link InventoryManager} implementation depending on the given {@link InventoryType}.
     *
     * @param type type of inventory (e.g., DB_INVENTORY)
     * @return an {@link InventoryManager} instance, or {@code null} if no matching type is found
     */
    public static InventoryManager getInventoryStore(InventoryType type) {
        InventoryManager inventoryManager = null;
        switch (type) {
            case DB_INVENTORY -> inventoryManager = new InventoryManager(new DBInventoryStore());
            default -> {
                // No default implementation for other types yet
            }
        }
        return inventoryManager;
    }
}
