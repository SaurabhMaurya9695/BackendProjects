package com.backend.design.pattern.designs.Tomato.models;

import com.backend.design.pattern.designs.Tomato.strategies.PaymentStrategy;

import java.util.List;

public abstract class Order {

    private static int _nextOrderId = 0;

    protected int _orderId;
    protected User _user;
    protected Restaurant _restaurant;
    protected List<MenuItem> _menuItems;
    protected PaymentStrategy _paymentStrategy;
    protected double _total;
    protected String _scheduled;

    public Order() {
        this._user = null;
        this._restaurant = null;
        this._paymentStrategy = null;
        this._total = 0.0;
        this._scheduled = "";
        this._orderId = ++_nextOrderId;
    }

    public boolean processPayment() {
        if (_paymentStrategy != null) {
            _paymentStrategy.pay(_total);
            return true;
        } else {
            System.out.println("Please choose a payment mode first");
            return false;
        }
    }

    // this is a abstract method then it has to provide the body at runtime
    public abstract String getType();

    // Getters and Setters
    public int getOrderId() {
        return _orderId;
    }

    public void setUser(User u) {
        _user = u;
    }

    public User getUser() {
        return _user;
    }

    public void setRestaurant(Restaurant r) {
        _restaurant = r;
    }

    public Restaurant getRestaurant() {
        return _restaurant;
    }

    public void setMenuItems(List<MenuItem> its) {
        _menuItems = its;
        _total = 0;
        for (MenuItem i : _menuItems) {
            _total += i.getPrice();
        }
    }

    public List<MenuItem> getMenuItems() {
        return _menuItems;
    }

    public void setPaymentStrategy(PaymentStrategy p) {
        _paymentStrategy = p;
    }

    public void setScheduled(String s) {
        _scheduled = s;
    }

    public String getScheduled() {
        return _scheduled;
    }

    public double getTotal() {
        return _total;
    }

    public void setTotal(double total) {
        this._total = total;
    }
}

