
// created Thread Using Runnable Interface
class MyThread implements Runnable{

    @Override
    public void run() {
        System.out.println("My Thread Running");
        for(int i = 0 ; i< 10 ; i ++){
           System.out.print(i + " ");
        }
        System.out.println("\nMy Thread Ended");
    }
    
}

// created Thread Using Thread Class
class MyThread2 extends Thread{

    @Override
    public void run() {
        System.out.println("My Thread 2  Running");
        for(int i = 0 ; i< 10 ; i ++){
           System.out.print(i + " ");
        }
        System.out.println("\nMy Thread 2 Ended");
    }
}


public class Threads {
    public static void main(String[] args) throws InterruptedException {
        // steps to create thread using Runnable Interface
        // 1 : create a class and implements Runnable Interface 
        // 2 : override run method 
        // 3 : write the login in run Method
        // 4 : create an object for class "MyThread";
        MyThread myThread = new MyThread();
        // 5 : create an object for thread class 
        Thread thread = new Thread(myThread) ; //  thread initialization 
        // 6 : now start your thread
        thread.start();


        // steps to create thread using Thread Class
        // 1 : create a class and extends Thread 
        // 2 : override run method & write the logic
        // 3 : create an object for a class 
        MyThread2 myThread2 = new MyThread2() ; // thread initialization 
        // 4 : now start using your thread
        myThread2.start();
        myThread2.sleep(1000);
    }
}