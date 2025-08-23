package com.backend.design.pattern.designs.NotificationSystem.NotificationAndDecorator;

public class SimpleNotification implements INotification {

    private final String _text;

    public SimpleNotification(String text) {
        _text = text;
    }

    @Override
    public String getContent() {
        return _text;
    }
}
