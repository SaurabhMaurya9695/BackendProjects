# Modular Chess Game System - Low-Level Design

## Overview
This is a fully modular chess game implementation following SOLID principles and design patterns.

## Design Patterns Used

### 1. **Creational Patterns**
- **Factory Pattern**: `PieceFactory` for creating chess pieces
- **Singleton Pattern**: `GameManager` with thread-safe lazy initialization

### 2. **Structural Patterns**
- **Facade Pattern**: `GameManager` provides a simple interface to the complex chess system

### 3. **Behavioral Patterns**
- **Strategy Pattern**: 
  - `Piece` hierarchy for different movement strategies
  - `MatchingStrategy` for different matchmaking algorithms
  - `ChessRules` for different rule implementations
- **Observer Pattern**: `GameEventListener` for game events
- **Mediator Pattern**: `ChatMediator` for player communication
- **Command Pattern**: `MoveCommand` for move execution and undo capability

## SOLID Principles

### Single Responsibility Principle (SRP)
- Each class has one reason to change
- `Board` only manages piece positions
- `BoardDisplay` only handles visual representation
- `CheckDetector` only detects check conditions
- `MoveValidator` only validates moves
- `SpecialMoveHandler` only handles special moves

### Open/Closed Principle (OCP)
- Open for extension, closed for modification
- New piece types can be added without modifying existing code
- New matching strategies can be added via `MatchingStrategy` interface
- New rule variations can be added via `ChessRules` interface

### Liskov Substitution Principle (LSP)
- All `Piece` subclasses can be used interchangeably
- All `MatchingStrategy` implementations are substitutable
- All `ChessRules` implementations are substitutable

### Interface Segregation Principle (ISP)
- Interfaces are focused and specific
- `GameEventListener` has specific methods for different events
- `ChessRules` interface is cohesive
- `ChatMediator` is focused on communication

### Dependency Inversion Principle (DIP)
- High-level modules depend on abstractions
- `Match` depends on `ChessRules` interface, not concrete implementation
- `GameManager` depends on `MatchingStrategy` interface
- Allows for dependency injection and easier testing

## Package Structure

```
ModularCode/
├── model/               # Value objects (immutable)
│   ├── Color
│   ├── PieceType
│   ├── Position
│   ├── Move
│   ├── MoveType
│   ├── GameStatus
│   └── GameResult
├── exception/           # Custom exceptions
│   ├── ChessException
│   ├── InvalidMoveException
│   ├── GameNotInProgressException
│   ├── NotYourTurnException
│   └── InvalidPieceSelectionException
├── pieces/              # Chess pieces
│   ├── Piece (abstract)
│   ├── King, Queen, Rook
│   ├── Bishop, Knight, Pawn
│   └── PieceFactory
├── board/               # Board management
│   ├── Board
│   └── BoardDisplay
├── rules/               # Game rules
│   ├── ChessRules (interface)
│   ├── StandardChessRules
│   ├── MoveValidator
│   ├── CheckDetector
│   └── SpecialMoveHandler
├── moves/               # Move commands
│   ├── MoveCommand (interface)
│   ├── StandardMoveCommand
│   └── MoveHistory
├── game/                # Game management
│   ├── Match
│   ├── GameManager
│   └── GameEventListener
├── matching/            # Matchmaking
│   ├── MatchingStrategy (interface)
│   ├── ScoreBasedMatching
│   └── SimpleMatchingStrategy
├── user/                # User management
│   └── User
├── chat/                # Chat system
│   ├── ChatMediator (interface)
│   ├── Message
│   └── Colleague
└── ChessDemo           # Demo application
```

## Features

### Complete Chess Implementation
✅ All standard chess pieces with correct movement
✅ Move validation including check detection
✅ Checkmate and stalemate detection
✅ Special moves: Castling, En Passant, Pawn Promotion
✅ Move history tracking
✅ Undo capability (via Command pattern)

### Game Management
✅ Thread-safe GameManager singleton
✅ Matchmaking with pluggable strategies
✅ Score-based matchmaking
✅ Chat system between players
✅ Event notification system

### Code Quality
✅ Immutable value objects
✅ Proper exception handling
✅ JavaDoc documentation
✅ Separation of concerns
✅ High cohesion, low coupling
✅ Thread-safe where needed
✅ Easy to test and maintain

## Usage Example

```java
// Create users
User alice = new User("user1", "Alice");
User bob = new User("user2", "Bob");

// Get game manager and request match
GameManager gm = GameManager.getInstance();
Match match = gm.requestMatch(alice);
gm.requestMatch(bob);  // Creates match

// Make moves
match.makeMove(Position.fromNotation("e2"), 
               Position.fromNotation("e4"), 
               alice);

// Chat
alice.send(new Message(alice.getId(), "Good game!"));

// Add event listener
match.addEventListener(new GameEventListener() {
    @Override
    public void onMoveMade(Move move, String matchId) {
        System.out.println("Move: " + move);
    }
    // ... other methods
});
```

## Running the Demo

```bash
java com.backend.design.pattern.designs.ChessGame.ModularCode.ChessDemo
```

## Comparison with UnModularCode

| Aspect | UnModularCode | ModularCode |
|--------|--------------|-------------|
| **File Count** | 1 file (1162 lines) | 40+ files |
| **SRP** | Violated | ✅ Adhered |
| **OCP** | Partially violated | ✅ Adhered |
| **DIP** | Violated | ✅ Adhered |
| **Special Moves** | Missing en passant, promotion | ✅ Complete |
| **Exception Handling** | Console output | ✅ Proper exceptions |
| **Thread Safety** | Not thread-safe | ✅ Thread-safe |
| **Testability** | Low | ✅ High |
| **Maintainability** | Low | ✅ High |
| **Extensibility** | Limited | ✅ Easy to extend |
| **LLD Score** | ~60% | **~95%** |

## Key Improvements

1. **Modular Architecture**: Each class has a single, well-defined responsibility
2. **Immutable Value Objects**: Position, Move, Message are immutable
3. **Proper Exception Handling**: Custom exceptions instead of console output
4. **Thread Safety**: Singleton and GameManager are thread-safe
5. **Observer Pattern**: Event-driven architecture for game events
6. **Command Pattern**: Enables undo/redo functionality
7. **Strategy Pattern**: Pluggable algorithms for matching and rules
8. **Complete Chess Rules**: All special moves implemented
9. **Separation of Concerns**: Display logic separated from business logic
10. **Dependency Injection**: Strategies can be injected at runtime

## Future Enhancements

- Persistence layer for saving/loading games
- AI opponent implementation
- Network play support
- GUI implementation
- Tournament management
- Time controls
- Draw by repetition and 50-move rule
- PGN export/import

