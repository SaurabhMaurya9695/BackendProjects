package com.backend.design.pattern.designs.Zepto.Others;

import com.backend.design.pattern.common.Utility;
import com.backend.design.pattern.designs.Zepto.Factory.ProductFactory;
import com.backend.design.pattern.designs.Zepto.Models.ZProduct;

import java.util.ArrayList;
import java.util.List;

public class Cart {

    private final List<Utility.Pair<ZProduct, Integer>> _items;

    public Cart() {
        _items = new ArrayList<>();
        System.out.println("\n🛒 [Cart] New cart created.");
    }

    /**
     * Add an item to the cart.
     *
     * @param sku Product SKU
     * @param qty Quantity
     */
    public void addItems(int sku, int qty) {
        ZProduct product = ProductFactory.createProduct(sku);

        if (product == null) {
            System.out.println("❌ [Cart] Invalid SKU: " + sku + ". Item not added.");
            return;
        }

        _items.add(new Utility.Pair<>(product, qty));
        System.out.println("✔ [Cart] Added → " + product.getProductName() + " (SKU: " + sku + ") x" + qty + " @ ₹"
                + product.getPrice());
    }

    /**
     * Get the total price of items in the cart.
     *
     * @return total price
     */
    public double getTotal() {
        double sum = 0.0;
        for (Utility.Pair<ZProduct, Integer> item : _items) {
            sum += item.left.getPrice() * item.right;
        }
        System.out.println("💰 [Cart] Current Total = ₹" + sum);
        return sum;
    }

    /**
     * Get all items in the cart.
     *
     * @return list of items (empty if none)
     */
    public List<Utility.Pair<ZProduct, Integer>> getItems() {
        if (_items.isEmpty()) {
            System.out.println("⚠️ [Cart] No items in the cart.");
            return new ArrayList<>();
        }
        System.out.println("📦 [Cart] Fetching " + _items.size() + " item(s) from cart...");
        return _items;
    }
}
