class Vehicle{
    String vehicleName ;
    
    Vehicle(){
        System.out.println("Vehicle Const Called");
    }
    
    void runVehical(){
        System.out.println("Vehicle Is Running");
    }
}


class Car extends Vehicle{ // car Is-A vehicle but vehicle is not a car
    String carName ;
    Car(){
        super();
        super.runVehical();
        System.out.println("Car Const Called");
    }
    void run(){
        System.out.println("Car Is Running");
    }
}

public class Super {
    public static void main(String[] args) {
        Car  car = new Car();
        car.run();
        car.runVehical();
    }
}