package com.backend.design.pattern.structural.decoratorPattern.toppings;

import com.backend.design.pattern.structural.decoratorPattern.Pizza;
import com.backend.design.pattern.structural.decoratorPattern.decorater.PizzaDecorator;

public class JalepanoToppings extends PizzaDecorator {

    public JalepanoToppings(Pizza pizza) {
        super(pizza);
    }

    public String bake() {
        return pizza.bake() + addJalepano();
    }

    public String addJalepano() {
        return "Jalepeno toppings added + ";
    }
}