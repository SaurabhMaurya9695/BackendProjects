package com.backend.design.pattern.designs.Tinder.MatchingAlgo;

import com.backend.design.pattern.designs.Tinder.Models.User;

public interface Matcher {

    double calculateMatchScore(User user1, User user2);
}
