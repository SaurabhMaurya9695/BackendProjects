
// class Vehicle{
//     void run(){
//         System.out.println("Vehicle is running");
//     }
// }

// class Bike extends Vehicle{
//     void run1(){
//         System.out.println("Bike is running");
//     }
// }

// public class MethodOverriding {
//     public static void main(String[] args) {
//         Bike bike = new Bike();
//         bike.run1();
//         bike.run();
//     }
// }

class Bank {
    int getRateOfInterest() {
        return 0;
    }
}

// Creating child classes.
class SBI extends Bank {
    int getRateOfInterest() {
        return 8; // 8 per
    }
}

class ICICI extends Bank {
    int getRateOfInterest() {
        return 7; // 7 per
    }
}

class AXIS extends Bank {
    int getRateOfInterest() {
        return 9; // 9 per
    }
}

// Test class to create objects and call the methods
class MethodOverriding {
    public static void main(String args[]) {
        // SBI sbi = new SBI();
        // ICICI icici = new ICICI();
        // AXIS axix = new AXIS();
        Bank sbi = new SBI();
        Bank icici = new ICICI();
        Bank axix = new AXIS();
        System.out.println("SBI Rate of Interest: " + sbi.getRateOfInterest());
        System.out.println("ICICI Rate of Interest: " + icici.getRateOfInterest());
        System.out.println("AXIS Rate of Interest: " + axix.getRateOfInterest());
    }
}
