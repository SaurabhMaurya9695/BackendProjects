package com.backend.design.pattern.structural.decoratorPatternRevised;

import com.backend.design.pattern.structural.decoratorPatternRevised.concreateDecorator.GunPowerDecorator;
import com.backend.design.pattern.structural.decoratorPatternRevised.concreateDecorator.HeightPowerDecorator;

public class Main {

    public static void main(String[] args) {
        ICharacter mario = new MarioCharacter();
        mario.getAbility();

        // now I want to provide some more power to mario
        ICharacter heightUpMario = new HeightPowerDecorator(mario);
        System.out.println("After Height Up -> " + heightUpMario.getAbility());

        // HeightPower with GunPower
        ICharacter gunPowerMario = new GunPowerDecorator(mario);
        System.out.println("After Gun Power -> " + gunPowerMario.getAbility());
    }
}
