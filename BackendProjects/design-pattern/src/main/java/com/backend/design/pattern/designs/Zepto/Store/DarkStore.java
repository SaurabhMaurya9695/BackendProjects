package com.backend.design.pattern.designs.Zepto.Store;

import com.backend.design.pattern.designs.Zepto.Enum.InventoryType;
import com.backend.design.pattern.designs.Zepto.Factory.InventoryStoreFactory;
import com.backend.design.pattern.designs.Zepto.Managers.InventoryManager;
import com.backend.design.pattern.designs.Zepto.Models.ZProduct;
import com.backend.design.pattern.designs.Zepto.Strategies.IReplenishStrategy;

import java.util.List;
import java.util.Map;

/**
 * Represents a Dark Store location responsible for maintaining inventory,
 * replenishment strategies, and stock management.
 */
public class DarkStore {

    /**
     * Store name.
     */
    private String _storeName;

    /**
     * Store X-coordinate (for location).
     */
    private double x;

    /**
     * Store Y-coordinate (for location).
     */
    private double y;

    /**
     * Inventory manager for handling stock.
     */
    private final InventoryManager _inventoryManager;

    /**
     * Strategy for replenishing stock.
     */
    private IReplenishStrategy _replenishStrategy;

    /**
     * Creates a new DarkStore with the given name and coordinates.
     *
     * @param storeName name of the store
     * @param x_cor     X-coordinate
     * @param y_cor     Y-coordinate
     */
    public DarkStore(String storeName, double x_cor, double y_cor) {
        x = x_cor;
        y = y_cor;
        _storeName = storeName;
        _inventoryManager = InventoryStoreFactory.getInventoryStore(InventoryType.DB_INVENTORY);
    }

    /**
     * Calculates Euclidean distance from user coordinates.
     *
     * @param ux user X-coordinate
     * @param uy user Y-coordinate
     * @return distance to user
     */
    public double distanceTo(double ux, double uy) {
        return Math.sqrt((x - ux) * (x - ux) + (y - uy) * (y - uy));
    }

    /**
     * Runs replenishment using the configured strategy.
     *
     * @param itemsToReplenish map of SKU → quantity to replenish
     */
    public void runReplenish(Map<Integer, Integer> itemsToReplenish) {
        if (_replenishStrategy != null) {
            _replenishStrategy.replenish(_inventoryManager, itemsToReplenish);
        }
    }

    /**
     * Retrieves all available products in the store.
     *
     * @return list of products
     */
    public List<ZProduct> getAllProducts() {
        return _inventoryManager.getAvailableProduct();
    }

    /**
     * Checks stock availability for a given SKU.
     *
     * @param sku product SKU
     * @return available quantity
     */
    public int checkStock(int sku) {
        return _inventoryManager.checkStock(sku);
    }

    /**
     * Removes stock of a product.
     *
     * @param sku      product SKU
     * @param quantity quantity to remove
     */
    public void removeStock(int sku, int quantity) {
        _inventoryManager.removeStock(sku, quantity);
    }

    /**
     * Adds stock for a product.
     *
     * @param sku product SKU
     * @param qty quantity to add
     */
    public void addStock(int sku, int qty) {
        _inventoryManager.addStock(sku, qty);
    }

    /**
     * Sets replenishment strategy for the store.
     *
     * @param strategy replenishment strategy
     */
    public void setReplenishStrategy(IReplenishStrategy strategy) {
        _replenishStrategy = strategy;
    }

    /**
     * Gets the store name.
     *
     * @return store name
     */
    public String getStoreName() {
        return this._storeName;
    }
}
