package com.backend.design.pattern.creational.AbstactFactory.windows;

import com.backend.design.pattern.creational.AbstactFactory.commonUtility.Checkbox;

public class WindowsCheckbox implements Checkbox {

    @Override
    public void checkboxPainted() {
        System.out.println("WindowsCheckbox painted with grey Color");
    }
}
