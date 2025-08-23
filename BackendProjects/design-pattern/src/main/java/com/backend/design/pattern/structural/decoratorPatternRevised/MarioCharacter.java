package com.backend.design.pattern.structural.decoratorPatternRevised;

//Step 2 : created a common concrete class for ICharacter
public class MarioCharacter implements ICharacter {

    @Override
    public String getAbility() {
        String x = "Hello From Mario ";
        System.out.println(x);
        return x;
    }
}
