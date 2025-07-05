package com.backend.design.pattern.creational.AbstactFactory.factory;

import com.backend.design.pattern.creational.AbstactFactory.ApplicationFactory;
import com.backend.design.pattern.creational.AbstactFactory.commonUtility.Button;
import com.backend.design.pattern.creational.AbstactFactory.commonUtility.Checkbox;
import com.backend.design.pattern.creational.AbstactFactory.windows.WindowsButton;
import com.backend.design.pattern.creational.AbstactFactory.windows.WindowsCheckbox;

public class WindowsFactory implements ApplicationFactory {

    @Override
    public Button createButton() {
        return new WindowsButton();
    }

    @Override
    public Checkbox createCheckBox() {
        return new WindowsCheckbox();
    }
}
