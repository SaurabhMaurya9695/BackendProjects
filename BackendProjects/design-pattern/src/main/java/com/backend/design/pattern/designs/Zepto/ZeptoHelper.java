package com.backend.design.pattern.designs.Zepto;

import com.backend.design.pattern.designs.Zepto.Managers.DarkStoreManager;
import com.backend.design.pattern.designs.Zepto.Models.User;
import com.backend.design.pattern.designs.Zepto.Models.ZProduct;
import com.backend.design.pattern.designs.Zepto.Store.DarkStore;
import com.backend.design.pattern.designs.Zepto.Strategies.ThresholdReplenishStrategy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility class for Zepto application.
 * Provides helper methods for initializing dark stores
 * and displaying available products for users.
 */
public class ZeptoHelper {

    /**
     * Displays all available products within 5 KM of the user.
     *
     * @param user the customer to check nearby products for
     */
    public static void showAllItems(User user) {
        System.out.println("\n[Zepto] Available products within 5 KM for " + user.getUserName() + ":");

        DarkStoreManager dsManager = DarkStoreManager.getInstance();
        List<DarkStore> nearbyStores = dsManager.getNearbyDarkStores(user.getX(), user.getY(), 5.0);

        Map<Integer, Double> skuToPrice = new HashMap<>();
        Map<Integer, String> skuToName = new HashMap<>();

        for (DarkStore store : nearbyStores) {
            for (ZProduct product : store.getAllProducts()) {
                int sku = product.getSku();
                // Only keep the first occurrence of a SKU
                skuToPrice.putIfAbsent(sku, product.getPrice());
                skuToName.putIfAbsent(sku, product.getProductName());
            }
        }

        if (skuToPrice.isEmpty()) {
            System.out.println("  No products available nearby.");
            return;
        }

        skuToPrice.forEach(
                (sku, price) -> System.out.println("  SKU " + sku + " - " + skuToName.get(sku) + " @ ₹" + price));
    }

    /**
     * Initializes dark stores with sample stock and replenishment strategies.
     */
    public static void initialize() {
        DarkStoreManager dsManager = DarkStoreManager.getInstance();

        // Create and setup DarkStore A
        DarkStore darkStoreA = new DarkStore("DarkStoreA", 0.0, 0.0);
        darkStoreA.setReplenishStrategy(new ThresholdReplenishStrategy(3));
        System.out.println("\n[Zepto] Adding stocks to DarkStoreA...");
        darkStoreA.addStock(101, 5);
        darkStoreA.addStock(102, 2);

        // Create and setup DarkStore B
        DarkStore darkStoreB = new DarkStore("DarkStoreB", 4.0, 1.0);
        darkStoreB.setReplenishStrategy(new ThresholdReplenishStrategy(3));
        System.out.println("\n[Zepto] Adding stocks to DarkStoreB...");
        darkStoreB.addStock(101, 3);
        darkStoreB.addStock(103, 10);

        // Create and setup DarkStore C
        DarkStore darkStoreC = new DarkStore("DarkStoreC", 2.0, 3.0);
        darkStoreC.setReplenishStrategy(new ThresholdReplenishStrategy(3));
        System.out.println("\n[Zepto] Adding stocks to DarkStoreC...");
        darkStoreC.addStock(102, 5);
        darkStoreC.addStock(201, 7);

        // Register dark stores
        dsManager.registerDarkStore(darkStoreA);
        dsManager.registerDarkStore(darkStoreB);
        dsManager.registerDarkStore(darkStoreC);

        System.out.println("\n[Zepto] Dark stores initialized successfully.");
    }
}
