package com.backend.design.pattern.designs.DiscountCouponSystem.CouponsChain.ConcreteChains;

import com.backend.design.pattern.designs.DiscountCouponSystem.CouponsChain.Coupon;
import com.backend.design.pattern.designs.DiscountCouponSystem.Manager.DiscountStrategyManager;
import com.backend.design.pattern.designs.DiscountCouponSystem.Models.Cart;
import com.backend.design.pattern.designs.DiscountCouponSystem.Strategies.DiscountStrategy;
import com.backend.xjc.DiscountStrategyType;

public class BulkPurchaseDiscount extends Coupon {

    private final double _threshold;
    private final double _flatOff;
    private final DiscountStrategy _strategy;

    public BulkPurchaseDiscount(double threshold, double flatOff) {
        _threshold = threshold;
        _flatOff = flatOff;
        _strategy = DiscountStrategyManager.getInstance().getStrategy(DiscountStrategyType.FLAT, threshold, flatOff);
    }

    @Override
    public boolean isApplicable(Cart cart) {
        return cart.getCurrentTotal() >= _threshold;
    }

    @Override
    public double getDiscount(Cart cart) {
        return _strategy.calculatePrice(cart.getCurrentTotal());
    }

    @Override
    public String name(Cart cart) {
        return "Bulk Purchase RS " + _flatOff + " off over " + _threshold;
    }
}
