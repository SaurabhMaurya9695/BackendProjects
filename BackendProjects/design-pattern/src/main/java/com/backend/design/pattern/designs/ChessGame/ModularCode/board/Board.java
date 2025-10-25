package com.backend.design.pattern.designs.ChessGame.ModularCode.board;

import com.backend.design.pattern.designs.ChessGame.ModularCode.model.Color;
import com.backend.design.pattern.designs.ChessGame.ModularCode.model.PieceType;
import com.backend.design.pattern.designs.ChessGame.ModularCode.model.Position;
import com.backend.design.pattern.designs.ChessGame.ModularCode.pieces.Piece;
import com.backend.design.pattern.designs.ChessGame.ModularCode.pieces.PieceFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents the chess board and manages piece positions.
 * This class follows the Single Responsibility Principle - it only manages board state.
 * Display logic is separated into BoardDisplay class.
 */
public class Board {

    private final Piece[][] board;
    private final Map<Position, Piece> piecePositions;
    private Position lastDoubleMovePawn; // For en passant detection

    /**
     * Creates a new chess board and initializes it with pieces in starting positions.
     */
    public Board() {
        this.board = new Piece[8][8];
        this.piecePositions = new HashMap<>();
        this.lastDoubleMovePawn = null;
        initializeBoard();
    }

    /**
     * Creates an empty board (used for copying/testing).
     *
     * @param empty if true, creates an empty board
     */
    private Board(boolean empty) {
        this.board = new Piece[8][8];
        this.piecePositions = new HashMap<>();
        this.lastDoubleMovePawn = null;
        if (!empty) {
            initializeBoard();
        }
    }

    /**
     * Initializes the board with pieces in standard starting positions.
     */
    private void initializeBoard() {
        // Initialize white pieces (bottom of board)
        placePiece(new Position(7, 0), PieceFactory.createPiece(PieceType.ROOK, Color.WHITE));
        placePiece(new Position(7, 1), PieceFactory.createPiece(PieceType.KNIGHT, Color.WHITE));
        placePiece(new Position(7, 2), PieceFactory.createPiece(PieceType.BISHOP, Color.WHITE));
        placePiece(new Position(7, 3), PieceFactory.createPiece(PieceType.QUEEN, Color.WHITE));
        placePiece(new Position(7, 4), PieceFactory.createPiece(PieceType.KING, Color.WHITE));
        placePiece(new Position(7, 5), PieceFactory.createPiece(PieceType.BISHOP, Color.WHITE));
        placePiece(new Position(7, 6), PieceFactory.createPiece(PieceType.KNIGHT, Color.WHITE));
        placePiece(new Position(7, 7), PieceFactory.createPiece(PieceType.ROOK, Color.WHITE));

        for (int i = 0; i < 8; i++) {
            placePiece(new Position(6, i), PieceFactory.createPiece(PieceType.PAWN, Color.WHITE));
        }

        // Initialize black pieces (top of board)
        placePiece(new Position(0, 0), PieceFactory.createPiece(PieceType.ROOK, Color.BLACK));
        placePiece(new Position(0, 1), PieceFactory.createPiece(PieceType.KNIGHT, Color.BLACK));
        placePiece(new Position(0, 2), PieceFactory.createPiece(PieceType.BISHOP, Color.BLACK));
        placePiece(new Position(0, 3), PieceFactory.createPiece(PieceType.QUEEN, Color.BLACK));
        placePiece(new Position(0, 4), PieceFactory.createPiece(PieceType.KING, Color.BLACK));
        placePiece(new Position(0, 5), PieceFactory.createPiece(PieceType.BISHOP, Color.BLACK));
        placePiece(new Position(0, 6), PieceFactory.createPiece(PieceType.KNIGHT, Color.BLACK));
        placePiece(new Position(0, 7), PieceFactory.createPiece(PieceType.ROOK, Color.BLACK));

        for (int i = 0; i < 8; i++) {
            placePiece(new Position(1, i), PieceFactory.createPiece(PieceType.PAWN, Color.BLACK));
        }
    }

