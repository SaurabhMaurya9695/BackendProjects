package com.backend.design.pattern.designs.Tinder.Factory;

import com.backend.design.pattern.designs.Tinder.Enums.MatcherType;
import com.backend.design.pattern.designs.Tinder.MatchingAlgo.BasicMatcher;
import com.backend.design.pattern.designs.Tinder.MatchingAlgo.InterestsBasedMatcher;
import com.backend.design.pattern.designs.Tinder.MatchingAlgo.LocationBasedMatcher;
import com.backend.design.pattern.designs.Tinder.MatchingAlgo.Matcher;

public class MatcherFactory {

    public static Matcher createMatcher(MatcherType type) {
        return switch (type) {
            case INTERESTS_BASED -> new InterestsBasedMatcher();
            case LOCATION_BASED -> new LocationBasedMatcher();
            default -> new BasicMatcher();
        };
    }
}
