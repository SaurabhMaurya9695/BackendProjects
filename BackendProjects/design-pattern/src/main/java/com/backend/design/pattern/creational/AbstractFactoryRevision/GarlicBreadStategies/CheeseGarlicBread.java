package com.backend.design.pattern.creational.AbstractFactoryRevision.GarlicBreadStategies;

public class CheeseGarlicBread implements GarlicBread {

    @Override
    public void prepareGarlicBread() {
        System.out.println("Preparing Basic Cheese Garlic Bread");
    }
}
