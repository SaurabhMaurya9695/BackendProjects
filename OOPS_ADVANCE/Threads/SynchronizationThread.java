
class Theater {

    int availableSeats;

    Theater() {

    }

    Theater(int availableSeats) {
        this.availableSeats = availableSeats;
    }

    // one thread execute at once
    synchronized public void bookSeats(User user, int seats) {
        if (availableSeats >= seats) {
            System.out.println("Username " + user.userName + " has booked " + seats + " seats");
            availableSeats = availableSeats - seats;
            System.out.println("Seats Available : " + availableSeats);
        } else {
            System.out.println("Seat Not Available");
        }
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

public class SynchronizationThread {
    public static void main(String[] args) throws InterruptedException {

        User rauank = new User("Rauank");
        User yash = new User("yash");
        User vikas = new User("vikas");
        User saurabh = new User("saurabh");

        rauank.start();
        yash.start();
        vikas.start();
        saurabh.start();

    }
}
