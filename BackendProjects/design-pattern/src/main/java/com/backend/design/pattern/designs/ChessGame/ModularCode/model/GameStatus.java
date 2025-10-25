package com.backend.design.pattern.designs.ChessGame.ModularCode.model;

/**
 * Represents the current status of a chess game.
 */
public enum GameStatus {
    /** Game is waiting for players to join */
    WAITING,

    /** Game is currently in progress */
    IN_PROGRESS,

    /** Game has completed normally */
    COMPLETED,

    /** Game was aborted or cancelled */
    ABORTED
}

