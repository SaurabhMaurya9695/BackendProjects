package com.backend.design.pattern.designs.Tinder.Strategy;

import com.backend.design.pattern.designs.Tinder.Models.Location;
import com.backend.design.pattern.designs.Tinder.Models.User;

import java.util.ArrayList;
import java.util.List;

public class BasicLocationStrategy implements LocationStrategy {

    @Override
    public List<User> findNearbyUsers(Location location, double maxDistance, List<User> allUsers) {
        List<User> nearbyUsers = new ArrayList<>();
        for (User user : allUsers) {
            double distance = location.distanceInKm(user.getProfile().getLocation());
            if (distance <= maxDistance) {
                nearbyUsers.add(user);
            }
        }
        return nearbyUsers;
    }
}
