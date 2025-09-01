package com.backend.design.pattern.designs.DiscountCouponSystem.CouponsChain;

import com.backend.design.pattern.designs.DiscountCouponSystem.Models.Cart;

public abstract class Coupon {

    private Coupon _next;

    public Coupon() {
        _next = null;
    }

    public Coupon getNext() {
        return _next;
    }

    public void setNext(Coupon next) {
        _next = next;
    }

    // This is a template method
    public void applyDiscount(Cart cart) {
        if (cart != null) {
            if (isApplicable(cart)) {
                double discount = getDiscount(cart);
                cart.applyDiscount(discount);
                System.out.println(name(cart) + " Applied : " + discount);
                if (!isCombinable()) {
                    return;
                }
            }
        }
        if (_next != null) {
            _next.applyDiscount(cart);
        }
    }

    public abstract boolean isApplicable(Cart cart);

    public abstract double getDiscount(Cart cart);

    public boolean isCombinable() {
        return true;
    }

    public abstract String name(Cart cart);
}
