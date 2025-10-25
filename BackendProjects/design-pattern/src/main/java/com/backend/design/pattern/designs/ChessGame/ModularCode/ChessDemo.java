package com.backend.design.pattern.designs.ChessGame.ModularCode;

import com.backend.design.pattern.designs.ChessGame.ModularCode.exception.ChessException;
import com.backend.design.pattern.designs.ChessGame.ModularCode.game.GameEventListener;
import com.backend.design.pattern.designs.ChessGame.ModularCode.game.GameManager;
import com.backend.design.pattern.designs.ChessGame.ModularCode.game.Match;
import com.backend.design.pattern.designs.ChessGame.ModularCode.matching.SimpleMatchingStrategy;
import com.backend.design.pattern.designs.ChessGame.ModularCode.model.Color;
import com.backend.design.pattern.designs.ChessGame.ModularCode.model.GameResult;
import com.backend.design.pattern.designs.ChessGame.ModularCode.model.Move;
import com.backend.design.pattern.designs.ChessGame.ModularCode.model.Position;
import com.backend.design.pattern.designs.ChessGame.ModularCode.user.User;
import com.backend.design.pattern.designs.ChessGame.ModularCode.chat.Message;

/**
 * Demonstration class for the modular chess game system.
 * Shows various features including
 * - Game creation and matchmaking
 * - Move execution
 * - Chat functionality
 * - Event listening
 * - Scholar's Mate scenario
 */
public class ChessDemo {

    /**
     * Simple event listener implementation for demonstration.
     */
    static class ConsoleEventListener implements GameEventListener {
        @Override
        public void onMoveMade(Move move, String matchId) {
            System.out.println("[EVENT] Move made in " + matchId + ": " + move);
        }

        @Override
        public void onCheck(Color color, String matchId) {
            System.out.println("[EVENT] " + color + " is in CHECK in match " + matchId);
        }

        @Override
        public void onGameEnd(GameResult result, String reason, String matchId) {
            System.out.println("[EVENT] Game ended in " + matchId + ": " + result + " by " + reason);
        }

        @Override
        public void onPlayerQuit(String playerName, String matchId) {
            System.out.println("[EVENT] " + playerName + " quit match " + matchId);
        }
    }

    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║  Starting Chess Game System " );
        System.out.println("╚════════════════════════════════════════════════════════════╝\n");

        // Demo 1: Matchmaking System
        demonstrateMatchmaking();

        System.out.println("\n" + "=".repeat(60) + "\n");

        // Demo 2: Scholar's Mate
        demonstrateScholarsMate();

        System.out.println("\n" + "=".repeat(60) + "\n");

