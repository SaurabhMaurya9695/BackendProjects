package com.backend.design.pattern.designs.Tomato.managers;

import com.backend.design.pattern.designs.Tomato.models.Order;

import java.util.ArrayList;
import java.util.List;

public class OrderManager {

    private final List<Order> _orders = new ArrayList<>();
    private static OrderManager _instance = null;

    private OrderManager() {
        // Private Constructor
    }

    public static OrderManager get_instance() {
        if (_instance == null) {
            _instance = new OrderManager();
        }
        return _instance;
    }

    public void addOrder(Order order) {
        _orders.add(order);
    }

    public void listOrders() {
        System.out.println("\n--- All Orders ---");
        for (Order order : _orders) {
            System.out.println(
                    order.getType() + " order for " + order.getUser().getUserName() + " | Total: ₹" + order.getTotal()
                            + " | At: " + order.getScheduled());
        }
    }
}

