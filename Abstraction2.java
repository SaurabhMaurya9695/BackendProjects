
abstract class CalculatePrice {
    abstract void printPrice();

    void show(){
        System.out.println("This is show");
    }
}

class Samsung extends CalculatePrice {

    @Override
    void printPrice() {
        System.out.println("Price for Samsung is : 499");
    }

}

class Sony extends CalculatePrice {

    @Override
    void printPrice() {
        System.out.println("Price for Sony is : 699");
    }

}

class TV extends CalculatePrice {

    @Override
    void printPrice() {
        System.err.println("price for tv is : 1000");
    }

}

public class Abstraction2 {
    public static void main(String[] args) {
        CalculatePrice calculatePrice =  new Samsung();
        CalculatePrice calculatePrice1 =  new Sony();
        CalculatePrice calculatePrice2 =  new TV();
        calculatePrice.printPrice();
        calculatePrice1.printPrice();
        calculatePrice2.printPrice();
        
    }
}
