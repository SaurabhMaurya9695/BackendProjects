package com.backend.design.pattern.structural.adaptorPattern;

import com.backend.design.pattern.structural.adaptorPattern.Item1.RestaurantItems;

import java.util.ArrayList;
import java.util.List;

public class SwiggyStore {

    List<RestaurantItems> items = new ArrayList<>();

    // currently it is delivering the RestaurantItems.
    public void addItems(RestaurantItems item) {
        items.add(item);
    }

    public void printItems() {
        System.out.println(items.size());
    }
}