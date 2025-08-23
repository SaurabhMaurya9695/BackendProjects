package com.backend.design.pattern.designs.NotificationSystem.NotificationStrategy.ConcreateStrategies;

import com.backend.design.pattern.designs.NotificationSystem.NotificationStrategy.INotificationStrategy;

public class EmailStrategy implements INotificationStrategy {

    private final String _emailId;

    public EmailStrategy(String emailId) {
        _emailId = emailId;
    }

    @Override
    public void sendNotification(String content) {
        // write your real own code to send notification via email
        System.out.println("Sending Notification Via Email Strategy to : " + _emailId + " With Content : " + content);
    }
}
