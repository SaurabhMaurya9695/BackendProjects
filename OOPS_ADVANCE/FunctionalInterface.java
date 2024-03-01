package OOPS_ADVANCE;

@FunctionalInterface
interface Operation {
    public abstract int applyOp(int a , int b);

}

public class FunctionalInterface {
    public static void main(String[] args) {
       
       Operation addition  = (a, b) -> a + b;
       Operation subtraction = (a, b) -> a - b;
       Operation prod = (a, b) -> a * b;

       System.out.println(addition.applyOp(10, 5));
       System.out.println(subtraction.applyOp(10, 5));
       System.out.println(prod.applyOp(10, 5));
       
       
    }
}
