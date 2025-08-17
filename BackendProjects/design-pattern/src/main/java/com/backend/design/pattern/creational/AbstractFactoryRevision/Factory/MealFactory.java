package com.backend.design.pattern.creational.AbstractFactoryRevision.Factory;

import com.backend.design.pattern.creational.AbstractFactoryRevision.BurgerStategies.Burger;
import com.backend.design.pattern.creational.AbstractFactoryRevision.GarlicBreadStategies.GarlicBread;

// meal factory can create two types of factories
public interface MealFactory {

    Burger prepareBurgers(String type);

    GarlicBread prepareGarlicBread(String type);
}
