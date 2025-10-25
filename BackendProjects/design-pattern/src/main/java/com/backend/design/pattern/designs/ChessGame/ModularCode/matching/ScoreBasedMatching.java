package com.backend.design.pattern.designs.ChessGame.ModularCode.matching;

import com.backend.design.pattern.designs.ChessGame.ModularCode.user.User;

import java.util.List;

/**
 * Score-based matchmaking strategy.
 * Matches players with similar skill ratings within a tolerance range.
 */
public class ScoreBasedMatching implements MatchingStrategy {

    private final int scoreTolerance;

    /**
     * Creates a new score-based matching strategy.
     *
     * @param scoreTolerance maximum score difference for a valid match
     */
    public ScoreBasedMatching(int scoreTolerance) {
        this.scoreTolerance = scoreTolerance;
    }

    @Override
    public User findMatch(User user, List<User> waitingUsers) {
        User bestMatch = null;
        int bestScoreDiff = Integer.MAX_VALUE;

        for (User waitingUser : waitingUsers) {
            // Don't match with self
            if (waitingUser.getId().equals(user.getId())) {
                continue;
            }

            int scoreDiff = Math.abs(waitingUser.getScore() - user.getScore());

            // Check if within tolerance and better than current best
            if (scoreDiff <= scoreTolerance && scoreDiff < bestScoreDiff) {
                bestMatch = waitingUser;
                bestScoreDiff = scoreDiff;
            }
        }

        return bestMatch;
    }

    /**
     * Gets the current score tolerance.
     *
     * @return the score tolerance value
     */
    public int getScoreTolerance() {
        return scoreTolerance;
    }
}

