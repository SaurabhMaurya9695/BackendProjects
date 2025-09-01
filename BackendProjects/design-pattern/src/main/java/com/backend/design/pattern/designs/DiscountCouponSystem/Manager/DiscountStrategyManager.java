package com.backend.design.pattern.designs.DiscountCouponSystem.Manager;

import com.backend.design.pattern.designs.DiscountCouponSystem.Strategies.ConcreteStrategies.FlatDiscountStrategy;
import com.backend.design.pattern.designs.DiscountCouponSystem.Strategies.ConcreteStrategies.PercentageDiscountStrategy;
import com.backend.design.pattern.designs.DiscountCouponSystem.Strategies.ConcreteStrategies.PercentageWithCapStrategy;
import com.backend.design.pattern.designs.DiscountCouponSystem.Strategies.DiscountStrategy;
import com.backend.xjc.DiscountStrategyType;

public class DiscountStrategyManager {

    private static DiscountStrategyManager _instance;

    private DiscountStrategyManager() {

    }

    public static DiscountStrategyManager getInstance() {
        if (_instance == null) {
            _instance = new DiscountStrategyManager();
        }
        return _instance;
    }

    public DiscountStrategy getStrategy(DiscountStrategyType strategyType, double param1, double param2) {
        switch (strategyType) {
            case FLAT -> {
                return new FlatDiscountStrategy(param2);
            }
            case PERCENTAGE -> {
                return new PercentageDiscountStrategy(param1);
            }
            case PERCENTAGE_WITH_CAP -> {
                return new PercentageWithCapStrategy(param1, param2);
            }
            default -> throw new RuntimeException("UNSUPPORTED STRATEGY");
        }
    }
}
