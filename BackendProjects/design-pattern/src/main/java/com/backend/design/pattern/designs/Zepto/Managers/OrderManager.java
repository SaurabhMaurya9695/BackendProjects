package com.backend.design.pattern.designs.Zepto.Managers;

import com.backend.design.pattern.common.Utility;
import com.backend.design.pattern.designs.Zepto.Factory.ProductFactory;
import com.backend.design.pattern.designs.Zepto.Models.DeliveryPartner;
import com.backend.design.pattern.designs.Zepto.Models.User;
import com.backend.design.pattern.designs.Zepto.Models.ZProduct;
import com.backend.design.pattern.designs.Zepto.Others.Cart;
import com.backend.design.pattern.designs.Zepto.Others.Order;
import com.backend.design.pattern.designs.Zepto.Store.DarkStore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderManager {

    private List<Order> _orderList = new ArrayList<>();
    public static OrderManager _instance = null;

    public static OrderManager getInstance() {
        if (_instance == null) {
            _instance = new OrderManager();
        }
        return _instance;
    }

    public List<Order> getAllOrders() {
        return _orderList != null ? _orderList : new ArrayList<>();
    }

    public void placeOrder(User user, Cart cart) {
        System.out.println("\n======================[ Order Placement Started ]======================");
        System.out.println(
                "[Customer] " + user.getUserName() + " (Location: " + user.getX() + ", " + user.getY() + ")");
        System.out.println("[Cart] Requested Items:");
        for (Utility.Pair<ZProduct, Integer> item : cart.getItems()) {
            System.out.println(
                    "   → SKU " + item.left.getSku() + " (" + item.left.getProductName() + ") x" + item.right);
        }

        // Step 1: Find nearby dark stores
        double maxDist = 5.0;
        List<DarkStore> nearbyDarkStores = DarkStoreManager.getInstance().getNearbyDarkStores(user.getX(), user.getY(),
                maxDist);

        if (nearbyDarkStores.isEmpty()) {
            System.out.println(
                    "[OrderManager] ❌ No dark stores found within " + maxDist + " KM. Order cannot be placed.");
            System.out.println("======================================================================\n");
            return;
        } else {
            System.out.println(
                    "[OrderManager] ✅ Found " + nearbyDarkStores.size() + " nearby store(s) within " + maxDist
                            + " KM.");
        }

        // Step 2: Check if first store has all items
        DarkStore firstStore = nearbyDarkStores.get(0);
        boolean allInFirst = true;
        for (Utility.Pair<ZProduct, Integer> item : cart.getItems()) {
            if (firstStore.checkStock(item.left.getSku()) < item.right) {
                allInFirst = false;
                break;
            }
        }

        Order order = new Order(user);

        // Case 1: One store can fulfill all items
        if (allInFirst) {
            System.out.println("[OrderManager] ✅ All items available at: " + firstStore.getStoreName());

            for (Utility.Pair<ZProduct, Integer> item : cart.getItems()) {
                firstStore.removeStock(item.left.getSku(), item.right);
                order.itemsList.add(new Utility.Pair<>(item.left, item.right));
            }

            order.totalAmount = cart.getTotal();
            order._deliveryPartnerList.add(new DeliveryPartner("Partner1"));
            System.out.println("[Delivery] Assigned Delivery Partner → Partner1");
        }

        // Case 2: Need to split across multiple stores
        else {
            System.out.println("[OrderManager] ⚠️ Splitting order across multiple stores...");

            Map<Integer, Integer> allItems = new HashMap<>();
            for (Utility.Pair<ZProduct, Integer> item : cart.getItems()) {
                allItems.put(item.left.getSku(), item.right);
            }

            int partnerId = 1;
            for (DarkStore store : nearbyDarkStores) {
                if (allItems.isEmpty()) {
                    break;
                }

                System.out.println("   🔍 Checking store: " + store.getStoreName());
                List<Integer> toErase = new ArrayList<>();

                for (Map.Entry<Integer, Integer> entry : allItems.entrySet()) {
                    int sku = entry.getKey();
                    int qtyNeeded = entry.getValue();
                    int availableQty = store.checkStock(sku);

                    if (availableQty <= 0) {
                        continue;
                    }

                    int takenQty = Math.min(availableQty, qtyNeeded);
                    store.removeStock(sku, takenQty);

                    System.out.println("      ✔ " + store.getStoreName() + " supplies SKU " + sku + " x" + takenQty);
                    order.itemsList.add(new Utility.Pair<>(ProductFactory.createProduct(sku), takenQty));

                    if (qtyNeeded > takenQty) {
                        allItems.put(sku, qtyNeeded - takenQty);
                    } else {
                        toErase.add(sku);
                    }
                }

                for (int sku : toErase) {
                    allItems.remove(sku);
                }

                if (!toErase.isEmpty()) {
                    String pname = "Partner" + partnerId++;
                    order._deliveryPartnerList.add(new DeliveryPartner(pname));
                    System.out.println(
                            "      🚴 Assigned Delivery Partner → " + pname + " (from " + store.getStoreName() + ")");
                }
            }

            if (!allItems.isEmpty()) {
                System.out.println("[OrderManager] ❌ Could not fulfill the following items:");
                for (Map.Entry<Integer, Integer> entry : allItems.entrySet()) {
                    System.out.println("      SKU " + entry.getKey() + " x" + entry.getValue());
                }
            }

            double sum = 0;
            for (Utility.Pair<ZProduct, Integer> it : order.itemsList) {
                sum += it.left.getPrice() * it.right;
            }
            order.totalAmount = sum;
        }

        // Final Order Summary
        System.out.println("\n======================[ Order Summary ]======================");
        System.out.println("Order ID: " + order.orderId);
        System.out.println("Customer: " + user.getUserName());
        System.out.println("Items:");
        for (Utility.Pair<ZProduct, Integer> item : order.itemsList) {
            System.out.println(
                    "   → " + item.left.getProductName() + " (SKU " + item.left.getSku() + ") x" + item.right + " @ ₹"
                            + item.left.getPrice());
        }
        System.out.println("Total Amount: ₹" + order.totalAmount);
        System.out.println("Assigned Delivery Partners:");
        for (DeliveryPartner dp : order._deliveryPartnerList) {
            System.out.println("   → " + dp.getDeliveryPartnerName());
        }
        System.out.println("=============================================================\n");

        _orderList.add(order);
    }
}
