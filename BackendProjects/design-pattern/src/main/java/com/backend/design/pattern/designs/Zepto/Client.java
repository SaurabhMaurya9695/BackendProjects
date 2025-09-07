package com.backend.design.pattern.designs.Zepto;

import com.backend.design.pattern.designs.Zepto.Managers.OrderManager;
import com.backend.design.pattern.designs.Zepto.Models.User;
import com.backend.design.pattern.designs.Zepto.Others.Cart;

/**
 * Demo client for Zepto platform.
 * Simulates user interaction: browsing products, adding to cart, and placing an order.
 */
public class Client {

    public static void main(String[] args) {
        try {
            System.out.println("\n[Client] Initializing Zepto Platform...");
            ZeptoHelper.initialize();

            // User arrives on platform
            User user = new User("Saurabh", 1.0, 1.0);
            System.out.println("\n[Client] User \"" + user.getUserName() + "\" has entered the platform at location ("
                    + user.getX() + ", " + user.getY() + ")");

            // Show all available items
            System.out.println("\n[Client] Displaying available products for user...");
            ZeptoHelper.showAllItems(user);

            // User adds items to cart
            System.out.println("\n[Client] Adding items to cart for user \"" + user.getUserName() + "\"...");
            Cart cart = user.getCart();
            cart.addItems(101, 4);
            cart.addItems(102, 3);
            cart.addItems(103, 2);

            // Place the order
            System.out.println("\n[Client] Placing order for user \"" + user.getUserName() + "\"...");
            OrderManager.getInstance().placeOrder(user, cart);

            System.out.println("\n[Client] Demo Complete ✅");
        } catch (Exception e) {
            System.out.println("\n[Client] ERROR: An exception occurred while running the demo.");
            e.printStackTrace();
        }
    }
}
