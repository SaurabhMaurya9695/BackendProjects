# 🅿️ Smart Parking Lot System - Complete LLD Implementation

A comprehensive **Low-Level Design (LLD)** implementation of a Parking Lot System following **SOLID principles** and industry-standard **design patterns**.

---

## 📋 Table of Contents

1. [Requirements](#requirements)
2. [System Features](#system-features)
3. [Architecture & Design](#architecture--design)
4. [Design Patterns Used](#design-patterns-used)
5. [SOLID Principles](#solid-principles)
6. [Class Structure](#class-structure)
7. [How to Run](#how-to-run)
8. [Use Cases](#use-cases)
9. [UML Diagram](#uml-diagram)
10. [Future Enhancements](#future-enhancements)

---

## ✅ Requirements

The system fulfills the following requirements:

1. **Multiple Parking Slots** - Parking lot has multiple slots available for vehicles
2. **Vehicle Type Support** - Different vehicle types (Bike, Car, Truck) occupy different slot sizes
3. **Parking Ticket System** - Each vehicle gets a parking ticket upon entry
4. **Dynamic Pricing** - System calculates price based on vehicle type and duration of stay
5. **Payment Validation** - Vehicle must complete payment before exiting
6. **Multiple Payment Methods** - Supports Cash, Credit Card, and UPI payments
7. **Slot Management** - Once payment is completed, vehicle can exit and slot is freed
8. **Occupancy Prevention** - Vehicle cannot park on an occupied space
9. **Exit Validation** - Vehicle cannot exit without completing payment

---

## 🚀 System Features

### Core Features

- **Real-time Parking Availability**: Check available spots by vehicle type
- **Automated Ticket Generation**: Unique ticket ID for each parking session
- **Flexible Pricing Strategies**: Hourly or flat-rate pricing
- **Multiple Payment Options**: Cash, Credit Card, UPI
- **Thread-Safe Operations**: Concurrent access handling
- **Comprehensive Logging**: Detailed logs for debugging and monitoring
- **Status Dashboard**: Real-time parking lot status

### Business Logic

- Smart spot allocation (finds best fit spot for vehicle)
- Automatic time tracking
- Payment validation before exit
- Duplicate parking prevention
- Change calculation for cash payments

---

## 🏗️ Architecture & Design

### High-Level Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                    ParkingLotSystem                         │
│                    (Facade Pattern)                         │
└───────────────────────┬─────────────────────────────────────┘
                        │
        ┌───────────────┼───────────────┐
        │               │               │
        ▼               ▼               ▼
┌──────────────┐ ┌────────────┐ ┌──────────────┐
│ParkingSpot   │ │  Ticket    │ │   Payment    │
│  Manager     │ │  Manager   │ │   Manager    │
└──────────────┘ └────────────┘ └──────────────┘
        │               │               │
        │               │               │
        ▼               ▼               ▼
  [Spot Logic]   [Ticket Logic]  [Payment Logic]
```

### Component Breakdown

1. **Models**: Core business entities (Vehicle, ParkingSpot, ParkingTicket)
2. **Managers**: Business logic handlers (ParkingSpotManager, TicketManager, PaymentManager)
3. **Strategies**: Pluggable algorithms (PricingStrategy, PaymentStrategy)
4. **Enums**: Type-safe constants (VehicleType, SpotSize, PaymentMethod, etc.)

---

## 🎨 Design Patterns Used

### 1. **Strategy Pattern**
- **Pricing Strategies**: HourlyPricingStrategy, FlatRatePricingStrategy
- **Payment Strategies**: CashPayment, CreditCardPayment, UPIPayment
- **Benefit**: Easy to add new pricing models or payment methods without modifying existing code

```java
// Example: Switch pricing strategy at runtime
PricingStrategy hourly = new HourlyPricingStrategy();
PricingStrategy flat = new FlatRatePricingStrategy();
```

### 2. **Singleton Pattern**
- **ParkingLotSystem**: Ensures only one instance of the parking lot system
- **Benefit**: Global access point, controlled instantiation

```java
ParkingLotSystem parkingLot = ParkingLotSystem.getInstance("My Parking", pricingStrategy);
```

### 3. **Facade Pattern**
- **ParkingLotSystem**: Provides simplified interface to complex subsystems
- **Benefit**: Hides complexity of managers from clients

```java
parkingLot.parkVehicle(vehicle);  // Simple interface hiding complex operations
```

### 4. **Template Method Pattern**
- **Vehicle.getRequiredSpotSize()**: Abstract method implemented by subclasses
- **Benefit**: Defines skeleton of algorithm, lets subclasses implement specific steps

---

## 🔧 SOLID Principles

### ✅ Single Responsibility Principle (SRP)
Each class has **one reason to change**:
- `ParkingSpotManager`: Only manages parking spots
- `TicketManager`: Only manages tickets
- `PaymentManager`: Only manages payments
- `Vehicle`: Only represents vehicle data

### ✅ Open/Closed Principle (OCP)
Classes are **open for extension, closed for modification**:
- New vehicle types can be added by extending `Vehicle`
- New pricing strategies by implementing `PricingStrategy`
- New payment methods by implementing `PaymentStrategy`

```java
// Add new vehicle type without modifying existing code
public class Bus extends Vehicle {
    @Override
    public SpotSize getRequiredSpotSize() {
        return SpotSize.EXTRA_LARGE;
    }
}
```

### ✅ Liskov Substitution Principle (LSP)
**Subclasses can replace parent classes** without breaking functionality:
- Any `Vehicle` subclass works with `ParkingSpot.canAccommodate()`
- Any `PaymentStrategy` implementation works with `PaymentManager`

### ✅ Interface Segregation Principle (ISP)
**Small, focused interfaces**:
- `PricingStrategy`: Only pricing-related methods
- `PaymentStrategy`: Only payment-related methods
- No client forced to depend on methods it doesn't use

### ✅ Dependency Inversion Principle (DIP)
**Depend on abstractions, not concretions**:
- `PaymentManager` depends on `PricingStrategy` interface, not concrete implementations
- Easy to switch strategies at runtime

---

## 📦 Class Structure

### Package Organization

```
CursorWay/
├── enums/
│   ├── VehicleType.java
│   ├── SpotSize.java
│   ├── SpotStatus.java
│   ├── PaymentStatus.java
│   └── PaymentMethod.java
├── models/
│   ├── Vehicle.java (abstract)
│   ├── Bike.java
│   ├── Car.java
│   ├── Truck.java
│   ├── ParkingSpot.java
│   └── ParkingTicket.java
├── strategy/
│   ├── pricing/
│   │   ├── PricingStrategy.java (interface)
│   │   ├── HourlyPricingStrategy.java
│   │   └── FlatRatePricingStrategy.java
│   └── payment/
│       ├── PaymentStrategy.java (interface)
│       ├── CashPayment.java
│       ├── CreditCardPayment.java
│       └── UPIPayment.java
├── managers/
│   ├── ParkingSpotManager.java
│   ├── TicketManager.java
│   └── PaymentManager.java
├── ParkingLotSystem.java (main facade)
├── ParkingLotDemo.java (demonstration)
└── README.md
```

### Key Classes

| Class | Responsibility | Design Pattern |
|-------|---------------|----------------|
| `ParkingLotSystem` | Main system facade | Singleton, Facade |
| `Vehicle` | Base vehicle entity | Template Method |
| `ParkingSpot` | Parking spot management | - |
| `ParkingTicket` | Ticket information | - |
| `ParkingSpotManager` | Spot allocation logic | - |
| `TicketManager` | Ticket lifecycle | - |
| `PaymentManager` | Payment processing | - |
| `PricingStrategy` | Pricing algorithm | Strategy |
| `PaymentStrategy` | Payment method | Strategy |

---

## 🏃 How to Run

### Prerequisites
- Java 17 or higher
- Maven (optional)

### Compile and Run

```bash
# Navigate to the CursorWay directory
cd system-design/src/main/java/com/backend/system/design/LLD/ParkingLot/CursorWay

# Compile
javac -d out $(find . -name "*.java")

# Run demo
java -cp out com.backend.system.design.LLD.ParkingLot.CursorWay.ParkingLotDemo
```

### Expected Output

The demo will show:
1. ✅ Parking multiple vehicles
2. ✅ Ticket generation
3. ✅ Preventing duplicate parking
4. ✅ Fee calculation
5. ✅ Payment processing (Cash, UPI, Credit Card)
6. ✅ Vehicle exit
7. ✅ Slot availability updates

---

## 📝 Use Cases

### Use Case 1: Park a Vehicle

```java
// Create vehicle
Vehicle car = new Car("MH12AB1234", "John Doe");

// Park vehicle
Optional<ParkingTicket> ticket = parkingLot.parkVehicle(car);

// Ticket is automatically generated with entry time
```

### Use Case 2: Calculate Parking Fee

```java
// Get fee for a ticket
String ticketId = ticket.get().getTicketId();
double fee = parkingLot.getParkingFee(ticketId);

// Or display detailed fee breakdown
parkingLot.displayFeeDetails(ticketId);
```

### Use Case 3: Process Payment

```java
// Option 1: Cash payment
PaymentStrategy cashPayment = new CashPayment(100.0);
parkingLot.processPayment(ticketId, cashPayment);

// Option 2: UPI payment
PaymentStrategy upiPayment = new UPIPayment("user@upi");
parkingLot.processPayment(ticketId, upiPayment);

// Option 3: Credit card payment
PaymentStrategy cardPayment = new CreditCardPayment(
    "1234567812345678", "John Doe", "12/25", "123"
);
parkingLot.processPayment(ticketId, cardPayment);
```

### Use Case 4: Exit Vehicle

```java
// Exit with payment
parkingLot.unparkVehicle(ticketId, paymentStrategy);

// Or exit if already paid
parkingLot.unparkVehicle(ticketId, null);
```

### Use Case 5: Check Parking Status

```java
// Display current status
parkingLot.displayStatus();

// Display active tickets
parkingLot.displayActiveTickets();
```

---

## 📊 UML Diagram

See `ParkingLotSystemUML.puml` for the complete class diagram.

To view the UML:
1. Visit [PlantUML Online Editor](http://www.plantuml.com/plantuml/uml/)
2. Copy contents from `ParkingLotSystemUML.puml`
3. Paste and view the diagram

---

## 🔮 Future Enhancements

### Potential Improvements

1. **Database Integration**
   - Persist parking data
   - Historical reporting

2. **REST API**
   - Web service endpoints
   - Mobile app integration

3. **Advanced Features**
   - Reservation system
   - Loyalty programs
   - Dynamic pricing based on demand
   - Multi-floor visualization

4. **Security**
   - Authentication & authorization
   - Payment encryption
   - Audit logs

5. **Analytics**
   - Revenue reports
   - Peak hour analysis
   - Vehicle type statistics

6. **Notifications**
   - SMS/Email alerts
   - Payment reminders
   - Spot availability alerts

---

## 🧪 Testing

### Test Scenarios Covered

- ✅ Park different vehicle types
- ✅ Prevent duplicate parking
- ✅ Prevent parking on occupied spot
- ✅ Calculate fees correctly
- ✅ Validate payment before exit
- ✅ Process multiple payment methods
- ✅ Free spot after exit
- ✅ Handle concurrent operations

---

## 📄 License

This is an educational project demonstrating LLD principles.

---

## 👨‍💻 Author

**Saurabh**  
Low-Level Design Implementation  
Focus: Clean Code, SOLID Principles, Design Patterns

---

## 📞 Support

For questions or suggestions:
- Review the code comments
- Check the demo for usage examples
- Refer to SOLID principles documentation

---

## 🎯 Key Takeaways

1. **Design Patterns** make code flexible and maintainable
2. **SOLID Principles** ensure scalable architecture
3. **Clean Code** practices improve readability
4. **Comprehensive Logging** aids debugging
5. **Thread Safety** enables concurrent operations

---

**Happy Coding! 🚀**

