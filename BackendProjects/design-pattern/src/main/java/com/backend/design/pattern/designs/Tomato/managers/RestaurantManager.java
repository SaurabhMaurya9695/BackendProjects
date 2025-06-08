package com.backend.design.pattern.designs.Tomato.managers;

import com.backend.design.pattern.designs.Tomato.models.Restaurant;

import java.util.ArrayList;
import java.util.List;

// Singleton
public class RestaurantManager {

    private final List<Restaurant> _restaurants = new ArrayList<>();
    private static RestaurantManager instance = null;

    private RestaurantManager() {
        // private constructor
    }

    public static RestaurantManager getInstance() {
        if (instance == null) {
            instance = new RestaurantManager();
        }
        return instance;
    }

    // add your custom logic to add in this
    public void addRestaurant(Restaurant restaurant) {
        _restaurants.add(restaurant);
    }

    public List<Restaurant> searchByLocation(String loc) {
        List<Restaurant> result = new ArrayList<>();
        loc = loc.toLowerCase();
        for (Restaurant restaurant : _restaurants) {
            String rl = restaurant.getLocation().toLowerCase();
            if (rl.equals(loc)) {
                result.add(restaurant);
            }
        }
        return result;
    }
}
