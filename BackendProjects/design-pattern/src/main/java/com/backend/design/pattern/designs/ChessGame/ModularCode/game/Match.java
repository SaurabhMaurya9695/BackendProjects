package com.backend.design.pattern.designs.ChessGame.ModularCode.game;

import com.backend.design.pattern.designs.ChessGame.ModularCode.board.Board;
import com.backend.design.pattern.designs.ChessGame.ModularCode.board.BoardDisplay;
import com.backend.design.pattern.designs.ChessGame.ModularCode.chat.ChatMediator;
import com.backend.design.pattern.designs.ChessGame.ModularCode.chat.Message;
import com.backend.design.pattern.designs.ChessGame.ModularCode.exception.*;
import com.backend.design.pattern.designs.ChessGame.ModularCode.model.*;
import com.backend.design.pattern.designs.ChessGame.ModularCode.moves.MoveHistory;
import com.backend.design.pattern.designs.ChessGame.ModularCode.pieces.Piece;
import com.backend.design.pattern.designs.ChessGame.ModularCode.rules.ChessRules;
import com.backend.design.pattern.designs.ChessGame.ModularCode.rules.StandardChessRules;
import com.backend.design.pattern.designs.ChessGame.ModularCode.user.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a chess match between two players.
 * Implements ChatMediator to handle communication between players.
 * Follows Single Responsibility Principle by delegating specific tasks to other classes.
 */
public class Match implements ChatMediator {

    private final String matchId;
    private final User whitePlayer;
    private final User blackPlayer;
    private final Board board;
    private final ChessRules rules;
    private final MoveHistory moveHistory;
    private final List<Message> chatHistory;
    private final List<GameEventListener> eventListeners;

    private Color currentTurn;
    private GameStatus status;

    /**
     * Creates a new chess match between two players.
     *
     * @param matchId     unique identifier for this match
     * @param whitePlayer player playing as white
     * @param blackPlayer player playing as black
     */
    public Match(String matchId, User whitePlayer, User blackPlayer) {
        this.matchId = matchId;
        this.whitePlayer = whitePlayer;
        this.blackPlayer = blackPlayer;
        this.board = new Board();
        this.rules = new StandardChessRules();
        this.moveHistory = new MoveHistory();
        this.chatHistory = new ArrayList<>();
        this.eventListeners = new ArrayList<>();
        this.currentTurn = Color.WHITE;
        this.status = GameStatus.IN_PROGRESS;

        // Set this match as mediator for both players
        whitePlayer.setMediator(this);
        blackPlayer.setMediator(this);

        System.out.println("Match " + matchId + " started: " +
                whitePlayer.getName() + " (White) vs " + blackPlayer.getName() + " (Black)");
    }

    /**
     * Attempts to make a move in the game.
     *
     * @param from   starting position
     * @param to     destination position
     * @param player the player making the move
     * @throws ChessException if the move is invalid or not allowed
     */
    public void makeMove(Position from, Position to, User player) throws ChessException {
        validateGameInProgress();
        validatePlayerTurn(player);

        Piece piece = board.getPiece(from);
        validatePieceSelection(piece, player);

        Move move = new Move(from, to, piece, board.getPiece(to));

        if (!rules.isValidMove(move, board)) {
            throw new InvalidMoveException("Invalid move from " + from + " to " + to);
        }

        // Execute the move
        board.movePiece(from, to);
        moveHistory.addMove(move);

        // Notify listeners
        notifyMoveMade(move);

        System.out.println(player.getName() + " moved " + piece.getSymbol() +
                " from " + from + " to " + to);

        // Check game end conditions
        Color opponentColor = currentTurn.opposite();
        if (rules.isCheckmate(opponentColor, board)) {
            endGame(player, "checkmate");
        } else if (rules.isStalemate(opponentColor, board)) {
            endGame(null, "stalemate");
        } else {
            // Switch turn
            currentTurn = opponentColor;

            // Check if opponent is in check
            if (rules.isInCheck(opponentColor, board)) {
                System.out.println(getPlayerByColor(opponentColor).getName() + " is in check!");
                notifyCheck(opponentColor);
            }
        }
    }

    /**
     * Allows a player to quit/resign from the game.
     *
     * @param player the player who is quitting
     */
    public void quitGame(User player) {
        User opponent = (player.equals(whitePlayer)) ? blackPlayer : whitePlayer;
        endGame(opponent, "resignation");
        player.decrementScore(50); // Penalty for quitting
        notifyPlayerQuit(player.getName());
        System.out.println(player.getName() + " resigned. Score decreased by 50.");
    }

