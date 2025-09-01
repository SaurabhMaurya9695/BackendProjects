package com.backend.design.pattern.designs.DiscountCouponSystem.Strategies.ConcreteStrategies;

import com.backend.design.pattern.designs.DiscountCouponSystem.Strategies.DiscountStrategy;

public class FlatDiscountStrategy implements DiscountStrategy {

    private final double _flatDiscount;

    public FlatDiscountStrategy(double amount) {
        _flatDiscount = amount;
    }

    @Override
    public double calculatePrice(double amount) {
        // if a user sends price 100, and we have an offer on flat discount of 20, it will return
        // 20 instead of 100
        return Math.min(amount, _flatDiscount);
    }
}
