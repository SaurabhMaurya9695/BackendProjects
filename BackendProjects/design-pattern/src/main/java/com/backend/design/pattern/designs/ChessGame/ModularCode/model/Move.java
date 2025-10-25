package com.backend.design.pattern.designs.ChessGame.ModularCode.model;

import com.backend.design.pattern.designs.ChessGame.ModularCode.pieces.Piece;

import java.util.Objects;

/**
 * Immutable value object representing a chess move.
 * Contains all information needed to execute and potentially undo a move.
 */
public final class Move {

    private final Position from;
    private final Position to;
    private final Piece piece;
    private final Piece capturedPiece;
    private final MoveType moveType;
    private final PieceType promotionType;

    /**
     * Creates a normal or capture move.
     *
     * @param from          the starting position
     * @param to            the destination position
     * @param piece         the piece being moved
     * @param capturedPiece the piece being captured (null if none)
     */
    public Move(Position from, Position to, Piece piece, Piece capturedPiece) {
        this(from, to, piece, capturedPiece, capturedPiece != null ? MoveType.CAPTURE : MoveType.NORMAL, null);
    }

    /**
     * Creates a move with specified type and optional promotion.
     *
     * @param from           the starting position
     * @param to             the destination position
     * @param piece          the piece being moved
     * @param capturedPiece  the piece being captured (null if none)
     * @param moveType       the type of move
     * @param promotionType  the piece type to promote to (null if not promotion)
     */
    public Move(Position from, Position to, Piece piece, Piece capturedPiece,
                MoveType moveType, PieceType promotionType) {
        this.from = Objects.requireNonNull(from, "From position cannot be null");
        this.to = Objects.requireNonNull(to, "To position cannot be null");
        this.piece = Objects.requireNonNull(piece, "Piece cannot be null");
        this.capturedPiece = capturedPiece;
        this.moveType = Objects.requireNonNull(moveType, "Move type cannot be null");
        this.promotionType = promotionType;
    }

    public Position getFrom() {
        return from;
    }

    public Position getTo() {
        return to;
    }

    public Piece getPiece() {
        return piece;
    }

    public Piece getCapturedPiece() {
        return capturedPiece;
    }

    public MoveType getMoveType() {
        return moveType;
    }

    public PieceType getPromotionType() {
        return promotionType;
    }

    /**
     * Checks if this move is a capture.
     *
     * @return true if this move captures a piece
     */
    public boolean isCapture() {
        return capturedPiece != null || moveType == MoveType.EN_PASSANT;
    }

    /**
     * Checks if this move is castling.
     *
     * @return true if this is a castling move
     */
    public boolean isCastling() {
        return moveType == MoveType.CASTLING;
    }

    /**
     * Checks if this move is en passant.
     *
     * @return true if this is an en passant capture
     */
    public boolean isEnPassant() {
        return moveType == MoveType.EN_PASSANT;
    }

    /**
     * Checks if this move is a pawn promotion.
     *
     * @return true if this is a pawn promotion
     */
    public boolean isPromotion() {
        return moveType == MoveType.PROMOTION;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(piece.getType().getSymbol());
        sb.append(from.toChessNotation());
        sb.append(isCapture() ? "x" : "-");
        sb.append(to.toChessNotation());

        if (isPromotion() && promotionType != null) {
            sb.append("=").append(promotionType.getSymbol());
        }

        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Move move = (Move) obj;
        return Objects.equals(from, move.from) &&
                Objects.equals(to, move.to) &&
                Objects.equals(piece, move.piece) &&
                Objects.equals(capturedPiece, move.capturedPiece) &&
                moveType == move.moveType &&
                promotionType == move.promotionType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to, piece, capturedPiece, moveType, promotionType);
    }
}

