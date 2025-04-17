package com.backend.design.pattern.AbstactFactory.windows;

import com.backend.design.pattern.AbstactFactory.commonUtility.Button;
import com.backend.design.pattern.AbstactFactory.mac.MacButton;

public class WindowsButton implements Button {

    @Override
    public void buttonPainted() {
        System.out.println("WindowsButton painted with grey Color");
    }
}
