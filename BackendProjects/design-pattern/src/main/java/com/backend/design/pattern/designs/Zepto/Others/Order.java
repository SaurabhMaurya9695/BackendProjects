package com.backend.design.pattern.designs.Zepto.Others;

import com.backend.design.pattern.common.Utility;
import com.backend.design.pattern.designs.Zepto.Models.DeliveryPartner;
import com.backend.design.pattern.designs.Zepto.Models.User;
import com.backend.design.pattern.designs.Zepto.Models.ZProduct;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a customer order containing user details,
 * ordered products, assigned delivery partners, and total cost.
 */
public class Order {

    /**
     * Tracks the next order ID to assign.
     */
    public static int nextId;

    /**
     * Unique ID of the order.
     */
    public int orderId;

    /**
     * The customer who placed the order.
     */
    public User _user;

    /**
     * List of ordered products with their quantities.
     */
    public List<Utility.Pair<ZProduct, Integer>> itemsList;

    /**
     * List of delivery partners assigned to the order.
     */
    public List<DeliveryPartner> _deliveryPartnerList;

    /**
     * Total cost of the order.
     */
    public double totalAmount;

    /**
     * Creates a new order for the given user.
     *
     * @param user the customer placing the order
     */
    public Order(User user) {
        orderId = nextId++;
        _user = user;
        totalAmount = 0.0;
        itemsList = new ArrayList<>();
        _deliveryPartnerList = new ArrayList<>();
    }
}
