package com.backend.design.pattern.designs.Tinder.Strategy;

import com.backend.design.pattern.designs.Tinder.Models.Location;
import com.backend.design.pattern.designs.Tinder.Models.User;

import java.util.List;

public interface LocationStrategy {
    List<User> findNearbyUsers(Location location, double maxDistance, List<User> allUsers);
}
