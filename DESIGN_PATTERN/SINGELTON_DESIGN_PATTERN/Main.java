package org.Singelton;

class CustomThread extends Thread{

    @Override
    public void run(){
        Logger loggerObj = Logger.getInstance();
//        loggerObj.log("Thread Created " + Thread.currentThread().getName());
        System.out.println(loggerObj.hashCode());
    }
}

public class Main {
    public static void main(String[] args) {

        // this code is for method1 and lazy Propgation 
        // Logger loggerObj = Logger.getInstance();
        // loggerObj.log("This side logger 1");
        // System.out.println(loggerObj.hashCode());

        // Logger loggerObj2 = Logger.getInstance();
        // loggerObj2.log("This side logger 2");
        // System.out.println(loggerObj2.hashCode());

        // -------------------------------------------------------------
        CustomThread t1 = new CustomThread() ;
        t1.start();

        CustomThread t2 = new CustomThread() ;
        t2.start();
    }
}