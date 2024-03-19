

// thread1 
class Raunak implements Runnable{

    @Override
    public void run() {
       System.out.println("We are in Rauank Thread");
       for(int i = 0 ; i < 5 ; i ++){
           System.out.print(i + " ");
        }
        System.out.println(" Rauank Thread Ended");
    }

}


class Saurabh extends Thread{

    public void run() {
        System.out.println("We are in Saurabh Thread");
        for(int i = 0 ; i < 5 ; i ++){
            System.out.print(i + " ");
         }
         System.out.println(" Rauank Saurabh Ended");
    }
}


public class ThreadDay2 {
    public static void main(String[] args) {
        // Created the Thread rauank 
        Raunak r = new Raunak() ;
        Thread rauank = new Thread(r);

        // before starting the raunak thread
        System.out.println("Life cycle of rauank thread is before starting : " + rauank.getState());
        rauank.start();
        System.out.println("Life cycle of rauank thread is after starting : " + rauank.getState());


        Saurabh saurabh = new Saurabh();
        System.out.println("Life cycle of rauank thread is before starting : " + saurabh.getState());
        saurabh.start();
        System.out.println("Life cycle of rauank thread is after starting : " + saurabh.getState());
        
    }
}
