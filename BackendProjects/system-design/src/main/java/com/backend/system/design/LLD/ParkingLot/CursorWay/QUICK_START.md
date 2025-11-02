# 🚀 Quick Start Guide - Parking Lot System

## Files Created

### 📁 Project Structure
```
CursorWay/
├── 📄 README.md                          # Complete documentation
├── 📄 QUICK_START.md                     # This file
├── 📄 HOW_TO_VIEW_UML.md                 # UML viewing instructions
├── 📄 ParkingLotSystemUML.puml           # PlantUML diagram
│
├── enums/
│   ├── VehicleType.java                  # Vehicle type enumeration
│   ├── SpotSize.java                     # Parking spot sizes
│   ├── SpotStatus.java                   # Spot availability status
│   ├── PaymentStatus.java                # Payment states
│   └── PaymentMethod.java                # Payment methods
│
├── models/
│   ├── Vehicle.java                      # Abstract vehicle class
│   ├── Bike.java                         # Bike implementation
│   ├── Car.java                          # Car implementation
│   ├── Truck.java                        # Truck implementation
│   ├── ParkingSpot.java                  # Parking spot entity
│   └── ParkingTicket.java                # Parking ticket entity
│
├── strategy/
│   ├── pricing/
│   │   ├── PricingStrategy.java          # Pricing interface
│   │   ├── HourlyPricingStrategy.java    # Hourly pricing
│   │   └── FlatRatePricingStrategy.java  # Flat rate pricing
│   └── payment/
│       ├── PaymentStrategy.java          # Payment interface
│       ├── CashPayment.java              # Cash payment
│       ├── CreditCardPayment.java        # Credit card payment
│       └── UPIPayment.java               # UPI payment
│
├── managers/
│   ├── ParkingSpotManager.java           # Manages parking spots
│   ├── TicketManager.java                # Manages tickets
│   └── PaymentManager.java               # Manages payments
│
├── ParkingLotSystem.java                 # Main system (Facade + Singleton)
└── ParkingLotDemo.java                   # Demonstration & test cases
```

**Total Files**: 25+ Java files + 4 documentation files

---

## 🏃‍♂️ Run the Demo (3 Simple Steps)

### Step 1: Navigate to the directory
```bash
cd system-design/src/main/java
```

### Step 2: Compile all files
```bash
javac com/backend/system/design/LLD/ParkingLot/CursorWay/**/*.java
```

### Step 3: Run the demo
```bash
java com.backend.system.design.LLD.ParkingLot.CursorWay.ParkingLotDemo
```

---

## 📋 What the Demo Shows

The automated demo demonstrates **ALL 9 requirements**:

### ✅ Requirement Coverage

| # | Requirement | Demo Scenario |
|---|-------------|---------------|
| 1 | Multiple parking slots | Initializes parking lot with 48 spots across 2 floors |
| 2 | Different vehicle sizes | Parks Bike (small), Car (medium), Truck (large) |
| 3 | Ticket upon entry | Each vehicle receives unique ticket with ID |
| 4 | Price calculation | Calculates fees based on vehicle type and duration |
| 5 | Payment before exit | Prevents exit without payment |
| 6 | Multiple payment methods | Demonstrates Cash, UPI, Credit Card payments |
| 7 | Slot freed after exit | Shows spot becomes available after vehicle exits |
| 8 | Can't park on occupied | Attempts to park same vehicle twice (fails) |
| 9 | Can't exit without payment | Shows payment validation before exit |

---

## 💻 Code Examples

### Example 1: Basic Usage
```java
// Initialize system
ParkingLotSystem parkingLot = ParkingLotSystem.getInstance(
    "My Parking Lot", 
    new HourlyPricingStrategy()
);

// Initialize parking spots
parkingLot.initializeParkingSpots(5, 10, 3, 2);  // 5 small, 10 medium, 3 large, 2 floors

// Create and park a vehicle
Vehicle car = new Car("ABC123", "John Doe");
Optional<ParkingTicket> ticket = parkingLot.parkVehicle(car);

// Process payment and exit
if (ticket.isPresent()) {
    String ticketId = ticket.get().getTicketId();
    PaymentStrategy payment = new CashPayment(100.0);
    parkingLot.processPayment(ticketId, payment);
    parkingLot.unparkVehicle(ticketId, null);
}
```

### Example 2: Check Fee Before Payment
```java
// Display fee details
parkingLot.displayFeeDetails(ticketId);

// Get fee amount
double fee = parkingLot.getParkingFee(ticketId);
System.out.println("Fee: ₹" + fee);
```

