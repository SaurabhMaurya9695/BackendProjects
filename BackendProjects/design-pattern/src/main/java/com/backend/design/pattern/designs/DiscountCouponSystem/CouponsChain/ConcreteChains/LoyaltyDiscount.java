package com.backend.design.pattern.designs.DiscountCouponSystem.CouponsChain.ConcreteChains;

import com.backend.design.pattern.designs.DiscountCouponSystem.CouponsChain.Coupon;
import com.backend.design.pattern.designs.DiscountCouponSystem.Manager.DiscountStrategyManager;
import com.backend.design.pattern.designs.DiscountCouponSystem.Models.Cart;
import com.backend.design.pattern.designs.DiscountCouponSystem.Strategies.DiscountStrategy;
import com.backend.xjc.DiscountStrategyType;

public class LoyaltyDiscount extends Coupon {

    private final double _percentage;
    private final DiscountStrategy _strategy;

    public LoyaltyDiscount(double percentage) {
        _percentage = percentage;
        _strategy = DiscountStrategyManager.getInstance().getStrategy(DiscountStrategyType.PERCENTAGE, percentage, 0);
    }

    @Override
    public boolean isApplicable(Cart cart) {
        return cart.isLoyalMember();
    }

    @Override
    public double getDiscount(Cart cart) {
        return _strategy.calculatePrice(cart.getCurrentTotal());
    }

    @Override
    public String name(Cart cart) {
        return "Loyalty Discount " + _percentage + " %off ";
    }
}
