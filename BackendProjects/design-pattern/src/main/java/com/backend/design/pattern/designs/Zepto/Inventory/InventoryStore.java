package com.backend.design.pattern.designs.Zepto.Inventory;

import com.backend.design.pattern.designs.Zepto.Models.ZProduct;

import java.util.List;

/**
 * Defines the contract for an inventory store in the Zepto system.
 * <p>
 * Implementations manage products and their stock levels.
 */
public interface InventoryStore {

    /**
     * Adds a product to the inventory with the given quantity.
     *
     * @param product the product to add
     * @param q       the quantity to add
     */
    void addProduct(ZProduct product, int q);

    /**
     * Removes the specified quantity of a product from the inventory.
     *
     * @param sku the SKU of the product
     * @param q   the quantity to remove
     */
    void removeProduct(int sku, int q);

    /**
     * Checks the available stock for a given SKU.
     *
     * @param sku the SKU of the product
     * @return the quantity available in stock
     */
    int checkStock(int sku);

    /**
     * Lists all products currently in the inventory.
     *
     * @return list of available products
     */
    List<ZProduct> listAllProducts();
}
