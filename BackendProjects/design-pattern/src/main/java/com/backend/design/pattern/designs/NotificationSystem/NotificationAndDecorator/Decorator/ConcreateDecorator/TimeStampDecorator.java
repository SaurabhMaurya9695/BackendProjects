package com.backend.design.pattern.designs.NotificationSystem.NotificationAndDecorator.Decorator.ConcreateDecorator;

import com.backend.design.pattern.designs.NotificationSystem.NotificationAndDecorator.Decorator.NotificationDecorator;
import com.backend.design.pattern.designs.NotificationSystem.NotificationAndDecorator.INotification;
import com.backend.design.pattern.designs.Tomato.utils.TimeUtils;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TimeStampDecorator extends NotificationDecorator {

    public TimeStampDecorator(INotification notification) {
        super(notification);
    }

    @Override
    public String getContent() {
        return super.getContent();
    }
}
