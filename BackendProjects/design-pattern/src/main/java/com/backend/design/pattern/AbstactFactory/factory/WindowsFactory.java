package com.backend.design.pattern.AbstactFactory.factory;

import com.backend.design.pattern.AbstactFactory.ApplicationFactory;
import com.backend.design.pattern.AbstactFactory.commonUtility.Button;
import com.backend.design.pattern.AbstactFactory.commonUtility.Checkbox;
import com.backend.design.pattern.AbstactFactory.windows.WindowsButton;
import com.backend.design.pattern.AbstactFactory.windows.WindowsCheckbox;

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
