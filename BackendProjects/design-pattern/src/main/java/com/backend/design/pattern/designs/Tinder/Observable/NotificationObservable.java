package com.backend.design.pattern.designs.Tinder.Observable;

import com.backend.design.pattern.designs.Tinder.Observers.NotificationObserver;

import java.util.HashMap;
import java.util.Map;

public class NotificationObservable {

    private Map<String, NotificationObserver> _observerMap;
    private static NotificationObservable _instance = null;

    public NotificationObservable() {
        _observerMap = new HashMap<>();
    }

    public static NotificationObservable get_instance() {
        if (_instance == null) {
            _instance = new NotificationObservable();
        }
        return _instance;
    }

    public void registerObserver(String userId, NotificationObserver observer) {
        _observerMap.put(userId, observer);
    }

    public void removeObserver(String userId) {
        _observerMap.remove(userId);
    }

    public void notifyUser(String userId, String msg) {
        if (!_observerMap.containsKey(userId)) {
            System.out.println("[NotificationObservable] user not found with this id " + userId);
        }
        _observerMap.get(userId).update(msg);
    }

    public void notifyAll(String msg) {
        _observerMap.forEach((userId, observer) -> {
            observer.update(msg);
        });
    }
}
