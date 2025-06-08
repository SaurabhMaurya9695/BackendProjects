package com.backend.design.pattern.designs.Tomato.models;

import java.util.ArrayList;
import java.util.List;

public class Cart {

    private Restaurant _restaurant;
    private final List<MenuItem> _menuItems = new ArrayList<>();

    public Cart() {
        _restaurant = null;
    }

    public void addItem(MenuItem item) {
        if (_restaurant == null) {
            System.err.println("Cart: Set a restaurant before adding items.");
            return;
        }
        _menuItems.add(item);
    }

    public double getTotalCost() {
        double sum = 0;
        for (MenuItem it : _menuItems) {
            sum += it.getPrice();
        }
        return sum;
    }

    public boolean isEmpty() {
        return _restaurant == null || _menuItems.isEmpty();
    }

    public void clear() {
        _menuItems.clear();
        _restaurant = null;
    }

    public void setRestaurant(Restaurant r) {
        _restaurant = r;
    }

    public Restaurant getRestaurant() {
        return _restaurant;
    }

    public List<MenuItem> getMenuItems() {
        return _menuItems;
    }
}

