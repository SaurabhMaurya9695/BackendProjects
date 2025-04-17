package com.backend.design.pattern.AbstactFactory.mac;

import com.backend.design.pattern.AbstactFactory.commonUtility.Checkbox;

public class MacCheckbox implements Checkbox {

    @Override
    public void checkboxPainted() {
        System.out.println("MacButton painted with black Color");
    }
}
