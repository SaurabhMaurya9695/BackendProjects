package com.backend.design.pattern.designs.NotificationSystem.NotificationStrategy;

// Notification can be of any type - Email, SMS, WP etc. so it has to decide by strategy
public interface INotificationStrategy {

    public void sendNotification(String content);
}
