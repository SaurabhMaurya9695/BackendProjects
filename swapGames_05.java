
class Movie{
    private int duration ;

    public int getDuration() {
        return duration;
    }
    public void setDuration(int x) {
        duration = x;
    }
    
}



public class SwapGames_05 {
    public static void main(String[] args) {
        Movie m1 = new Movie() ;
        m1.setDuration(120);

        Movie m2 = new Movie() ;
        m2.setDuration(150);
        
        
        System.out.println("Before Swapping");
        System.out.println(m1.getDuration() + " " + m2.getDuration());
        swap(m1 , m2);
        System.out.println("After Swapping");
        System.out.println(m1.getDuration() + " " + m2.getDuration());
    }

    public static void swap4(Movie a1 , Movie a2){
        Movie a3 = new Movie() ;
         a3.setDuration(a1.getDuration());
         a1.setDuration(a2.getDuration());
         a2.setDuration(a3.getDuration());
         
    }

    public static void swap3(Movie a1 , Movie a2){
        Movie a3 = a1;
        a1.setDuration(a2.getDuration());
        a2.setDuration(a3.getDuration());       
    }

    public static void swap2(Movie a1 , Movie a2){
        Movie a3 = a1;
        a3.setDuration(a1.getDuration());
        a1 = a2 ;
        a2 = a3 ;
    }

    public void swap(Movie a1 , Movie a2){
        Movie a3 = a1 ;
        a1 = a2 ;
        a2 = a3 ;
    }
}
