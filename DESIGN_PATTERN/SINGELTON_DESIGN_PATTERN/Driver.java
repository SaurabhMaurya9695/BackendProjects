package org.Singelton;

public class Driver {
    public static void main(String[] args) {
        Logger loggerObj = Logger.getInstance();
        loggerObj.log("This side logger 1");
        System.out.println(loggerObj.hashCode());
    }
}
