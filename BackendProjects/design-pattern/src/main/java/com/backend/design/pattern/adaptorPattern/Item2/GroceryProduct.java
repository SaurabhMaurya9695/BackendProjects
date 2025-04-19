package com.backend.design.pattern.adaptorPattern.Item2;

public class GroceryProduct implements GroceryItems {

    @Override
    public String getItemName() {
        return "GroceryProduct Name";
    }

    @Override
    public String getPrice() {
        return "GroceryProduct is 200";
    }

    @Override
    public String getGroceryName() {
        return "Saurabh's shop";
    }
}