    /**
     * Ends the game with a result.
     *
     * @param winner the winning player, or null for a draw
     * @param reason the reason for game end
     */
    private void endGame(User winner, String reason) {
        status = GameStatus.COMPLETED;

        GameResult result;
        if (winner != null) {
            User loser = (winner.equals(whitePlayer)) ? blackPlayer : whitePlayer;
            winner.incrementScore(30);
            loser.decrementScore(20);

            result = winner.equals(whitePlayer) ? GameResult.WHITE_WINS : GameResult.BLACK_WINS;

            System.out.println("Game ended - " + winner.getName() + " wins by " + reason + "!");
            System.out.println("Score update: " + winner.getName() + " +30, " + loser.getName() + " -20");
        } else {
            result = GameResult.DRAW;
            System.out.println("Game ended in " + reason + "! No score change.");
        }

        notifyGameEnd(result, reason);
    }

    /**
     * Validates that the game is in progress.
     *
     * @throws GameNotInProgressException if game is not in progress
     */
    private void validateGameInProgress() throws GameNotInProgressException {
        if (status != GameStatus.IN_PROGRESS) {
            throw new GameNotInProgressException("Game is not in progress!");
        }
    }

    /**
     * Validates that it's the player's turn.
     *
     * @param player the player attempting to move
     * @throws NotYourTurnException if it's not the player's turn
     */
    private void validatePlayerTurn(User player) throws NotYourTurnException {
        Color playerColor = getPlayerColor(player);
        if (playerColor != currentTurn) {
            throw new NotYourTurnException("It's not your turn!");
        }
    }

    /**
     * Validates piece selection.
     *
     * @param piece  the selected piece
     * @param player the player selecting the piece
     * @throws InvalidPieceSelectionException if piece selection is invalid
     */
    private void validatePieceSelection(Piece piece, User player) throws InvalidPieceSelectionException {
        if (piece == null) {
            throw new InvalidPieceSelectionException("No piece at the selected position!");
        }

        Color playerColor = getPlayerColor(player);
        if (piece.getColor() != playerColor) {
            throw new InvalidPieceSelectionException("You can only move your own pieces!");
        }
    }

    /**
     * Gets the color of the specified player.
     *
     * @param player the player
     * @return the player's color
     */
    public Color getPlayerColor(User player) {
        return player.equals(whitePlayer) ? Color.WHITE : Color.BLACK;
    }

    /**
     * Gets the player of the specified color.
     *
     * @param color the color
     * @return the player
     */
    public User getPlayerByColor(Color color) {
        return color == Color.WHITE ? whitePlayer : blackPlayer;
    }

    /**
     * Displays the current board state.
     */
    public void displayBoard() {
        BoardDisplay.display(board);
    }

    // ChatMediator implementation

    @Override
    public void sendMessage(Message message, User sender) {
        chatHistory.add(message);
        User recipient = sender.equals(whitePlayer) ? blackPlayer : whitePlayer;
        recipient.receive(message);
        System.out.println("Chat in match " + matchId + " - " + message);
    }

    @Override
    public void addUser(User user) {
        // Not applicable for chess match (always 2 users)
    }

    @Override
    public void removeUser(User user) {
        quitGame(user);
    }

    // Event listener management

    /**
     * Adds an event listener to this match.
     *
     * @param listener the listener to add
     */
    public void addEventListener(GameEventListener listener) {
        eventListeners.add(listener);
    }

    /**
     * Removes an event listener from this match.
     *
     * @param listener the listener to remove
     */
    public void removeEventListener(GameEventListener listener) {
        eventListeners.remove(listener);
    }

    private void notifyMoveMade(Move move) {
        for (GameEventListener listener : eventListeners) {
            listener.onMoveMade(move, matchId);
        }
    }

    private void notifyCheck(Color color) {
        for (GameEventListener listener : eventListeners) {
            listener.onCheck(color, matchId);
        }
    }

    private void notifyGameEnd(GameResult result, String reason) {
        for (GameEventListener listener : eventListeners) {
            listener.onGameEnd(result, reason, matchId);
        }
    }

    private void notifyPlayerQuit(String playerName) {
        for (GameEventListener listener : eventListeners) {
            listener.onPlayerQuit(playerName, matchId);
        }
    }

    // Getters

    public String getMatchId() {
        return matchId;
    }

    public User getWhitePlayer() {
        return whitePlayer;
    }

    public User getBlackPlayer() {
        return blackPlayer;
    }

    public Board getBoard() {
        return board;
    }

    public GameStatus getStatus() {
        return status;
    }

    public Color getCurrentTurn() {
        return currentTurn;
    }

    public MoveHistory getMoveHistory() {
        return moveHistory;
    }

    public List<Message> getChatHistory() {
        return new ArrayList<>(chatHistory);
    }
}

