



class LMV{
    String lightMoterVehicle;
    
    LMV(){
        this.lightMoterVehicle = "This Is LMV";
        System.out.println(this.lightMoterVehicle);
    }

    LMV(String name){
        this.lightMoterVehicle = name;
    }

    void lmv(){
        System.out.println("This is LMV method Called");
    }
}


class Vehicle extends LMV{
    String vehicleName ;
    
    Vehicle(){
        super("updated LMV");
        super.lmv();
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

public class Super2 {
    public static void main(String[] args) {
        Car  car = new Car();
        car.run();
        car.runVehical();
        System.out.println(car.lightMoterVehicle);
        car.lmv();
    }
}

