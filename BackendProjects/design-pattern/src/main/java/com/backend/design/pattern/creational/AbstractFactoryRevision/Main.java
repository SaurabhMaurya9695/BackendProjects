package com.backend.design.pattern.creational.AbstractFactoryRevision;

import com.backend.design.pattern.creational.AbstractFactoryRevision.BurgerStategies.Burger;
import com.backend.design.pattern.creational.AbstractFactoryRevision.Franchiese.KingBurgers;
import com.backend.design.pattern.creational.AbstractFactoryRevision.Franchiese.SinghBurger;
import com.backend.design.pattern.creational.AbstractFactoryRevision.GarlicBreadStategies.GarlicBread;

public class Main {

    public static void main(String[] args) {
        KingBurgers kingBurgers = new KingBurgers();
        Burger basic = kingBurgers.prepareBurgers("Basic");
        basic.prepareBurger();

        SinghBurger singhBurger = new SinghBurger();
        GarlicBread garlicBread = singhBurger.prepareGarlicBread("Basic");
        garlicBread.prepareGarlicBread();
    }
}
