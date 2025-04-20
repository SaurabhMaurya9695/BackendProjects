package com.backend.design.pattern.decoratorPattern.toppings;

import com.backend.design.pattern.decoratorPattern.Pizza;
import com.backend.design.pattern.decoratorPattern.decorater.PizzaDecorator;

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
