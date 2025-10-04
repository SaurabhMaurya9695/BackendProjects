package com.backend.design.pattern.designs.Tinder.Models;

import com.backend.design.pattern.designs.Tinder.Enums.SwipeAction;
import com.backend.design.pattern.designs.Tinder.Observable.NotificationObservable;
import com.backend.design.pattern.designs.Tinder.Observers.NotificationObserver;
import com.backend.design.pattern.designs.Tinder.Observers.UserNotificationObserver;

import java.util.HashMap;
import java.util.Map;

public class User {

    private String _userID;
    private UserProfile _profile;
    private Preference _preference;
    private Map<String, SwipeAction> _swipeHistory;    // userId -> action
    private NotificationObserver _notificationObserver;

    public User(String userId) {
        _userID = userId;
        _profile = new UserProfile();
        _preference = new Preference();
        _swipeHistory = new HashMap<>();
        _notificationObserver = new UserNotificationObserver(userId);
        NotificationObservable.get_instance().registerObserver(userId, _notificationObserver);
    }

    public String getUserID() {
        return _userID;
    }

    public UserProfile getProfile() {
        return _profile;
    }

    public Preference getPreference() {
        return _preference;
    }

    public void swipe(String otherUserId, SwipeAction action) {
        _swipeHistory.put(otherUserId, action);
    }

    public boolean hasLiked(String otherUserId) {
        return _swipeHistory.containsKey(otherUserId) && _swipeHistory.get(otherUserId) == SwipeAction.RIGHT;
    }

    public boolean hasDisliked(String otherUserId) {
        return _swipeHistory.containsKey(otherUserId) && _swipeHistory.get(otherUserId) == SwipeAction.LEFT;
    }

    public boolean hasInteractedWith(String otherUserId) {
        return _swipeHistory.containsKey(otherUserId);
    }

    public void displayProfile() {  // Principle of the least knowledge
        _profile.display();
    }
}