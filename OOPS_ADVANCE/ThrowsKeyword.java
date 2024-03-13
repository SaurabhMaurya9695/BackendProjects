package OOPS_ADVANCE;



class CantDivideByZeroException extends RuntimeException {
    public CantDivideByZeroException() {
        super();
    }

    public CantDivideByZeroException(String msg) {
        super(msg);
    }
}

class X{
    public int d(int a , int b) throws CantDivideByZeroException {
        try{
            return a / b ;
        }
        catch(Exception e){
            throw new CantDivideByZeroException("Can't Divide by Zero");
        }
    }
}

class Calc{
    public int divide(int a, int b) throws CantDivideByZeroException {
        X x = new X();
        return x.d(a, b);
    }
}

public class ThrowsKeyword {
    public static void main(String[] args) throws CantDivideByZeroException{
        Calc cal = new Calc();
        System.out.println(cal.divide(10 , 0));
    }
}