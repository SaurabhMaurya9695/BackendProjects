package com.backend.design.pattern.creational.AbstactFactory.windows;

import com.backend.design.pattern.creational.AbstactFactory.commonUtility.Button;

public class WindowsButton implements Button {

    @Override
    public void buttonPainted() {
        System.out.println("WindowsButton painted with grey Color");
    }
}
