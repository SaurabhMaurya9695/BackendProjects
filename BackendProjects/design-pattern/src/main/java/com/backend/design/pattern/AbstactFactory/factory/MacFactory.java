package com.backend.design.pattern.AbstactFactory.factory;

import com.backend.design.pattern.AbstactFactory.ApplicationFactory;
import com.backend.design.pattern.AbstactFactory.commonUtility.Button;
import com.backend.design.pattern.AbstactFactory.commonUtility.Checkbox;
import com.backend.design.pattern.AbstactFactory.mac.MacButton;
import com.backend.design.pattern.AbstactFactory.mac.MacCheckbox;

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
