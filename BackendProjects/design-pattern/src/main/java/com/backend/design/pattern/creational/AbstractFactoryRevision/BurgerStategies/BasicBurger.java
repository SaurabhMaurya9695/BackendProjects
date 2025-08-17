package com.backend.design.pattern.creational.AbstractFactoryRevision.BurgerStategies;

public class BasicBurger implements Burger {

    @Override
    public void prepareBurger() {
        System.out.println("Preparing A Basic Burger");
    }
}
