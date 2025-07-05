package com.backend.design.pattern.structural.decoratorPattern.toppings;

import com.backend.design.pattern.structural.decoratorPattern.Pizza;
import com.backend.design.pattern.structural.decoratorPattern.decorater.PizzaDecorator;

public class CheeseBurstTopping extends PizzaDecorator {

    public CheeseBurstTopping(Pizza pizza) {
        super(pizza);
    }

    public String bake() {
        return pizza.bake() + addCheese();
    }

    public String addCheese() {
        return "Cheese toppings added + ";
    }
}
