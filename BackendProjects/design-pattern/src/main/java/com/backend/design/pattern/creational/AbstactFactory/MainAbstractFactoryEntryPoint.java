package com.backend.design.pattern.creational.AbstactFactory;

import com.backend.design.pattern.creational.AbstactFactory.commonUtility.Button;
import com.backend.design.pattern.creational.AbstactFactory.commonUtility.Checkbox;

public class MainAbstractFactoryEntryPoint {

    private final Button _button;
    private final Checkbox _checkbox;

    public MainAbstractFactoryEntryPoint(ApplicationFactory factory) {
        _button = factory.createButton();
        _checkbox = factory.createCheckBox();
    }

    public void paintOs() {
        _button.buttonPainted();
        _checkbox.checkboxPainted();
    }
}
