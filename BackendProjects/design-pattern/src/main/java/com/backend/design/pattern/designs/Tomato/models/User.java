package com.backend.design.pattern.designs.Tomato.models;

public class User {

    private final int userId;
    private String _userName;
    private String address;
    private final Cart cart;

    public User(int userId, String userName, String address) {
        this.userId = userId;
        this._userName = userName;
        this.address = address;
        this.cart = new Cart();
    }

    public String getUserName() {
        return _userName;
    }

    public void setUserName(String n) {
        _userName = n;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String a) {
        address = a;
    }

    public Cart getCart() {
        return cart;
    }
}
