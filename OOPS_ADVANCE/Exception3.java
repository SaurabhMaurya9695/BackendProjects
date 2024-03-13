import java.math.BigDecimal;

// we are creating custom exception 
class CantDivideByZeroException extends RuntimeException {
    public CantDivideByZeroException() {
        super();
    }

    public CantDivideByZeroException(String msg) {
        super(msg);
    }
}

// created Custom Exception
class BigDecimalException extends RuntimeException {
    public BigDecimalException() {
        super();
    }

    public BigDecimalException(String msg) {
        super(msg);
    }
}

class Calc {
    public int divide(int a, int b) {
        try {
            return a / b;
        } catch (Exception e) {
            throw new CantDivideByZeroException("can't Divide");
        }
    }

    public void check() {
        BigDecimal a1 = new BigDecimal(11);
        BigDecimal a2 = new BigDecimal(17);
        try {
            a1 = a1.divide(a2); // 0.8369136193619826356401640
            System.out.println(a1.toString());
        } catch (Exception e) {
            throw new BigDecimalException("This is not valid" +
             "Exception division");
        }
    }
}

public class Exception3 {
    public static void main(String[] args) {
        Calc c = new Calc();
        // System.out.println(c.divide(10, 0));
        c.check();
    }

    
}
