package com.backend.design.pattern.designs.DiscountCouponSystem.Strategies.ConcreteStrategies;

import com.backend.design.pattern.designs.DiscountCouponSystem.Strategies.DiscountStrategy;

public class PercentageDiscountStrategy implements DiscountStrategy {

    private final double _percentage;

    public PercentageDiscountStrategy(double percentage) {
        _percentage = percentage;
    }

    @Override
    public double calculatePrice(double amount) {
        return amount * (_percentage / 100.0);
    }
}
