package com.backend.design.pattern.AbstactFactory;

import com.backend.design.pattern.AbstactFactory.commonUtility.Button;
import com.backend.design.pattern.AbstactFactory.commonUtility.Checkbox;

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
