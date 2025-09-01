package com.backend.design.pattern.designs.DiscountCouponSystem.Models;

import com.backend.xjc.CartItem;
import com.backend.xjc.Product;

import java.util.ArrayList;
import java.util.List;

public class Cart {

    // cart can have list of multiple cartItems
    private List<CartItem> _cartItemList;
    private double _originalTotal;
    private double _currentTotal;
    private boolean _isLoyalMember;
    private String _paymentBank;

    public Cart() {
        _cartItemList = new ArrayList<>();
        _originalTotal = 0.0;
        _currentTotal = 0.0;
        _paymentBank = null;
    }

    public void addProductToCart(Product product, int quantity) {
        if (_cartItemList != null) {
            CartItem cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);

            _cartItemList.add(cartItem);
            _originalTotal += getCartPrice(cartItem);
            _currentTotal += getCartPrice(cartItem);
        }
    }

    private double getCartPrice(CartItem cartItem) {
        return cartItem.getQuantity() * cartItem.getProduct().getProductPrice();
    }

    public void applyDiscount(double discount) {
        _currentTotal -= discount;
        if (_currentTotal < 0) {
            _currentTotal = 0;
        }
    }

    public List<CartItem> getCartItemList() {
        return _cartItemList;
    }

    public void setCartItemList(List<CartItem> cartItemList) {
        _cartItemList = cartItemList;
    }

    public double getOriginalTotal() {
        return _originalTotal;
    }

    public void setOriginalTotal(double originalTotal) {
        _originalTotal = originalTotal;
    }

    public double getCurrentTotal() {
        return _currentTotal;
    }

    public void setCurrentTotal(double currentTotal) {
        _currentTotal = currentTotal;
    }

    public boolean isLoyalMember() {
        return _isLoyalMember;
    }

    public void setLoyalMember(boolean loyalMember) {
        _isLoyalMember = loyalMember;
    }

    public String getPaymentBank() {
        return _paymentBank;
    }

    public void setPaymentBank(String paymentBank) {
        _paymentBank = paymentBank;
    }
}
