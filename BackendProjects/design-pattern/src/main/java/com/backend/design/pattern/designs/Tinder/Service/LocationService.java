package com.backend.design.pattern.designs.Tinder.Service;

import com.backend.design.pattern.designs.Tinder.Models.Location;
import com.backend.design.pattern.designs.Tinder.Models.User;
import com.backend.design.pattern.designs.Tinder.Strategy.BasicLocationStrategy;
import com.backend.design.pattern.designs.Tinder.Strategy.LocationStrategy;

import java.util.List;

public class LocationService {

    private LocationStrategy _strategy;

    // Singleton Pattern
    private static LocationService _instance;

    private LocationService() {
        _strategy = new BasicLocationStrategy();
    }

    public static LocationService get_instance() {
        if (_instance == null) {
            _instance = new LocationService();
        }
        return _instance;
    }

    public void setStrategy(LocationStrategy newStrategy) {
        _strategy = newStrategy;
    }

    public List<User> findNearbyUsers(Location location, double maxDistance, List<User> allUsers) {
        return _strategy.findNearbyUsers(location, maxDistance, allUsers);
    }
}