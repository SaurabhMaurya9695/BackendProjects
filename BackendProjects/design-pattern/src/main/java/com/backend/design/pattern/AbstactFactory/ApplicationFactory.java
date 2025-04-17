package com.backend.design.pattern.AbstactFactory;

import com.backend.design.pattern.AbstactFactory.commonUtility.Button;
import com.backend.design.pattern.AbstactFactory.commonUtility.Checkbox;

public interface ApplicationFactory {
    Button createButton();
    Checkbox createCheckBox();
}
