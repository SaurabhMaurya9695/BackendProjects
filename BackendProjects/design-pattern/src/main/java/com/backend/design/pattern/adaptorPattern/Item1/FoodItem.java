package com.backend.design.pattern.adaptorPattern.Item1;

public class FoodItem implements RestaurantItems {

    @Override
    public String getItemName() {
        return "Food Item ";
    }

    @Override
    public String getPrice() {
        return "Food Price is 100";
    }

    @Override
    public String getRestaurantName() {
        return "Saurabh's kichen";
    }
}