        // Demo 3: Chat and Event System
        demonstrateChatAndEvents();
    }

    /**
     * Demonstrates the matchmaking system with multiple users.
     */
    private static void demonstrateMatchmaking() {
        System.out.println("=== Demo 1: Matchmaking System ===\n");

        GameManager gm = GameManager.getInstance();
        gm.reset(); // Clear any previous state

        // Create users with different scores
        User alice = new User("USER_1", "Alice", 1200);
        User bob = new User("USER_2", "Bob", 1180);
        User charlie = new User("USER_3", "Charlie", 900);
        User diana = new User("USER_4", "Diana", 1500);

        System.out.println("Users created:");
        System.out.println("  - " + alice);
        System.out.println("  - " + bob);
        System.out.println("  - " + charlie);
        System.out.println("  - " + diana);
        System.out.println();

        // Request matches
        System.out.println("Requesting matches...\n");
        gm.requestMatch(alice);    // Goes to waiting list
        gm.requestMatch(bob);      // Matches with Alice (similar score)
        gm.requestMatch(charlie);  // Goes to waiting list
        gm.requestMatch(diana);    // Goes to waiting list (no match within tolerance)

        System.out.println();
        gm.displayActiveMatches();

        // Change strategy to simple FIFO matching
        System.out.println("\n--- Switching to Simple Matching Strategy ---\n");
        gm.setMatchingStrategy(new SimpleMatchingStrategy());

        gm.requestMatch(new User("USER_5", "Eve", 2000)); // Matches with Charlie (first in queue)
        gm.displayActiveMatches();
    }

    /**
     * Demonstrates a complete game with Scholar's Mate scenario.
     */
    private static void demonstrateScholarsMate() {
        System.out.println("=== Demo 2: Scholar's Mate (4-move Checkmate) ===\n");

        User aditya = new User("DEMO_1", "Aditya");
        User rohit = new User("DEMO_2", "Rohit");

        // Create match directly
        Match match = new Match("SCHOLARS_MATE_DEMO", aditya, rohit);

        // Add event listener
        match.addEventListener(new ConsoleEventListener());

        System.out.println("Starting position:");
        match.displayBoard();

        try {
            // Move 1: White e2-e4
            System.out.println("\n--- Move 1: White e4 ---");
            match.makeMove(Position.fromNotation("e2"), Position.fromNotation("e4"), aditya);

            // Move 1: Black e7-e5
            System.out.println("\n--- Move 1: Black e5 ---");
            match.makeMove(Position.fromNotation("e7"), Position.fromNotation("e5"), rohit);

            // Move 2: White Bf1-c4
            System.out.println("\n--- Move 2: White Bc4 (targeting f7) ---");
            match.makeMove(Position.fromNotation("f1"), Position.fromNotation("c4"), aditya);

            // Move 2: Black Nb8-c6
            System.out.println("\n--- Move 2: Black Nc6 ---");
            match.makeMove(Position.fromNotation("b8"), Position.fromNotation("c6"), rohit);

            // Move 3: White Qd1-h5
            System.out.println("\n--- Move 3: White Qh5 (attacking f7) ---");
            match.makeMove(Position.fromNotation("d1"), Position.fromNotation("h5"), aditya);

            // Move 3: Black Ng8-f6??
            System.out.println("\n--- Move 3: Black Nf6 (mistake!) ---");
            match.makeMove(Position.fromNotation("g8"), Position.fromNotation("f6"), rohit);

            // Move 4: White Qh5xf7# (Checkmate!)
            System.out.println("\n--- Move 4: White Qxf7# (CHECKMATE!) ---");
            match.makeMove(Position.fromNotation("h5"), Position.fromNotation("f7"), aditya);

        } catch (ChessException e) {
            System.err.println("Error during game: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\nFinal scores:");
        System.out.println("  - " + aditya);
        System.out.println("  - " + rohit);

        System.out.println("\nMove history: " + match.getMoveHistory());
    }

    /**
     * Demonstrates chat functionality and event system.
     */
    private static void demonstrateChatAndEvents() {
        System.out.println("=== Demo 3: Chat and Event System ===\n");

        User player1 = new User("CHAT_1", "Emma");
        User player2 = new User("CHAT_2", "Liam");

        Match match = new Match("CHAT_DEMO", player1, player2);
        match.addEventListener(new ConsoleEventListener());

        System.out.println("Testing chat functionality:\n");

        // Send messages
        player1.send(new Message(player1.getId(), "Good luck, have fun!"));
        player2.send(new Message(player2.getId(), "Thanks, you too!"));

        System.out.println("\nMaking a few moves...\n");

        try {
            // Make some moves to demonstrate events
            match.makeMove(Position.fromNotation("e2"), Position.fromNotation("e4"), player1);
            player1.send(new Message(player1.getId(), "Going for a classic opening"));

            match.makeMove(Position.fromNotation("e7"), Position.fromNotation("e5"), player2);
            player2.send(new Message(player2.getId(), "I'll mirror it"));

            match.makeMove(Position.fromNotation("g1"), Position.fromNotation("f3"), player1);
            match.makeMove(Position.fromNotation("b8"), Position.fromNotation("c6"), player2);

            System.out.println("\nCurrent board state:");
            match.displayBoard();

        } catch (ChessException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}

