package com.backend.design.pattern.designs.NotificationSystem;

import com.backend.design.pattern.designs.NotificationSystem.NotificationAndDecorator.Decorator.ConcreateDecorator.SignatureDecorator;
import com.backend.design.pattern.designs.NotificationSystem.NotificationAndDecorator.Decorator.ConcreateDecorator.TimeStampDecorator;
import com.backend.design.pattern.designs.NotificationSystem.NotificationAndDecorator.INotification;
import com.backend.design.pattern.designs.NotificationSystem.NotificationAndDecorator.SimpleNotification;
import com.backend.design.pattern.designs.NotificationSystem.NotificationBridge.NotificationService;
import com.backend.design.pattern.designs.NotificationSystem.NotificationStrategy.ConcreateStrategies.EmailStrategy;
import com.backend.design.pattern.designs.NotificationSystem.NotificationStrategy.ConcreateStrategies.PopUpStrategy;
import com.backend.design.pattern.designs.NotificationSystem.NotificationStrategy.ConcreateStrategies.SMSStrategy;
import com.backend.design.pattern.designs.NotificationSystem.ObserverAndObservable.Observable_Sender.ConcreateObservable.NotificationObservable;
import com.backend.design.pattern.designs.NotificationSystem.ObserverAndObservable.Observer_Receiver.ConcreateObserver.LoggerNotificationObserver;
import com.backend.design.pattern.designs.NotificationSystem.ObserverAndObservable.Observer_Receiver.ConcreateObserver.NotificationEngine;

public class ClientCode {

    public static void main(String[] args) {
        // we know that "NotificationService" is responsible for the interaction between Notifications and Observers
        NotificationService notificationService = NotificationService.getInstance();
        // above we have a singleton object for notification service

        NotificationObservable notificationObservable = notificationService.getObservable();
        // above now, we have observable

        // now create LoggerObserver & notificationEngine observer
        LoggerNotificationObserver loggerObserver = new LoggerNotificationObserver(notificationObservable);
        NotificationEngine notificationEngine = new NotificationEngine(notificationObservable);

        // now notificationEngine and loggerObserver has subscribed to our notificationObservable

        // now add your strategies to observer
        notificationEngine.addNotificationStrategy(new EmailStrategy("saurabhyash1707@gmail.com"));
        notificationEngine.addNotificationStrategy(new SMSStrategy("+91 6392272716"));
        notificationEngine.addNotificationStrategy(new PopUpStrategy());

        // now attach these observers to observable
        notificationObservable.addReceiver(loggerObserver);
        notificationObservable.addReceiver(notificationEngine);

        // now chaining has been done
        // observable -> observer (Logger & NotificationEngine) -> NotificationEngine -> NotificationStrategies ->
        // {EMAIL, SMS, POP-UP}

        // now left with notifications
        INotification notification = new SimpleNotification("YEEAHHH! PLAYING WITH DESIGN PATTERNS");

        // now decorate this notification
        notification = new TimeStampDecorator(notification);
//        notification = new SignatureDecorator(notification, "CODE WITH SAURABH");

        notificationService.sendNotifications(notification);
    }
}
