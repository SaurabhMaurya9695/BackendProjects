package com.backend.design.pattern.designs.Tomato.factories;

import com.backend.design.pattern.designs.Tomato.models.Cart;
import com.backend.design.pattern.designs.Tomato.models.MenuItem;
import com.backend.design.pattern.designs.Tomato.models.Order;
import com.backend.design.pattern.designs.Tomato.models.Restaurant;
import com.backend.design.pattern.designs.Tomato.models.User;
import com.backend.design.pattern.designs.Tomato.strategies.PaymentStrategy;

import java.util.List;

public interface OrderFactory {

    Order createOrder(User user, Cart cart, Restaurant restaurant, List<MenuItem> menuItems,
            PaymentStrategy paymentStrategy, double totalCost, String orderType);
}