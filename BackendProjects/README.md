# Backend Projects - Comprehensive Learning Repository

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![Maven](https://img.shields.io/badge/Maven-3.x-blue.svg)](https://maven.apache.org/)
[![GitHub](https://img.shields.io/badge/GitHub-Repository-green.svg)](https://github.com/SaurabhMaurya9695/BackendProjects)

A comprehensive Java-based learning repository focused on backend development concepts, design patterns, data structures & algorithms, and system design implementations.

## 🌟 Project Overview

This repository serves as a complete learning resource for backend development, containing implementations of:

- **Design Patterns**: All major behavioral, creational, and structural patterns
- **Design Principles**: Core software engineering principles
- **Data Structures & Algorithms**: Comprehensive DSA implementations
- **System Designs**: Real-world application architectures (Spotify, Tomato, Zepto, etc.)
- **Custom Tools**: XJC plugin for enhanced code generation

## 📋 Table of Contents

- [Project Structure](#project-structure)
- [Modules Overview](#modules-overview)
- [Getting Started](#getting-started)
- [Design Patterns](#design-patterns)
- [Data Structures & Algorithms](#data-structures--algorithms)
- [System Designs](#system-designs)
- [Build & Run](#build--run)
- [Contributing](#contributing)

## 🏗️ Project Structure

```
BackendProjects/
├── design-pattern/       # Design patterns implementations
├── design-principles/    # Software design principles
├── dsa/                 # Data structures & algorithms
├── entry-point/         # Main entry classes
├── xjc-plugin/          # Custom XJC plugin
├── dist/                # Distribution module
└── pom.xml              # Parent Maven configuration
```

## 📦 Modules Overview

### 🎨 Design Pattern Module (`design-pattern/`)

**Comprehensive collection of design patterns with real-world implementations:**

#### Behavioral Patterns
- **Chain of Responsibility**: Approval workflow system
- **Command Pattern**: Remote controller implementation with undo/redo
- **Observer Pattern**: Publisher-subscriber notification system
- **Strategy Pattern**: Flexible sorting algorithms
- **Template Method**: Neural network training framework

#### Creational Patterns
- **Abstract Factory**: Cross-platform UI components
- **Builder Pattern**: Complex object construction (Burger builder)
- **Factory Pattern**: Operating system abstraction
- **Prototype Pattern**: Vehicle registry with cloning
- **Singleton Pattern**: Multiple implementations (Eager, Lazy, Thread-safe, Enum-based)

#### Structural Patterns
- **Adapter Pattern**: Third-party service integration
- **Bridge Pattern**: Video streaming platform abstraction
- **Composite Pattern**: File system hierarchy
- **Decorator Pattern**: Pizza toppings and character enhancements
- **Facade Pattern**: Computer system simplification
- **Proxy Pattern**: Image loading optimization

### 🏛️ System Design Implementations (`design-pattern/designs/`)

**Real-world application architectures:**

1. **🎵 Spotify Music Player**
   - Strategy pattern for playback modes
   - Adapter pattern for audio devices
   - Factory pattern for device management
   - Facade pattern for application orchestration

2. **🍕 Tomato Food Ordering System**
   - Factory pattern for order types
   - Strategy pattern for payment methods
   - Observer pattern for notifications
   - Template method for order processing

3. **🛒 Zepto Grocery Delivery**
   - Inventory management system
   - Strategy pattern for delivery options
   - Factory pattern for store creation
   - Manager pattern for business logic

4. **💰 Payment Gateway System**
   - Strategy pattern for payment methods
   - Factory pattern for gateway creation
   - Bridge pattern for bank integration
   - Template method for transaction processing

5. **🎫 Discount Coupon System**
   - Chain of responsibility for coupon validation
   - Strategy pattern for discount calculation
   - Factory pattern for coupon types

6. **📄 Document Editor**
   - Command pattern for edit operations
   - Composite pattern for document structure
   - Observer pattern for change notifications

7. **🔔 Notification System**
   - Decorator pattern for message enhancement
   - Strategy pattern for delivery channels
   - Observer pattern for event handling
   - Bridge pattern for platform abstraction

### 🧮 Data Structures & Algorithms (`dsa/`)

**Comprehensive DSA implementations organized by categories:**

#### Arrays (`arrays/`)
- Basic operations and manipulations
- Maximum consecutive ones
- Missing number algorithms
- Array rotation and transformations
- Duplicate removal techniques

#### Dynamic Programming (`dynamicProgramming/`)
- **Stock Trading Problems**: 7 variations of buy/sell stock problems
- **Climbing Stairs**: Multiple constraint variations
- **Fibonacci Series**: Classic and advanced implementations
- **Kadane's Algorithm**: Maximum subarray problems

#### HashMap & Heaps (`hashmapAndHeaps/`)
- Custom HashMap implementation
- Priority queue operations
- Anagram detection algorithms
- Sliding window problems
- K-largest elements algorithms
- Median finder implementation

#### Trees (`trees/`)
- **Binary Trees**: 31 comprehensive implementations
  - Traversal algorithms (BFS, DFS, Iterative)
  - Tree views (Top, Bottom, Left, Right, Vertical)
  - Path finding algorithms
  - Tree transformation operations
  - Serialization/Deserialization
- **N-ary Trees**: 18 specialized implementations
  - Generic tree operations
  - Multi-solver implementations
  - Distance calculations

#### Recursion (`recursion/`)
- Factorial and power calculations
- Maze path finding
- Subset generation algorithms
- Permutation and combination generators
- Encoding/decoding problems

#### Sorting Algorithms (`sorting/`)
- Bubble Sort, Selection Sort, Insertion Sort
- Merge Sort, Quick Sort implementations

### 🎯 Entry Points (`entry-point/`)

**Organized main classes for different learning modules:**

- `EntryPoint.java`: Trees and N-ary tree demonstrations
- `EntryPointForDP.java`: Dynamic programming examples
- `EntryPointForDesignPrinciple.java`: Design principles showcase
- `EntryPointForHashMapAndHeap.java`: HashMap and heap operations
- `EntryPointRecursion.java`: Recursion problem solutions

### 🔧 XJC Plugin (`xjc-plugin/`)

**Custom Maven plugin for enhanced code generation:**

- **Rich Model Generation**: Automatically adds Lombok annotations
- **Serializable Support**: Implements Serializable interface
- **Builder Pattern**: Generates builder methods
- **Copy Constructors**: Deep copy functionality
- **Enhanced Equality**: Custom equals and hashCode methods

### 🎯 Design Principles (`design-principles/`)

**Core software engineering principles:**
- SOLID principles implementations
- DRY, KISS, YAGNI demonstrations
- Separation of concerns examples

## 🚀 Getting Started

### Prerequisites

- **Java 17** or higher
- **Maven 3.x**
- **Git**

### Installation

1. **Clone the repository:**
```bash
git clone https://github.com/SaurabhMaurya9695/BackendProjects.git
cd BackendProjects
```

2. **Build the project:**
```bash
mvn clean compile
```

3. **Run specific modules:**
```bash
# For design patterns
mvn exec:java -pl entry-point -Dexec.mainClass="com.backend.EntryPointForDP"

# For DSA demonstrations
mvn exec:java -pl entry-point -Dexec.mainClass="com.backend.EntryPoint"

# For design principles
mvn exec:java -pl entry-point -Dexec.mainClass="com.backend.EntryPointForDesignPrinciple"


## 🔧 Build & Development

### Maven Commands

```bash
# Clean and compile all modules
mvn clean compile

# Run tests
mvn test

# Package the application
mvn package

# Generate XJC classes (design-pattern module)
mvn generate-sources

# Create distribution
mvn clean package -pl dist
```

### Module-specific Operations

```bash
# Compile specific module
mvn compile -pl design-pattern

# Run specific entry point
mvn exec:java -pl entry-point -Dexec.mainClass="com.backend.EntryPoint"

# Generate JAXB classes with custom plugin
mvn clean generate-sources -pl design-pattern
```

## 📊 Project Statistics

- **Total Modules**: 6
- **Design Patterns**: 15+ patterns across 3 categories
- **System Designs**: 7 real-world applications
- **DSA Problems**: 100+ implementations
- **Tree Algorithms**: 49 different implementations
- **DP Problems**: 20+ variations
- **Lines of Code**: 10,000+

## 🎯 Learning Path Recommendations

### For Beginners:
1. Start with `design-principles` module
2. Explore basic design patterns in `design-pattern/creational`
3. Practice with simple DSA problems in `dsa/arrays`
4. Run entry point examples

### For Intermediate:
1. Study system design implementations in `designs/`
2. Explore behavioral and structural patterns
3. Tackle dynamic programming problems
4. Understand tree algorithms

### For Advanced:
1. Analyze complete system architectures
2. Study the XJC plugin implementation
3. Contribute new patterns or optimizations
4. Explore advanced DSA problems

## 📈 Key Features

- ✅ **Production-Ready Code**: All implementations follow best practices
- ✅ **Comprehensive Documentation**: Detailed comments and explanations
- ✅ **Real-World Examples**: Practical applications of theoretical concepts
- ✅ **Modular Architecture**: Clean separation of concerns
- ✅ **Extensible Design**: Easy to add new patterns or algorithms
- ✅ **Educational Focus**: Perfect for learning and teaching

## 🤝 Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/NewPattern`)
3. Commit your changes (`git commit -m 'Add new design pattern'`)
4. Push to the branch (`git push origin feature/NewPattern`)
5. Open a Pull Request

### Contribution Guidelines:
- Follow existing code style and structure
- Add comprehensive documentation
- Include unit tests where applicable
- Update README if adding new modules

## 📞 Contact

- **Author**: Saurabh Maurya
- **GitHub**: [SaurabhMaurya9695](https://github.com/SaurabhMaurya9695)
- **Repository**: [BackendProjects](https://github.com/SaurabhMaurya9695/BackendProjects)

## 📄 License

This project is open source and available under the [MIT License](LICENSE).

---

⭐ **Star this repository if it helped you learn backend development concepts!**

*Happy Learning! 🚀*
