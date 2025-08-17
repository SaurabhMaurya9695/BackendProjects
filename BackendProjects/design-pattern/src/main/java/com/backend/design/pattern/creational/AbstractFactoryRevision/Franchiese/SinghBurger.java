package com.backend.design.pattern.creational.AbstractFactoryRevision.Franchiese;

import com.backend.design.pattern.creational.AbstractFactoryRevision.BurgerStategies.BasicBurger;
import com.backend.design.pattern.creational.AbstractFactoryRevision.BurgerStategies.BasicWheatBurger;
import com.backend.design.pattern.creational.AbstractFactoryRevision.BurgerStategies.Burger;
import com.backend.design.pattern.creational.AbstractFactoryRevision.Factory.MealFactory;
import com.backend.design.pattern.creational.AbstractFactoryRevision.GarlicBreadStategies.BasicGarlicBread;
import com.backend.design.pattern.creational.AbstractFactoryRevision.GarlicBreadStategies.CheeseGarlicBread;
import com.backend.design.pattern.creational.AbstractFactoryRevision.GarlicBreadStategies.GarlicBread;

import java.util.Objects;

// Singh Burger and King Burger are two types of franchise
public class SinghBurger implements MealFactory {

    @Override
    public Burger prepareBurgers(String type) {
        if (Objects.equals(type, "Basic")) {
            return new BasicBurger();
        } else if (Objects.equals(type, "Wheat")) {
            return new BasicWheatBurger();
        } else {
            return null;
        }
    }

    @Override
    public GarlicBread prepareGarlicBread(String type) {
        if (Objects.equals(type, "Basic")) {
            return new BasicGarlicBread();
        } else if (Objects.equals(type, "Cheese")) {
            return new CheeseGarlicBread();
        } else {
            return null;
        }
    }
}