    /**
     * Places a piece at the specified position.
     *
     * @param pos   the position to place the piece
     * @param piece the piece to place
     */
    public void placePiece(Position pos, Piece piece) {
        board[pos.getRow()][pos.getCol()] = piece;
        if (piece != null) {
            piecePositions.put(pos, piece);
        }
    }

    /**
     * Removes a piece from the specified position.
     *
     * @param pos the position to remove the piece from
     */
    public void removePiece(Position pos) {
        board[pos.getRow()][pos.getCol()] = null;
        piecePositions.remove(pos);
    }

    /**
     * Gets the piece at the specified position.
     *
     * @param pos the position to check
     * @return the piece at that position, or null if empty
     */
    public Piece getPiece(Position pos) {
        return board[pos.getRow()][pos.getCol()];
    }

    /**
     * Checks if a position is occupied by any piece.
     *
     * @param pos the position to check
     * @return true if a piece exists at that position
     */
    public boolean isOccupied(Position pos) {
        return getPiece(pos) != null;
    }

    /**
     * Checks if a position is occupied by a piece of the specified color.
     *
     * @param pos   the position to check
     * @param color the color to check for
     * @return true if a piece of the specified color exists at that position
     */
    public boolean isOccupiedBySameColor(Position pos, Color color) {
        Piece piece = getPiece(pos);
        return piece != null && piece.getColor() == color;
    }

    /**
     * Moves a piece from one position to another.
     * This is a low-level method that doesn't validate the move.
     *
     * @param from the starting position
     * @param to   the destination position
     */
    public void movePiece(Position from, Position to) {
        Piece piece = getPiece(from);
        if (piece != null) {
            // Remove captured piece if any
            removePiece(to);

            // Move the piece
            removePiece(from);
            placePiece(to, piece);
            piece.setMoved();

            // Track double pawn moves for en passant
            if (piece.getType() == PieceType.PAWN) {
                int rowDiff = Math.abs(to.getRow() - from.getRow());
                if (rowDiff == 2) {
                    lastDoubleMovePawn = to;
                } else {
                    lastDoubleMovePawn = null;
                }
            } else {
                lastDoubleMovePawn = null;
            }
        }
    }

    /**
     * Finds the position of the king of the specified color.
     *
     * @param color the color of the king to find
     * @return the position of the king, or null if not found
     */
    public Position findKing(Color color) {
        for (Map.Entry<Position, Piece> entry : piecePositions.entrySet()) {
            Piece piece = entry.getValue();
            if (piece.getType() == PieceType.KING && piece.getColor() == color) {
                return entry.getKey();
            }
        }
        return null;
    }

    /**
     * Gets all positions of pieces of the specified color.
     *
     * @param color the color to search for
     * @return list of positions containing pieces of that color
     */
    public List<Position> getAllPiecesOfColor(Color color) {
        List<Position> pieces = new ArrayList<>();
        for (Map.Entry<Position, Piece> entry : piecePositions.entrySet()) {
            if (entry.getValue().getColor() == color) {
                pieces.add(entry.getKey());
            }
        }
        return pieces;
    }

    /**
     * Gets the position of the last pawn that made a double move (for en passant).
     *
     * @return the position, or null if no recent double move
     */
    public Position getLastDoubleMovePawn() {
        return lastDoubleMovePawn;
    }

    /**
     * Sets the last double move pawn position (for en passant).
     *
     * @param position the position of the pawn
     */
    public void setLastDoubleMovePawn(Position position) {
        this.lastDoubleMovePawn = position;
    }

    /**
     * Creates a deep copy of this board.
     *
     * @return a new board with the same piece positions
     */
    public Board copy() {
        Board copy = new Board(true);
        for (Map.Entry<Position, Piece> entry : this.piecePositions.entrySet()) {
            Position pos = entry.getKey();
            Piece piece = entry.getValue().copy();
            copy.placePiece(pos, piece);
        }
        copy.lastDoubleMovePawn = this.lastDoubleMovePawn;
        return copy;
    }

    /**
     * Gets all piece positions on the board.
     *
     * @return map of positions to pieces
     */
    public Map<Position, Piece> getAllPieces() {
        return new HashMap<>(piecePositions);
    }
}

