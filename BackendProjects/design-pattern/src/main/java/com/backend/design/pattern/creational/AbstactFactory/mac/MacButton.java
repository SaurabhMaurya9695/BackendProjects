package com.backend.design.pattern.creational.AbstactFactory.mac;

import com.backend.design.pattern.creational.AbstactFactory.commonUtility.Button;

public class MacButton implements Button {

    @Override
    public void buttonPainted() {
        System.out.println("MacButton painted with black Color");
    }
}
