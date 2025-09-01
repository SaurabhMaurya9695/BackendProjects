package com.backend.design.pattern.designs.DiscountCouponSystem.CouponsChain.ConcreteChains;

import com.backend.design.pattern.designs.DiscountCouponSystem.CouponsChain.Coupon;
import com.backend.design.pattern.designs.DiscountCouponSystem.Manager.DiscountStrategyManager;
import com.backend.design.pattern.designs.DiscountCouponSystem.Models.Cart;
import com.backend.design.pattern.designs.DiscountCouponSystem.Strategies.DiscountStrategy;
import com.backend.xjc.DiscountStrategyType;

public class BankingDiscount extends Coupon {

    private final String _bank;
    private final double _minSpend;
    private final double _percentage;
    private final double _offCap;
    private final DiscountStrategy _strategy;

    public BankingDiscount(String bank, double minSpend, double percentage, double offCap) {
        _bank = bank;
        _minSpend = minSpend;
        _percentage = percentage;
        _offCap = offCap;
        _strategy = DiscountStrategyManager.getInstance().getStrategy(DiscountStrategyType.PERCENTAGE_WITH_CAP,
                _minSpend, _offCap);
    }

    @Override
    public boolean isApplicable(Cart cart) {
        return cart.getPaymentBank().equals(_bank) && cart.getOriginalTotal() >= _minSpend;
    }

    @Override
    public double getDiscount(Cart cart) {
        return _strategy.calculatePrice(cart.getCurrentTotal());
    }

    @Override
    public String name(Cart cart) {
        return _bank + " Bank RS " + _percentage + " off upto " + _offCap;
    }
}
