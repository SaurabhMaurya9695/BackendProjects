abstract class Car{
    // we cant create the object for this class
    // it can have constructor 
    // it can have abstract and non abstract methods
    // it can have static methods & final keyword
    // we can access abstract class with extends keywords
    
    Car(){
        System.out.println("This is from Car Constuctor");
    }

    // abtstract method - we are not focusing on implemenetation 
    abstract void showCarData();
    // non Abstract Method
    void show(){
        System.out.println("This is from Car");
    }
}

class BMW extends Car{

    @Override
    void showCarData() {
        System.out.println("This is Car");
    }
}


public class Abstraction {
    public static void main(String[] args) {
        Car car = new BMW();
        car.showCarData();
        car.show();
    }
}
