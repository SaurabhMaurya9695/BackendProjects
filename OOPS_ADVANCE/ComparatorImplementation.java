package OOPS_ADVANCE;
import java.util.*;

class Movie {
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

    Movie(int duration) {
        this.duration = duration;

    }

    @Override
    public String toString(){
        return "Duration : " + this.duration + " Rating : " + this.rating + " name : " + this.name;
    }

    
}

class SortByDuration implements Comparator<Movie>{
    @Override
    public int compare(Movie a, Movie b) {
        if(a.duration == b.duration){
            return (int)(a.rating - b.rating) ;
        }
        else {
            return a.duration - b.duration;
        }
    }
}

class SortByName implements Comparator<Movie>{
    @Override
    public int compare(Movie a, Movie b) {
        if((a.name).equals(b.name)){
            return a.duration - b.duration;
        }
        else{
            return b.duration - a.duration;
        }
    }
}

public class ComparatorImplementation {
    public static void main(String[] args) {
        Movie [] arr = new Movie[5];

        arr[0] = new Movie(10, 4.5, "Figher");
        arr[1] = new Movie(10, 3.0, "Article 370");
        arr[2] = new Movie(13, 1.0, "Demon Slayer");
        arr[3] = new Movie(1, 0.5, "Animal");
        arr[4] = new Movie(3, 5.5, "12th Fail");
        
        // if you want to do sorting in any class then you have to 
        // impelment comparator
        Arrays.sort(arr , new SortByDuration());
        for (Movie movie : arr) {
            System.out.println(movie.toString());
        }
        System.out.println();
    }    
}