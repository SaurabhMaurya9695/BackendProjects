package com.backend.design.pattern.decoratorPattern.decorater;

import com.backend.design.pattern.decoratorPattern.Pizza;

//Step 3
public abstract class PizzaDecorator implements Pizza {

    protected Pizza pizza;

    public PizzaDecorator(Pizza pizza) {
        this.pizza = pizza;
    }

    public String bake() {
        return pizza.bake();
    }
}