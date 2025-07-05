package com.backend.design.pattern.structural.decoratorPattern.decorater;

import com.backend.design.pattern.structural.decoratorPattern.Pizza;

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