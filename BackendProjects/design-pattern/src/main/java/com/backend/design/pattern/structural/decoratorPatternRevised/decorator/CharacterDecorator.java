package com.backend.design.pattern.structural.decoratorPatternRevised.decorator;

import com.backend.design.pattern.structural.decoratorPatternRevised.ICharacter;

// STEP 3 : CharacterDecorator 'has-a' and 'is-a' Character
public class CharacterDecorator implements ICharacter {

    ICharacter _iCharacter;

    public CharacterDecorator(ICharacter iCharacter) {
        _iCharacter = iCharacter;
    }

    @Override
    public String getAbility() {
        String ability = "Decorating Mario with ";
        return ability;
    }
}
