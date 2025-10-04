package com.backend.design.pattern.designs.Tinder.MatchingAlgo;

import com.backend.design.pattern.designs.Tinder.Models.User;

public class BasicMatcher implements Matcher {

    @Override
    public double calculateMatchScore(User user1, User user2) {
        // Basic scoring, just check if preferences align
        boolean user1LikesUser2Gender = user1.getPreference().isInterestedInGender(user2.getProfile().getGender());
        boolean user2LikesUser1Gender = user2.getPreference().isInterestedInGender(user1.getProfile().getGender());

        if (!user1LikesUser2Gender || !user2LikesUser1Gender) {
            return 0.0;
        }

        // Check age preference
        boolean user1LikesUser2Age = user1.getPreference().isAgeInRange(user2.getProfile().getAge());
        boolean user2LikesUser1Age = user2.getPreference().isAgeInRange(user1.getProfile().getAge());

        if (!user1LikesUser2Age || !user2LikesUser1Age) {
            return 0.0;
        }

        // Check distance preference
        double distance = user1.getProfile().getLocation().distanceInKm(user2.getProfile().getLocation());
        boolean user1LikesUser2Distance = user1.getPreference().isDistanceAcceptable(distance);
        boolean user2LikesUser1Distance = user2.getPreference().isDistanceAcceptable(distance);

        if (!user1LikesUser2Distance || !user2LikesUser1Distance) {
            return 0.0;
        }

        // If all basic criteria match, return a base score
        return 0.5; // 50% match
    }
}
