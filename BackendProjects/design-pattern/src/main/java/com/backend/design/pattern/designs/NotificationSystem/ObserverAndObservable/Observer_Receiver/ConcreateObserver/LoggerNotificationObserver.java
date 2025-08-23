package com.backend.design.pattern.designs.NotificationSystem.ObserverAndObservable.Observer_Receiver.ConcreateObserver;

import com.backend.design.pattern.designs.NotificationSystem.ObserverAndObservable.Observable_Sender.ConcreateObservable.NotificationObservable;
import com.backend.design.pattern.designs.NotificationSystem.ObserverAndObservable.Observer_Receiver.INotificationObserver;
import com.backend.design.pattern.designs.Tomato.utils.TimeUtils;

// LoggerNotificationObserver subscribe to our sender List -> Observable (NotificationObservable)
public class LoggerNotificationObserver implements INotificationObserver {

    NotificationObservable _notificationSender;

    public LoggerNotificationObserver(NotificationObservable notificationSender) {
        _notificationSender = notificationSender;
    }

    @Override
    public void update() {
        System.out.println(TimeUtils.getCurrentTime() + " : Receiving logging notification from Observable -> "
                + _notificationSender.getCurrentNotificationContent());
    }
}
