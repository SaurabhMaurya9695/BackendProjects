package com.backend.design.pattern.designs.Tinder.Observers;

public class UserNotificationObserver implements NotificationObserver {

    private String userId;

    public UserNotificationObserver(String userId) {
        this.userId = userId;
    }

    @Override
    public void update(String msg) {
        System.out.println(
                "[UserNotificationObserver] : Notification from the user for userId " + userId + " and Message is "
                        + msg);
    }
}
