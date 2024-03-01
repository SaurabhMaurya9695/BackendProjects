import java.util.*;
 
class Movie implements Comparable<Movie>{
    int duration ;
    double rating ;
    String name;
 
    Movie(){
 
    }
 
    Movie(int duration, double rating, String name) {
        this.duration = duration;
        this.rating = rating;
        this.name = name;
    }
 
    @Override
    public String toString(){
        return "Duration : " + this.duration + " Rating : " + 
        this.rating + " name : " + this.name;
    }
     @Override
    public int compareTo(Movie b) {
            return (int)(b.rating- this.rating);
    }
}
 
 
public class ComparableImplementation {
    public static void main(String[] args) {
        Movie [] arr = new Movie[5];
 
        arr[0] = new Movie(19, 4.5, "Figher");
        arr[1] = new Movie(12, 3.0, "Figher");
        arr[2] = new Movie(13, 1.0, "Demon Slayer");
        arr[3] = new Movie(1, 0.5, "Animal");
        arr[4] = new Movie(3, 5.5, "12th Fail");
 
        // if you want to do sorting in any class then you have to 
        // impelment comparator
        Arrays.sort(arr);
        for (Movie movie : arr) {
            System.out.println(movie.toString());
        }
        System.out.println();
    }    
}