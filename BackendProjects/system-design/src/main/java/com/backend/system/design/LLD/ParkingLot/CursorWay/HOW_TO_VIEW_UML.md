# 📊 How to View the UML Diagram

## 🌐 Online Method (Easiest & Recommended)

### Option 1: PlantUML Online Server (Best Quality)

1. **Open the UML file**
   - Navigate to: `CursorWay/ParkingLotSystemUML.puml`
   - Select all text (Ctrl+A / Cmd+A)
   - Copy (Ctrl+C / Cmd+C)

2. **Visit PlantUML Online Editor**
   ```
   🔗 http://www.plantuml.com/plantuml/uml/
   ```

3. **Paste and View**
   - Paste the code into the text area
   - The diagram will render automatically
   - Use zoom controls to explore details

4. **Download Options**
   - PNG: High-quality image
   - SVG: Vector graphics (best for documentation)
   - PDF: For printing

### Option 2: PlantText (Alternative)

1. **Visit PlantText**
   ```
   🔗 https://www.planttext.com/
   ```

2. **Paste the UML code**
   - Clear the existing example
   - Paste your code
   - Click "Refresh" button

---

## 💻 IDE Integration (For Developers)

### VS Code (Recommended)

1. **Install Extension**
   - Open Extensions (Ctrl+Shift+X)
   - Search: "PlantUML"
   - Install: "PlantUML" by jebbs

2. **View Diagram**
   - Open `ParkingLotSystemUML.puml`
   - Press `Alt+D` (Windows/Linux) or `Option+D` (Mac)
   - Preview appears in split view

3. **Export**
   - Right-click in preview
   - Select "Export Current Diagram"
   - Choose format (PNG, SVG, PDF)

### IntelliJ IDEA

1. **Install Plugin**
   - File → Settings → Plugins
   - Search: "PlantUML integration"
   - Install and restart

2. **View Diagram**
   - Open `ParkingLotSystemUML.puml`
   - Right-click → "Show PlantUML Diagram"
   - Or use the PlantUML toolbar

3. **Export**
   - Right-click in diagram view
   - Select export format

### Eclipse

1. **Install Plugin**
   - Help → Eclipse Marketplace
   - Search: "PlantUML"
   - Install and restart

2. **View Diagram**
   - Open `.puml` file
   - Diagram auto-renders in editor

---

## 🖥️ Local Installation (Advanced)

### macOS

```bash
# Using Homebrew
brew install plantuml

# Generate diagram
cd CursorWay
plantuml ParkingLotSystemUML.puml

# Output: ParkingLotSystemUML.png
```

### Linux (Ubuntu/Debian)

```bash
# Install dependencies
sudo apt-get update
sudo apt-get install plantuml

# Generate diagram
cd CursorWay
plantuml ParkingLotSystemUML.puml
```

### Windows

1. **Install Java** (if not already installed)
   ```
   🔗 https://www.java.com/download/
   ```

2. **Download PlantUML**
   ```
   🔗 https://plantuml.com/download
   ```

3. **Generate Diagram**
   ```cmd
   cd CursorWay
   java -jar plantuml.jar ParkingLotSystemUML.puml
   ```

---

## 🎨 What You'll See in the Diagram

### Layer 1: Enums (Yellow)
- `VehicleType` (BIKE, CAR, TRUCK)
- `SpotSize` (SMALL, MEDIUM, LARGE)
- `SpotStatus` (AVAILABLE, OCCUPIED, etc.)
- `PaymentStatus` (PENDING, COMPLETED, etc.)
- `PaymentMethod` (CASH, CREDIT_CARD, UPI)

### Layer 2: Domain Models
- **Vehicle Domain** (Green)
  - Abstract `Vehicle` class
  - `Bike`, `Car`, `Truck` subclasses
  
- **Parking Domain** (Pink)
  - `ParkingSpot`
  - `ParkingTicket`

### Layer 3: Strategy Patterns
- **Pricing Strategy** (Blue)
  - Interface: `PricingStrategy`
  - Implementations: `HourlyPricingStrategy`, `FlatRatePricingStrategy`
  
- **Payment Strategy** (Light Pink)
  - Interface: `PaymentStrategy`
  - Implementations: `CashPayment`, `CreditCardPayment`, `UPIPayment`

### Layer 4: Managers (Light Blue)
- `ParkingSpotManager` - Manages all parking spots
- `TicketManager` - Manages ticket lifecycle
- `PaymentManager` - Handles payment processing

### Layer 5: System Facade (Cyan)
- `ParkingLotSystem` - Main system entry point
- Singleton + Facade pattern
- Coordinates all operations

### Visual Elements
- 🎯 **Color-coded** by component type
- 📐 **Clear relationships** (inheritance, composition, dependency)
- 📝 **Detailed notes** explaining design patterns
- 🏆 **SOLID principles** annotations
- 📊 **Legend** explaining symbols and patterns

---

## 💡 Tips for Better Viewing

### For Large Diagrams

1. **Zoom Controls**
   - Use browser/IDE zoom
   - SVG format for infinite zoom
   - Focus on one package at a time

2. **Print Layout**
   - Export as PDF
   - Use landscape orientation
   - Scale to fit page

3. **Split View**
   - Open UML source and diagram side-by-side
   - Use IDE split editor

### For Presentations

1. **Export as SVG**
   - Best quality for slides
   - Scales without pixelation

2. **Highlight Sections**
   - Take screenshots of specific layers
   - Focus on one pattern at a time

3. **Add to Documentation**
   - Embed in README
   - Link in design docs

---

## 🎯 Diagram Highlights

### Design Patterns Visualized
✅ **Strategy Pattern** - Clear interface → implementation arrows  
✅ **Singleton Pattern** - Static instance, private constructor  
✅ **Facade Pattern** - Aggregates all managers  
✅ **Template Method** - Abstract Vehicle with concrete implementations  

### SOLID Principles
✅ **SRP** - Each class has one responsibility  
✅ **OCP** - Easy to extend (new vehicle types, strategies)  
✅ **LSP** - Subclasses properly substitute parents  
✅ **ISP** - Small, focused interfaces  
✅ **DIP** - Depends on abstractions (interfaces)  

### Relationships
- **Solid lines** = Strong association/composition
- **Dashed lines** = Dependency/usage
- **Triangular arrows** = Inheritance/implementation
- **Diamond shapes** = Composition/aggregation

---

## 🔧 Troubleshooting

### Diagram Not Rendering?
- ✅ Check if PlantUML syntax is valid
- ✅ Ensure Java is installed (for local rendering)
- ✅ Try online viewer first (no dependencies)

### Text Too Small?
- ✅ Export as SVG and zoom in
- ✅ Increase diagram size in settings
- ✅ View on larger screen

### Missing Elements?
- ✅ Scroll or zoom out to see full diagram
- ✅ Check if packages are collapsed
- ✅ View source code to verify all elements

---

## 📚 Additional Resources

- **PlantUML Documentation**: https://plantuml.com/
- **Class Diagram Guide**: https://plantuml.com/class-diagram
- **UML Basics**: https://www.uml-diagrams.org/

---

## ✨ Quick Reference

| Action | Command |
|--------|---------|
| View Online | http://www.plantuml.com/plantuml/uml/ |
| VS Code Preview | `Alt+D` / `Option+D` |
| Export PNG | Right-click → Export → PNG |
| Export SVG | Right-click → Export → SVG |
| Local Generate | `plantuml ParkingLotSystemUML.puml` |

---

**🎉 Enjoy exploring the comprehensive UML diagram of your CursorWay Parking Lot System!**
