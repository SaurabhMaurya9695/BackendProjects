package com.backend.design.pattern.designs.DiscountCouponSystem.Manager;

import com.backend.design.pattern.designs.DiscountCouponSystem.CouponsChain.Coupon;
import com.backend.design.pattern.designs.DiscountCouponSystem.Models.Cart;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CouponManager {

    private static CouponManager _instance;
    private Coupon _head;
    private final Lock _lock = new ReentrantLock();

    private CouponManager() {
        this._head = null;
    }

    public static synchronized CouponManager getInstance() {
        if (_instance == null) {
            _instance = new CouponManager();
        }
        return _instance;
    }

    public void registerCoupon(Coupon coupon) {
        _lock.lock();
        try {
            if (_head == null) {
                _head = coupon;
            } else {
                Coupon cur = _head;
                while (cur.getNext() != null) {
                    cur = cur.getNext();
                }
                cur.setNext(coupon);
            }
        } finally {
            _lock.unlock();
        }
    }

    public List<String> getApplicable(Cart cart) {
        _lock.lock();
        try {
            List<String> res = new ArrayList<>();
            Coupon cur = _head;
            while (cur != null) {
                if (cur.isApplicable(cart)) {
                    res.add(cur.name(cart));
                }
                cur = cur.getNext();
            }
            return res;
        } finally {
            _lock.unlock();
        }
    }

    public double applyAll(Cart cart) {
        _lock.lock();
        try {
            if (_head != null) {
                _head.applyDiscount(cart);
            }
            return cart.getOriginalTotal();
        } finally {
            _lock.unlock();
        }
    }
}