
class MyThread implements Runnable{

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName());
        for(int i = 0 ; i< 10 ; i ++){
           System.out.println( "Higher Priority " + i );
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(Thread.currentThread().isDaemon());
    }
    
}

class MyThread2 extends Thread{

    @Override
    public void run() {
        // Thread.yield(); -> stop the execution untill others is running 
        System.out.println(Thread.currentThread().getName());
        for(int i = 0 ; i< 10 ; i ++){
            System.out.println( "Lower Priority " + i );
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}


public class Threadsday2 {
    public static void main(String[] args) {
        MyThread myThread = new MyThread();
        Thread thread = new Thread(myThread);

        MyThread2 myThread2 = new MyThread2();
            
        // setting the Priority
        thread.setPriority(10);
        myThread2.setPriority(1);
        
        // setting name of a thread
        thread.setName("Higher Priority Thread");
        myThread2.setName("Lower Priority thread");
        
        // before starting we set our thread as demon 
        // thread.setDaemon(true);
        
        thread.start();
        myThread2.start();

        // thread.setDaemon(true); // it will throw an exception -> IllegalThreadStateException

        System.out.println(thread.getPriority());
        System.out.println(myThread2.getPriority());

        System.out.println(Thread.currentThread().getName());
       
        
    }
}
