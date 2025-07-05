package com.backend.design.pattern.creational.AbstactFactory.factory;

import com.backend.design.pattern.creational.AbstactFactory.ApplicationFactory;
import com.backend.design.pattern.creational.AbstactFactory.commonUtility.Button;
import com.backend.design.pattern.creational.AbstactFactory.commonUtility.Checkbox;
import com.backend.design.pattern.creational.AbstactFactory.mac.MacButton;
import com.backend.design.pattern.creational.AbstactFactory.mac.MacCheckbox;

public class MacFactory implements ApplicationFactory {

    @Override
    public Button createButton() {
        return new MacButton();
    }

    @Override
    public Checkbox createCheckBox() {
        return new MacCheckbox();
    }
}
