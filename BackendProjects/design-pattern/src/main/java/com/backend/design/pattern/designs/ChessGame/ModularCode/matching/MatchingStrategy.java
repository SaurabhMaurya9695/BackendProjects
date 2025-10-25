package com.backend.design.pattern.designs.ChessGame.ModularCode.matching;

import com.backend.design.pattern.designs.ChessGame.ModularCode.user.User;

import java.util.List;

/**
 * Strategy interface for matching players.
 * Implements the Strategy Pattern to allow different matching algorithms.
 * Follows Open/Closed Principle - new strategies can be added without modifying existing code.
 */
public interface MatchingStrategy {

    /**
     * Finds a suitable opponent for the given user from the waiting list.
     *
     * @param user         the user looking for a match
     * @param waitingUsers list of users waiting for a match
     * @return a matched opponent, or null if no suitable match found
     */
    User findMatch(User user, List<User> waitingUsers);
}

