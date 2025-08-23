package com.backend.design.pattern.structural.decoratorPatternRevised.concreateDecorator;

import com.backend.design.pattern.structural.decoratorPatternRevised.ICharacter;
import com.backend.design.pattern.structural.decoratorPatternRevised.decorator.CharacterDecorator;

// STEP 4 : Concrete Decorator Class for Mario
public class GunPowerDecorator extends CharacterDecorator {

    public GunPowerDecorator(ICharacter iCharacter) {
        super(iCharacter);
    }

    @Override
    public String getAbility() {
        return super.getAbility() + "With Gun Power Ability";
    }
}
