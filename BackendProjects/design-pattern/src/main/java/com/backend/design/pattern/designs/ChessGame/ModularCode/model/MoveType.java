package com.backend.design.pattern.designs.ChessGame.ModularCode.model;

/**
 * Represents the type of chess move being made.
 * Used to identify special moves that require different handling.
 */
public enum MoveType {
    /** Normal piece move */
    NORMAL,

    /** Capture move */
    CAPTURE,

    /** Castling (king and rook special move) */
    CASTLING,

    /** En passant capture (special pawn capture) */
    EN_PASSANT,

    /** Pawn promotion (pawn reaches opposite end) */
    PROMOTION
}

