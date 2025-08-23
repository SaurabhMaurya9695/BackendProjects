package com.backend.design.pattern.designs.NotificationSystem.ObserverAndObservable.Observable_Sender.ConcreateObservable;

import com.backend.design.pattern.designs.NotificationSystem.NotificationAndDecorator.INotification;
import com.backend.design.pattern.designs.NotificationSystem.ObserverAndObservable.Observable_Sender.INotificationObservable;
import com.backend.design.pattern.designs.NotificationSystem.ObserverAndObservable.Observer_Receiver.INotificationObserver;

import java.util.ArrayList;
import java.util.List;

public class NotificationObservable implements INotificationObservable {

    private List<INotificationObserver> _receiverList;

    private INotification _currentNotification;

    public NotificationObservable() {
        _currentNotification = null;
        _receiverList = new ArrayList<>();
    }

    @Override
    public void addReceiver(INotificationObserver IObserverReceiver) {
        if (_receiverList != null && !_receiverList.contains(IObserverReceiver)) {
            _receiverList.add(IObserverReceiver);
        }
    }

    @Override
    public void removeReceiver(INotificationObserver IObserverReceiver) {
        if (IObserverReceiver != null && !_receiverList.isEmpty() && _receiverList.contains(IObserverReceiver)) {
            _receiverList.add(IObserverReceiver);
        }
    }

    @Override
    public void notifyReceiver() {
        for (INotificationObserver observer : _receiverList) {
            observer.update();
        }
    }

    public INotification getCurrentNotification() {
        return _currentNotification;
    }

    public String getCurrentNotificationContent() {
        return _currentNotification.getContent();
    }

    public void setCurrentNotification(INotification currentNotification) {
        _currentNotification = currentNotification;
        System.out.println("Notifying Observer's");
        notifyReceiver();
    }
}
