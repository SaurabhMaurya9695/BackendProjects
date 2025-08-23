package com.backend.design.pattern.structural.decoratorPatternRevised.concreateDecorator;

import com.backend.design.pattern.structural.decoratorPatternRevised.ICharacter;
import com.backend.design.pattern.structural.decoratorPatternRevised.decorator.CharacterDecorator;

// STEP 4 : Concrete Decorator Class for Mario
public class HeightPowerDecorator extends CharacterDecorator {

    public HeightPowerDecorator(ICharacter iCharacter) {
        super(iCharacter);
    }

    @Override
    public String getAbility() {
        return super.getAbility() + "With Height Power";
    }
}
