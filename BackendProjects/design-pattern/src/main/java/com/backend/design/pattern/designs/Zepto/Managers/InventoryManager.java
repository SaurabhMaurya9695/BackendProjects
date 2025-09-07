package com.backend.design.pattern.designs.Zepto.Managers;

import com.backend.design.pattern.designs.Zepto.Factory.ProductFactory;
import com.backend.design.pattern.designs.Zepto.Inventory.InventoryStore;
import com.backend.design.pattern.designs.Zepto.Models.ZProduct;

import java.util.List;

/**
 * Manages inventory operations such as adding, removing, and checking stock.
 * Acts as a wrapper around {@link InventoryStore}.
 */
public class InventoryManager {

    private final InventoryStore _inventoryStore;

    /**
     * Creates an InventoryManager with the provided store implementation.
     *
     * @param store the inventory store backend
     */
    public InventoryManager(InventoryStore store) {
        _inventoryStore = store;
    }

    /**
     * Adds stock of a product by SKU.
     *
     * @param sku      product SKU
     * @param quantity quantity to add
     */
    public void addStock(int sku, int quantity) {
        ZProduct product = ProductFactory.createProduct(sku);
        _inventoryStore.addProduct(product, quantity);
        System.out.println("[InventoryManager] Product Added with SKU " + sku);
    }

    /**
     * Removes stock of a product by SKU.
     *
     * @param sku      product SKU
     * @param quantity quantity to remove
     */
    public void removeStock(int sku, int quantity) {
        _inventoryStore.removeProduct(sku, quantity);
        System.out.println("[InventoryManager] Remove Product with SKU " + sku);
    }

    /**
     * Checks the stock availability of a product.
     *
     * @param sku product SKU
     * @return available quantity in stock
     */
    public int checkStock(int sku) {
        int stock = _inventoryStore.checkStock(sku);
        System.out.println("[InventoryManager] Product " + (stock > 0 ? "Present" : "Not Present") + " in Stock");
        return stock;
    }

    /**
     * Retrieves all available products.
     *
     * @return list of available products
     */
    public List<ZProduct> getAvailableProduct() {
        return _inventoryStore.listAllProducts();
    }
}
