package com.backend.design.pattern.designs.Tinder.Models;

public class Interest {

    public String name;
    public String category;

    public Interest() {
        name = "";
        category = "";
    }

    public Interest(String name, String category) {
        this.name = name;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
