package com.backend.design.pattern.designs.NotificationSystem.NotificationStrategy.ConcreateStrategies;

import com.backend.design.pattern.designs.NotificationSystem.NotificationStrategy.INotificationStrategy;

public class SMSStrategy implements INotificationStrategy {

    private final String _phoneNo;

    public SMSStrategy(String phoneNo) {
        _phoneNo = phoneNo;
    }

    @Override
    public void sendNotification(String content) {
        // write your own real code to send notification via SMS
        System.out.println("Sending Notification Via SMS Strategy to : " + _phoneNo + " With Content : " + content);
    }
}
