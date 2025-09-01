package com.backend.design.pattern.designs.DiscountCouponSystem.CouponsChain.ConcreteChains;

import com.backend.design.pattern.designs.DiscountCouponSystem.CouponsChain.Coupon;
import com.backend.design.pattern.designs.DiscountCouponSystem.Manager.DiscountStrategyManager;
import com.backend.design.pattern.designs.DiscountCouponSystem.Models.Cart;
import com.backend.design.pattern.designs.DiscountCouponSystem.Strategies.DiscountStrategy;
import com.backend.xjc.CartItem;
import com.backend.xjc.DiscountStrategyType;

public class SessionalDiscount extends Coupon {

    private double _percent;
    private String _category;
    private final DiscountStrategy _strategy;

    public SessionalDiscount(double percent, String category) {
        _percent = percent;
        _category = category;
        _strategy = DiscountStrategyManager.getInstance().getStrategy(DiscountStrategyType.PERCENTAGE, _percent, 0);
    }

    @Override
    public boolean isApplicable(Cart cart) {
        for (CartItem cartItem : cart.getCartItemList()) {
            if (cartItem.getProduct().getProductCategory().equals(_category)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public double getDiscount(Cart cart) {
        double subtotal = 0.0;
        for (CartItem cartItem : cart.getCartItemList()) {
            if (cartItem.getProduct().getProductCategory().equals(_category)) {
                subtotal += (cartItem.getProduct().getProductPrice() * cartItem.getQuantity());
            }
        }
        return _strategy.calculatePrice(subtotal);
    }

    @Override
    public boolean isCombinable() {
        return super.isCombinable();
    }

    @Override
    public String name(Cart cart) {
        return "Sessional Offer " + _percent + " %off " + _category;
    }
}
