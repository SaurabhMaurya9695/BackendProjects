package com.backend.system.design.LLD.RateLimiter.model;

/**
 * Enum representing different rate limiting algorithms
 */
public enum RateLimiterType {
    TOKEN_BUCKET("Token Bucket", "Allows burst traffic, refills at constant rate"),
    LEAKY_BUCKET("Leaky Bucket", "Processes requests at constant rate"),
    FIXED_WINDOW_COUNTER("Fixed Window Counter", "Simple counter per time window"),
    SLIDING_WINDOW_LOG("Sliding Window Log", "Most accurate, stores all timestamps"),
    SLIDING_WINDOW_COUNTER("Sliding Window Counter", "Production-ready, balanced approach");

    private final String displayName;
    private final String description;

    RateLimiterType(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }
}

