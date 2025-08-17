package com.backend.design.pattern.creational.AbstractFactoryRevision.GarlicBreadStategies;

public class BasicGarlicBread implements GarlicBread{

    @Override
    public void prepareGarlicBread() {
        System.out.println("Preparing Basic Garlic Bread");
    }
}
