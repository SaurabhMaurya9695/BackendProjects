package com.backend.design.pattern.designs.NotificationSystem.NotificationStrategy.ConcreateStrategies;

import com.backend.design.pattern.designs.NotificationSystem.NotificationStrategy.INotificationStrategy;

public class PopUpStrategy implements INotificationStrategy {

    @Override
    public void sendNotification(String content) {
        // write your own code to send notification via pop-up
        System.out.print("Sending Notification Via Pop-up Strategy With Content : " + content);
    }
}
