package com.backend.design.pattern.designs.NotificationSystem.NotificationAndDecorator.Decorator.ConcreateDecorator;

import com.backend.design.pattern.designs.NotificationSystem.NotificationAndDecorator.Decorator.NotificationDecorator;
import com.backend.design.pattern.designs.NotificationSystem.NotificationAndDecorator.INotification;

public class SignatureDecorator extends NotificationDecorator {

    private final String _signature;

    public SignatureDecorator(INotification notification, String signature) {
        super(notification);
        _signature = signature;
    }

    @Override
    public String getContent() {
        return super.getContent() + "With My Signature " + _signature;
    }
}
