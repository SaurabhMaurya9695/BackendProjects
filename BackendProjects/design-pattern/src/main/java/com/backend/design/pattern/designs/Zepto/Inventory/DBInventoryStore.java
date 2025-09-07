package com.backend.design.pattern.designs.Zepto.Inventory;

import com.backend.design.pattern.designs.Zepto.Models.ZProduct;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Database-backed implementation of {@link InventoryStore}.
 * <p>
 * Manages products and their stock levels using in-memory maps
 * for SKU-to-quantity and SKU-to-product mappings.
 */
public class DBInventoryStore implements InventoryStore {

    /**
     * Maps SKU to available quantity.
     */
    private final Map<Integer, Integer> stockMap;

    /**
     * Maps SKU to product details.
     */
    private final Map<Integer, ZProduct> productMap;

    /**
     * Creates a new, empty inventory store.
     */
    public DBInventoryStore() {
        stockMap = new HashMap<>();
        productMap = new HashMap<>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addProduct(ZProduct product, int quantity) {
        int sku = product.getSku();
        productMap.putIfAbsent(sku, product);
        stockMap.put(sku, stockMap.getOrDefault(sku, 0) + quantity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeProduct(int sku, int q) {
        Integer currentQuantity = stockMap.get(sku);
        if (currentQuantity == null) {
            System.out.println("[DBInventoryStore] SKU " + sku + " does not exist in stock.");
            return;
        }

        int remainingQuantity = currentQuantity - q;
        if (remainingQuantity <= 0) {
            stockMap.remove(sku);
            System.out.println("[DBInventoryStore] SKU " + sku + " removed from stock.");
        } else {
            stockMap.put(sku, remainingQuantity);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int checkStock(int sku) {
        return stockMap.getOrDefault(sku, 0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ZProduct> listAllProducts() {
        if (stockMap.isEmpty()) {
            System.out.println("[DBInventoryStore] No products in stock.");
            return new ArrayList<>();
        }

        List<ZProduct> availableProducts = new ArrayList<>();
        stockMap.forEach((sku, quantity) -> {
            ZProduct product = productMap.get(sku);
            if (product != null) {
                availableProducts.add(product);
            }
        });
        return availableProducts;
    }
}
