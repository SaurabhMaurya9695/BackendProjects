package com.backend.design.pattern.designs.Tomato.models;

public class MenuItem {

    private String _code;
    private String _menuName;
    private int price;

    public MenuItem(String code, String name, int price) {
        this._code = code;
        this._menuName = name;
        this.price = price;
    }

    public String getCode() {
        return _code;
    }

    public void setCode(String c) {
        _code = c;
    }

    public String getMenuName() {
        return _menuName;
    }

    public void setMenuName(String n) {
        _menuName = n;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int p) {
        price = p;
    }
}

