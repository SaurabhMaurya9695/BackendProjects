package com.backend.design.pattern.structural.adaptorPattern.Adaptor;

import com.backend.design.pattern.structural.adaptorPattern.Item1.RestaurantItems;
import com.backend.design.pattern.structural.adaptorPattern.Item2.GroceryItems;

/* This class is for catch the grocery item and convert it to restaurant item */
public class GroceryItemAdaptor implements RestaurantItems {

    private final GroceryItems _item;

    public GroceryItemAdaptor(GroceryItems foodItem) {
        _item = foodItem;
    }

    @Override
    public String getItemName() {
        // catching the grocery item and converting it to food item
        return _item.getItemName();
    }

    @Override
    public String getPrice() {
        return _item.getItemName();
    }

    @Override
    public String getRestaurantName() {
        // catching the grocery name and converting it to restaurant name
        return _item.getGroceryName();
    }
}
