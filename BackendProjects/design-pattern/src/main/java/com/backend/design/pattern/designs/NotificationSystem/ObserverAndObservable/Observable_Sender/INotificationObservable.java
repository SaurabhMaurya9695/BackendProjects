package com.backend.design.pattern.designs.NotificationSystem.ObserverAndObservable.Observable_Sender;

import com.backend.design.pattern.designs.NotificationSystem.ObserverAndObservable.Observer_Receiver.INotificationObserver;

public interface INotificationObservable {

    void addReceiver(INotificationObserver IObserverReceiver);

    void removeReceiver(INotificationObserver IObserverReceiver);

    void notifyReceiver();
}
