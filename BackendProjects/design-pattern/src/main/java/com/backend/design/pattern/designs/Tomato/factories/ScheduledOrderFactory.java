package com.backend.design.pattern.designs.Tomato.factories;

import com.backend.design.pattern.designs.Tomato.models.Cart;
import com.backend.design.pattern.designs.Tomato.models.DeliveryOrder;
import com.backend.design.pattern.designs.Tomato.models.MenuItem;
import com.backend.design.pattern.designs.Tomato.models.Order;
import com.backend.design.pattern.designs.Tomato.models.PickupOrder;
import com.backend.design.pattern.designs.Tomato.models.Restaurant;
import com.backend.design.pattern.designs.Tomato.models.User;
import com.backend.design.pattern.designs.Tomato.strategies.PaymentStrategy;

import java.util.List;

public class ScheduledOrderFactory implements OrderFactory {

    private final String scheduleTime;

    public ScheduledOrderFactory(String scheduleTime) {
        this.scheduleTime = scheduleTime;
    }

    @Override
    public Order createOrder(User user, Cart cart, Restaurant restaurant, List<MenuItem> menuItems,
            PaymentStrategy paymentStrategy, double totalCost, String orderType) {
        Order order = null;

        if (orderType.equals("Delivery")) {
            DeliveryOrder deliveryOrder = new DeliveryOrder();
            deliveryOrder.setUserAddress(user.getAddress());
            order = deliveryOrder;
        } else {
            PickupOrder pickupOrder = new PickupOrder();
            pickupOrder.setRestaurantAddress(restaurant.getLocation());
            order = pickupOrder;
        }

        order.setUser(user);
        order.setRestaurant(restaurant);
        order.setMenuItems(menuItems);
        order.setPaymentStrategy(paymentStrategy);
        order.setScheduled(scheduleTime);
        order.setTotal(totalCost);
        return order;
    }
}
