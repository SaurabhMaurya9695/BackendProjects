package org.Singelton;

// public class Logger {
//     private static Logger instace = new Logger();

//     private Logger() {
//     }

//     // to access private things within the class we have to use ?
//     // getter , setter
//     public static Logger getInstance() {
//         return instace;
//     }

//     public void log(String msg) {
//         System.out.println(msg);
//     }
// }

public class Logger {
    private static Logger instace;

    private Logger() {
    }

    public static Logger getInstance() {
        System.out.println("We entered in a movie theater " 
                        + Thread.currentThread().getName());
        System.out.println("We opened paytm " 
                        + Thread.currentThread().getName());
        System.out.println("We scaned the QR " 
                        + Thread.currentThread().getName());

        if (instace == null) {
            synchronized (Logger.class) {
                if (instace == null) {
                    instace = new Logger();
                }
            }
        }
        return instace;
    }

    public void log(String msg) {
        System.out.println(msg);
    }
}
