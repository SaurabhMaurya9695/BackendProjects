package com.backend.design.pattern.designs.Tomato;

import com.backend.design.pattern.designs.Tomato.models.Order;
import com.backend.design.pattern.designs.Tomato.models.Restaurant;
import com.backend.design.pattern.designs.Tomato.models.User;
import com.backend.design.pattern.designs.Tomato.strategies.UpiPaymentStrategy;

import java.util.List;

public class Main {

    public static final String LOCATION = "lucknow";

    public static void main(String[] args) {
        // Simulating a happy flow
        // Create TomatoApp Object
        TomatoApp tomato = new TomatoApp();

        // Simulate a user coming in (Happy Flow)
        User user = new User(101, "Saurabh", LOCATION);
        System.out.println("User: " + user.getUserName() + " is active.");

        // User searches for restaurants by location
        List<Restaurant> restaurantList = tomato.searchRestaurants(LOCATION);

        if (restaurantList.isEmpty()) {
            System.out.println("No restaurants found!");
            return;
        }

        System.out.println("Found Restaurants:");
        for (Restaurant restaurant : restaurantList) {
            System.out.println(" - " + restaurant.getName());
        }

        tomato.selectRestaurant(user, restaurantList.get(0));
        System.out.println("Selected restaurant: " + restaurantList.get(0).getName());

        // User adds items to the cart
        tomato.addToCart(user, "P1");
        tomato.addToCart(user, "P2");

        tomato.printUserCart(user);

        // User checkout the cart
        Order order = tomato.checkoutNow(user, "Delivery", new UpiPaymentStrategy("1234567890"));

        // User pays for the cart. If payment is successful, notification is sent.
        tomato.payForOrder(user, order);
    }
}
