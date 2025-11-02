# How to View the TicTacToe UML Diagram

## 🖼️ Quick View Options

### Option 1: Online Viewer (EASIEST - No Installation) ⭐

1. **Visit**: https://www.plantuml.com/plantuml/uml/
2. **Open** `TicTacToe_UML.puml` in any text editor
3. **Copy** all the content
4. **Paste** into the website
5. **Click** Submit or press Enter
6. **See** the diagram instantly!

### Option 2: VS Code (Recommended for Developers)

1. **Install** the extension: "PlantUML" by jebbs
2. **Open** `TicTacToe_UML.puml`
3. **Press** `Alt + D` (Windows/Linux) or `Option + D` (Mac)
4. **View** the rendered diagram in split view

### Option 3: IntelliJ IDEA

1. **Install** plugin: "PlantUML integration"
2. **Open** `TicTacToe_UML.puml`
3. **Diagram** renders automatically in the side panel

### Option 4: Generate PNG Image

```bash
# Install PlantUML (one time)
brew install plantuml  # Mac
# or
sudo apt install plantuml  # Linux

# Generate PNG image
cd /path/to/TIcTacToe/folder
plantuml TicTacToe_UML.puml

# This creates: TicTacToe_UML.png
# Open the PNG file with any image viewer
```

---

## 📋 What You'll See

The UML diagram shows:

✅ **All 13 Classes** organized by package
- model/ (Board, Player, Symbol, Move, GameState)
- Strategy/ (WinStrategy, StandardWinStrategy)
- Notifications/ (GameObserver, GameSubject, ConsoleGameObserver)
- Game classes (TicTacToeGame, GameController, Client)

✅ **All Relationships**
- Inheritance (Strategy implements WinStrategy)
- Composition (Game contains Board)
- Dependencies (Game uses Strategy)

✅ **Design Patterns Highlighted**
- Strategy Pattern (blue)
- Observer Pattern (green)
- Color-coded by responsibility

✅ **SOLID Principles Legend**
- Shows how each principle is implemented

---

## 🎯 Understanding the Diagram

### Colors
- 🔵 **Blue** = Strategy Pattern classes
- 🟢 **Green** = Observer Pattern classes
- 🟡 **Wheat** = Model/Domain classes
- 🟡 **Yellow** = Controller classes
- 🔴 **Coral** = Client class

### Arrows
- `◄──` = Interface implementation
- `◄──` = Composition (strong)
- `◄- -` = Dependency (weak)

### Key Areas
- **Top**: Client (creates everything)
- **Middle**: Game Engine (orchestrates)
- **Bottom**: Models & Patterns (building blocks)

---

## 📸 Quick Screenshot Method

If you just want a quick look:

1. Go to: https://www.plantuml.com/plantuml/uml/
2. Paste the PlantUML code
3. Right-click on the diagram
4. "Save image as..." → Save as PNG
5. Open PNG with any image viewer

---

## ✅ Best Method

**For Quick View**: Use online PlantUML viewer (no installation needed)

**For Development**: Install VS Code extension (can view anytime)

---

## 🎓 The Diagram Shows

- How **Client** creates all dependencies
- How **TicTacToeGame** uses Strategy and Observer patterns
- How **Board** manages the game state
- How **Observer** pattern enables opponent notifications
- All class relationships and interactions

Perfect for understanding the architecture at a glance! 🎯



