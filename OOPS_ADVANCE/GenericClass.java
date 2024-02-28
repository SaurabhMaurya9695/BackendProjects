package OOPS_ADVANCE;


// this is a generic class of Rectangle , and this class can accept any dataType

class Rectangle<T extends Number>{
    private T length ;
    private T breath ;

    public T getLength() {
        return length;
    }
    public void setLength(T length) {
        this.length = length;
    }
    public T getBreath() {
        return breath;
    }
    public void setBreath(T breath) {
        this.breath = breath;
    }

    @Override
    public String toString() {
        return "Rectangle [length=" + length + ", breath=" + breath + "]";
    } 

    
}

public class GenericClass {
    public static void main(String[] args) {
        // Rectangle rectangle = new Rectangle() ;
        // rectangle.setLength("10");
        // rectangle.setBreath("20");
        // System.out.println(rectangle.getLength() + " " + rectangle.getBreath()) ;

        // i want to make this above class generic 

        Rectangle<Integer> r1 =  new Rectangle<>() ;
        r1.setBreath(10);
        r1.setLength(20);
        System.out.println(r1.toString());

        Rectangle<Double> r2 =  new Rectangle<>() ;
        r2.setBreath(10.0);
        r2.setLength(20.0);
        System.out.println(r2.toString());

        // Rectangle<String> r3 =  new Rectangle<>() ;
        // r3.setBreath("Hello");
        // r3.setLength("World");
        // System.out.println(r3.toString());

        // if we want to fix Type should be alwaya a number .

    }
}
