package com.backend.design.pattern.designs.NotificationSystem.NotificationBridge;

import com.backend.design.pattern.designs.NotificationSystem.NotificationAndDecorator.INotification;
import com.backend.design.pattern.designs.NotificationSystem.ObserverAndObservable.Observable_Sender.ConcreateObservable.NotificationObservable;

import java.util.ArrayList;
import java.util.List;

// It is a bridge / Manage between Notification and the observer
// This should be a singleton class
// Any client Code will interact with this code only
// It keeps track of all Notifications
public class NotificationService {

    private final NotificationObservable _observable;
    static NotificationService _instance;
    private final List<INotification> _notifications;

    private NotificationService() {
        _observable = new NotificationObservable();
        _notifications = new ArrayList<>();
    }

    public static NotificationService getInstance() {
        if (_instance == null) {
            return new NotificationService();
        }
        return _instance;
    }

    public NotificationObservable getObservable() {
        return _observable;
    }

    // create a new notification and notify observers
    public void sendNotifications(INotification iNotification) {
        _notifications.add(iNotification); // keeping track of notifications
        _observable.setCurrentNotification(iNotification);// set the current Notification and update the receivers
    }
}