### Example 3: Different Payment Methods
```java
// Cash
PaymentStrategy cash = new CashPayment(100.0);

// UPI
PaymentStrategy upi = new UPIPayment("user@upi");

// Credit Card
PaymentStrategy card = new CreditCardPayment(
    "1234567812345678", 
    "John Doe", 
    "12/25", 
    "123"
);

// Process
parkingLot.processPayment(ticketId, cash);
```

---

## 🎨 Design Patterns Used

| Pattern | Location | Purpose |
|---------|----------|---------|
| **Strategy** | `strategy/pricing/*` | Different pricing algorithms |
| **Strategy** | `strategy/payment/*` | Different payment methods |
| **Singleton** | `ParkingLotSystem` | Single instance of parking lot |
| **Facade** | `ParkingLotSystem` | Simplified interface to subsystems |
| **Template Method** | `Vehicle` | Common structure, specific implementations |

---

## 🔧 SOLID Principles

- ✅ **S**ingle Responsibility: Each class has one responsibility
- ✅ **O**pen/Closed: Open for extension, closed for modification
- ✅ **L**iskov Substitution: Subclasses can replace parent classes
- ✅ **I**nterface Segregation: Small, focused interfaces
- ✅ **D**ependency Inversion: Depend on abstractions, not concretions

---

## 📊 View UML Diagram

1. Open `ParkingLotSystemUML.puml`
2. Copy all contents
3. Go to: http://www.plantuml.com/plantuml/uml/
4. Paste and view!

See `HOW_TO_VIEW_UML.md` for more options.

---

## 🧪 Expected Output

When you run the demo, you'll see:

```
╔════════════════════════════════════════════════════════════════╗
║        WELCOME TO SMART PARKING LOT SYSTEM                     ║
╚════════════════════════════════════════════════════════════════╝

🚀 Starting Automated Demo...

════════════════════════════════════════════════════════════════
SCENARIO 1: Park different types of vehicles
════════════════════════════════════════════════════════════════

✅ VEHICLE PARKED SUCCESSFULLY!
Vehicle        : MH12AB1234
Type           : Bike
Parking Spot   : S-1-001 (Floor 1)
Ticket ID      : TKT-ABC12345
...

[10 comprehensive scenarios demonstrating all requirements]

✅ ALL REQUIREMENTS DEMONSTRATED SUCCESSFULLY!
```

---

## 📚 Further Reading

- **README.md** - Complete documentation
- **Code Comments** - Detailed explanations in source code
- **UML Diagram** - Visual system architecture
- **Demo Code** - Live examples in `ParkingLotDemo.java`

---

## 🎯 Key Features

1. **Thread-Safe**: Uses `ConcurrentHashMap` for concurrent access
2. **Comprehensive Logging**: Detailed logs for all operations
3. **Validation**: Input validation at every step
4. **Error Handling**: Graceful error handling
5. **Extensible**: Easy to add new vehicle types, payment methods, pricing strategies
6. **Type-Safe**: Uses enums for type safety
7. **Immutable Where Needed**: Final fields for data integrity
8. **Well-Documented**: Javadoc comments throughout

---

## 💡 Customization

### Add New Vehicle Type
```java
public class Bus extends Vehicle {
    public Bus(String regNo, String owner) {
        super(regNo, owner, VehicleType.BUS);
    }
    
    @Override
    public SpotSize getRequiredSpotSize() {
        return SpotSize.EXTRA_LARGE;
    }
}
```

### Add New Payment Method
```java
public class WalletPayment implements PaymentStrategy {
    @Override
    public boolean processPayment(double amount, String ticketId) {
        // Implementation
    }
    
    // Other methods...
}
```

### Change Pricing Strategy
```java
// Use flat rate instead of hourly
ParkingLotSystem parkingLot = ParkingLotSystem.getInstance(
    "My Parking", 
    new FlatRatePricingStrategy()  // Just change this!
);
```

---

## 🆘 Troubleshooting

### Issue: Compilation Error
**Solution**: Make sure you're in the correct directory and Java 17+ is installed
```bash
java -version  # Check Java version
```

### Issue: Package not found
**Solution**: Compile from the correct directory (src/main/java)

### Issue: No output
**Solution**: Check logging configuration in `ParkingLotDemo.java`

---

## ✨ Next Steps

1. ✅ Run the automated demo
2. ✅ View the UML diagram
3. ✅ Read the comprehensive README
4. ✅ Explore the code with comments
5. ✅ Modify and extend as needed

---

**🎉 You're all set! Happy coding!**

