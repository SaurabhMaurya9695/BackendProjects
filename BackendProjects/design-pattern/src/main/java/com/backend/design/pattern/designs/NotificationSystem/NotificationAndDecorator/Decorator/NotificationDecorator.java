package com.backend.design.pattern.designs.NotificationSystem.NotificationAndDecorator.Decorator;

import com.backend.design.pattern.designs.NotificationSystem.NotificationAndDecorator.INotification;

// Notification decorator 'has-a' and 'Is-a' Notification
// This Decorator can decorate SimpleNotification etc. with Multiple Functionality
public class NotificationDecorator implements INotification {

    private final INotification _iNotification;

    public NotificationDecorator(INotification notification) {
        _iNotification = notification;
    }

    @Override
    public String getContent() {
        return _iNotification.getContent() + "\n";
    }
}
