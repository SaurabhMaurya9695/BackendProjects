package com.backend.design.pattern.structural.decoratorPattern;

//Step 2
public class BasePizza implements Pizza {

    @Override
    public String bake() {
        // NOW create the toppings which you want to add to top of it
        return "Base Pizza + ";
    }
}
