package com.backend.design.pattern.designs.ChessGame.ModularCode.game;

import com.backend.design.pattern.designs.ChessGame.ModularCode.chat.Message;
import com.backend.design.pattern.designs.ChessGame.ModularCode.exception.ChessException;
import com.backend.design.pattern.designs.ChessGame.ModularCode.matching.MatchingStrategy;
import com.backend.design.pattern.designs.ChessGame.ModularCode.matching.ScoreBasedMatching;
import com.backend.design.pattern.designs.ChessGame.ModularCode.model.Position;
import com.backend.design.pattern.designs.ChessGame.ModularCode.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Manages all chess matches and matchmaking.
 * Implements the Singleton Pattern with thread-safe lazy initialization.
 * Uses Dependency Injection for the matching strategy.
 */
public class GameManager {

    private static volatile GameManager instance;
    private static final Object LOCK = new Object();

    private final Map<String, Match> activeMatches;
    private final List<User> waitingUsers;
    private MatchingStrategy matchingStrategy;
    private final AtomicInteger matchCounter;

    /**
     * Private constructor for singleton pattern.
     */
    private GameManager() {
        this.activeMatches = new ConcurrentHashMap<>();
        this.waitingUsers = new ArrayList<>();
        this.matchingStrategy = new ScoreBasedMatching(100); // Default: 100 points tolerance
        this.matchCounter = new AtomicInteger(0);
    }

    /**
     * Gets the singleton instance of GameManager (thread-safe).
     *
     * @return the GameManager instance
     */
    public static GameManager getInstance() {
        if (instance == null) {
            synchronized (LOCK) {
                if (instance == null) {
                    instance = new GameManager();
                }
            }
        }
        return instance;
    }

    /**
     * Sets the matching strategy (Dependency Injection).
     *
     * @param strategy the matching strategy to use
     */
    public void setMatchingStrategy(MatchingStrategy strategy) {
        this.matchingStrategy = strategy;
    }

    /**
     * Requests a match for a user.
     * If a suitable opponent is found, creates a match; otherwise adds to waiting list.
     *
     * @param user the user requesting a match
     * @return the created match, or null if user added to waiting list
     */
    public synchronized Match requestMatch(User user) {
        System.out.println(user.getName() + " is looking for a match...");

        User opponent = matchingStrategy.findMatch(user, waitingUsers);

        if (opponent != null) {
            // Remove opponent from waiting list
            waitingUsers.remove(opponent);

            // Create match
            String matchId = "MATCH_" + matchCounter.incrementAndGet();
            Match match = new Match(matchId, user, opponent);
            activeMatches.put(matchId, match);

            System.out.println("Match found! " + user.getName() + " vs " + opponent.getName());
            match.displayBoard();

            return match;
        } else {
            // Add to waiting list
            if (!waitingUsers.contains(user)) {
                waitingUsers.add(user);
                System.out.println(user.getName() + " added to waiting list.");
            }
            return null;
        }
    }

    /**
     * Executes a move in a match.
     *
     * @param matchId the match ID
     * @param from    starting position
     * @param to      destination position
     * @param player  the player making the move
     * @throws ChessException if the move is invalid
     */
    public void makeMove(String matchId, Position from, Position to, User player) throws ChessException {
        Match match = activeMatches.get(matchId);
        if (match == null) {
            throw new ChessException("Match not found: " + matchId);
        }

        match.makeMove(from, to, player);

        // Remove match if completed
        if (match.getStatus() == com.backend.design.pattern.designs.ChessGame.ModularCode.model.GameStatus.COMPLETED) {
            activeMatches.remove(matchId);
            System.out.println("Match " + matchId + " completed and removed from active matches.");
        }
    }

    /**
     * Allows a player to quit a match.
     *
     * @param matchId the match ID
     * @param player  the player quitting
     */
    public synchronized void quitMatch(String matchId, User player) {
        Match match = activeMatches.get(matchId);
        if (match != null) {
            match.quitGame(player);
            activeMatches.remove(matchId);
        }
    }

    /**
     * Sends a chat message in a match.
     *
     * @param matchId the match ID
     * @param message the message content
     * @param user    the user sending the message
     */
    public void sendChatMessage(String matchId, String message, User user) {
        Match match = activeMatches.get(matchId);
        if (match != null) {
            Message msg = new Message(user.getId(), message);
            match.sendMessage(msg, user);
        }
    }

    /**
     * Gets a match by ID.
     *
     * @param matchId the match ID
     * @return the match, or null if not found
     */
    public Match getMatch(String matchId) {
        return activeMatches.get(matchId);
    }

    /**
     * Displays information about all active matches.
     */
    public void displayActiveMatches() {
        System.out.println("\n=== Active Matches ===");
        for (Map.Entry<String, Match> entry : activeMatches.entrySet()) {
            Match match = entry.getValue();
            System.out.println("Match " + match.getMatchId() + ": " +
                    match.getWhitePlayer().getName() + " vs " +
                    match.getBlackPlayer().getName());
        }
        System.out.println("Total active matches: " + activeMatches.size());
        System.out.println("Users waiting: " + waitingUsers.size());
    }

    /**
     * Gets the number of active matches.
     *
     * @return number of active matches
     */
    public int getActiveMatchCount() {
        return activeMatches.size();
    }

    /**
     * Gets the number of users waiting for a match.
     *
     * @return number of waiting users
     */
    public int getWaitingUserCount() {
        return waitingUsers.size();
    }

    /**
     * Clears all matches and waiting users (for testing).
     */
    public synchronized void reset() {
        activeMatches.clear();
        waitingUsers.clear();
        matchCounter.set(0);
    }
}

