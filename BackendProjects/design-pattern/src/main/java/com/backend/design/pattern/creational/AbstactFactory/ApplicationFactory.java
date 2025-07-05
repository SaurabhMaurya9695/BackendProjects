package com.backend.design.pattern.creational.AbstactFactory;

import com.backend.design.pattern.creational.AbstactFactory.commonUtility.Button;
import com.backend.design.pattern.creational.AbstactFactory.commonUtility.Checkbox;

public interface ApplicationFactory {
    Button createButton();
    Checkbox createCheckBox();
}
