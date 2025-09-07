package com.backend.design.pattern.designs.Zepto.Models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import com.backend.design.pattern.designs.Zepto.Others.Cart;

/**
 * Represents a user in the Zepto system.
 */
@Setter
@Getter
@ToString
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class User {

    /**
     * Name of the user.
     */
    private String userName;

    /**
     * X-coordinate of the user's location.
     */
    private double x;

    /**
     * Y-coordinate of the user's location.
     */
    private double y;

    /**
     * Shopping cart associated with the user.
     */
    private Cart cart;

    /**
     * Creates a new user with name and location (without a cart).
     *
     * @param userName the user's name
     * @param x        X-coordinate of the user
     * @param y        Y-coordinate of the user
     */
    public User(String userName, double x, double y) {
        this.userName = userName;
        this.x = x;
        this.y = y;
        cart = new Cart();
    }
}
