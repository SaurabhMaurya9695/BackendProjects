package com.backend.design.pattern.designs.DiscountCouponSystem.Strategies.ConcreteStrategies;

import com.backend.design.pattern.designs.DiscountCouponSystem.Strategies.DiscountStrategy;

public class PercentageWithCapStrategy implements DiscountStrategy {

    private final double _percentage;
    private final double _cap;

    public PercentageWithCapStrategy(double percentage, double cap) {
        _percentage = percentage;
        _cap = cap;
    }

    @Override
    public double calculatePrice(double baseAmount) {
        double disc = (_percentage * 100.0) * baseAmount;
        if (disc > _cap) {
            return disc;
        }
        return disc;
    }
}
