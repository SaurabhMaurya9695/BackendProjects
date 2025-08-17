package com.backend.design.pattern.creational.AbstractFactoryRevision.BurgerStategies;

public class BasicWheatBurger implements Burger {

    @Override
    public void prepareBurger() {
        System.out.println("Preparing Basic Wheat Burger");
    }
}
