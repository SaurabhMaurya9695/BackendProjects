package com.backend.design.pattern.designs.Tomato.services;

import com.backend.design.pattern.designs.Tomato.models.MenuItem;
import com.backend.design.pattern.designs.Tomato.models.Order;

import java.util.List;

public class NotificationService {

    public static void notify(Order order) {
        System.out.println("\nPublisher: New " + order.getType() + " order placed!");
        System.out.println("---------------------------------------------");
        System.out.println("Order ID: " + order.getOrderId());
        System.out.println("Customer: " + order.getUser().getUserName());
        System.out.println("Restaurant: " + order.getRestaurant().getName());
        System.out.println("Items Ordered:");

        List<MenuItem> items = order.getMenuItems();
        for (MenuItem item : items) {
            System.out.println("   - " + item.getMenuName() + " (₹" + item.getPrice() + ")");
        }

        System.out.println("Total: ₹" + order.getTotal());
        System.out.println("Scheduled For: " + order.getScheduled());
        System.out.println("Payment: Done");
        System.out.println("---------------------------------------------");
    }
}

