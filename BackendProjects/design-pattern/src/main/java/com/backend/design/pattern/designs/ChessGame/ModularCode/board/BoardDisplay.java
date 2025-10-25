package com.backend.design.pattern.designs.ChessGame.ModularCode.board;

import com.backend.design.pattern.designs.ChessGame.ModularCode.pieces.Piece;

/**
 * Handles the visual display of the chess board.
 * Separates display logic from board state management (Single Responsibility Principle).
 * This allows for different display implementations (console, GUI, web) without changing Board class.
 */
public class BoardDisplay {

    private static final int CELL_WIDTH = 3;

    /**
     * Displays the board in a formatted console output.
     *
     * @param board the board to display
     */
    public static void display(Board board) {
        printBorder();
        printColumnLabels();
        printBorder();

        // Print each rank (row) from 8 to 1
        for (int rank = 8; rank >= 1; rank--) {
            int row = 8 - rank;
            System.out.print(rank + " |");

            for (int col = 0; col < 8; col++) {
                Piece piece = board.getPiece(new com.backend.design.pattern.designs.ChessGame.ModularCode.model.Position(row, col));
                String pieceStr = piece != null ? piece.toString() : "  ";

                // Center the piece string in the cell
                int pad = (CELL_WIDTH - 2) / 2;
                System.out.print(" ".repeat(pad) + pieceStr + " ".repeat(CELL_WIDTH - 2 - pad) + "|");
            }

            System.out.println(" " + rank);
            printBorder();
        }

        printColumnLabels();
        printBorder();
    }

    /**
     * Prints a horizontal border line.
     */
    private static void printBorder() {
        System.out.print("  +");
        for (int i = 0; i < 8; i++) {
            System.out.print("-".repeat(CELL_WIDTH) + "+");
        }
        System.out.println();
    }

    /**
     * Prints column labels (a-h).
     */
    private static void printColumnLabels() {
        System.out.print("  |");
        for (char file = 'a'; file <= 'h'; file++) {
            int pad = (CELL_WIDTH - 1) / 2;
            System.out.print(" ".repeat(pad) + file + " ".repeat(CELL_WIDTH - 1 - pad) + "|");
        }
        System.out.println();
    }

    /**
     * Displays the board with move highlighting.
     *
     * @param board         the board to display
     * @param fromPosition  the starting position to highlight
     * @param toPosition    the ending position to highlight
     */
    public static void displayWithHighlight(Board board,
                                            com.backend.design.pattern.designs.ChessGame.ModularCode.model.Position fromPosition,
                                            com.backend.design.pattern.designs.ChessGame.ModularCode.model.Position toPosition) {
        // For now, just call the standard display
        // Future enhancement: add color highlighting for terminal
        System.out.println("Last move: " + fromPosition + " -> " + toPosition);
        display(board);
    }
}

