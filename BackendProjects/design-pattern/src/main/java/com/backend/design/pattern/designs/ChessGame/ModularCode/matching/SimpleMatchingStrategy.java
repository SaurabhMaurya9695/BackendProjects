package com.backend.design.pattern.designs.ChessGame.ModularCode.matching;

import com.backend.design.pattern.designs.ChessGame.ModularCode.user.User;

import java.util.List;

/**
 * First-come-first-served matching strategy.
 * Simply matches the user with the first available player in the waiting list.
 */
public class SimpleMatchingStrategy implements MatchingStrategy {

    @Override
    public User findMatch(User user, List<User> waitingUsers) {
        if (waitingUsers.isEmpty()) {
            return null;
        }

        // Return the first waiting user (FIFO)
        for (User waitingUser : waitingUsers) {
            if (!waitingUser.getId().equals(user.getId())) {
                return waitingUser;
            }
        }

        return null;
    }
}

