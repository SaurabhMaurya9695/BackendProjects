class Theater {

    int availableSeats;

    Theater() {

    }

    Theater(int availableSeats) {
        this.availableSeats = availableSeats;
    }

    // one thread execute at once
    public void bookSeats(User user, int seats) {

        System.out.println("Welcome for coming in BookMyShow App : " + user.userName);
        System.out.println("Please Fill the details to move forward " );
        System.out.println("Please proceed to payment gateway " );
        
        // synchronized Block / Lock with respect to User 
        synchronized(User.class){
            if (availableSeats >= seats) {
                System.out.println("Username " + user.userName + " has booked " + seats + " seats");
                availableSeats = availableSeats - seats;
                System.out.println("Seats Available : " + availableSeats);
            } else {
                System.out.println("Seat Not Available");
            }

            try{
                this.notify();
            }
            catch(Exception e){

            }

        }
        System.out.println("Thanks for payment your seat has been alocated " );
        System.out.println("Thanks for visit bookMyShow " );
        System.out.println("------------------------------------------------");
    }
}

class User extends Thread{
    String userName;
    static Theater pvr = new Theater(20); // Has-A theater

    User() {

    }

    User(String name) {
        this.userName = name;
    }

    public void run(){
        pvr.bookSeats(this, 10);
    }

}

public class Synchronizationday2 {
    public static void main(String[] args) throws Exception {

        User rauank = new User("Rauank");
        User yash = new User("yash");
        User vikas = new User("vikas");
        User saurabh = new User("saurabh");

        // in below commented code .. we faced asynchonous behaviour in main thread

        /*
            rauank.start();
            System.out.println("avaible seats for Raunak : " + User.pvr.availableSeats);
            yash.start();
            System.out.println("avaible seats for yash : " + User.pvr.availableSeats);
            vikas.start();
            System.out.println("avaible seats for vikas : " + User.pvr.availableSeats);
            saurabh.start();
            System.out.println("avaible seats for saurabh : " + User.pvr.availableSeats);
         */

        rauank.start();
        synchronized(rauank){
            rauank.wait();
            System.out.println("avaible seats left : " + User.pvr.availableSeats);
        }

        yash.start();
        synchronized(yash){
            yash.wait();
            System.out.println("avaible seats left : " + User.pvr.availableSeats);
        }
        
        vikas.start();
        synchronized(vikas){
            vikas.wait();
            System.out.println("avaible seats left : " + User.pvr.availableSeats);
        }

        saurabh.start();
        synchronized(saurabh){
            saurabh.wait();
            System.out.println("avaible seats left : " + User.pvr.availableSeats);
        }
        

    }
}
