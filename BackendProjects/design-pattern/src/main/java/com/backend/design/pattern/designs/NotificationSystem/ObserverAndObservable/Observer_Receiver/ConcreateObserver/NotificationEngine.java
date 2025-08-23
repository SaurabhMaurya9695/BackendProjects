package com.backend.design.pattern.designs.NotificationSystem.ObserverAndObservable.Observer_Receiver.ConcreateObserver;

import com.backend.design.pattern.designs.NotificationSystem.NotificationStrategy.INotificationStrategy;
import com.backend.design.pattern.designs.NotificationSystem.ObserverAndObservable.Observable_Sender.ConcreateObservable.NotificationObservable;
import com.backend.design.pattern.designs.NotificationSystem.ObserverAndObservable.Observer_Receiver.INotificationObserver;
import com.backend.design.pattern.designs.Tomato.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;

public class NotificationEngine implements INotificationObserver {

    private final NotificationObservable _notificationSender;
    private List<INotificationStrategy> _strategies;

    public NotificationEngine(NotificationObservable notificationSender) {
        _notificationSender = notificationSender;
        _strategies = new ArrayList<>();
    }

    @Override
    public void update() {
        String content = _notificationSender.getCurrentNotificationContent();
        System.out.println(
                TimeUtils.getCurrentTime() + " : Receiving NotificationEngine notification from Observable -> "
                        + content);
        // now share this content to All Strategy
        for (INotificationStrategy strategy : _strategies) {
            System.out.println("Delegating request in Strategies : " + strategy.getClass().getSimpleName());
            strategy.sendNotification(content);
        }
    }

    public void addNotificationStrategy(INotificationStrategy strategy) {
        if (_strategies != null && !_strategies.contains(strategy)) {
            _strategies.add(strategy);
        }
    }

    public NotificationObservable getNotificationSender() {
        return _notificationSender;
    }
}
