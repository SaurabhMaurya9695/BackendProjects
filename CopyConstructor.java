
class Movie {
    private String movieName;
    private String genre;

    Movie(){
        
    }

    Movie(Movie m1){
        setMovieName(m1.getMovieName());
        setGenre(m1.getGenre() + "Adventure ");
    }

    Movie(Movie m1 , Movie m2){
        this.movieName  = (m1.getMovieName() + m2.getMovieName());
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }
}

public class CopyConstructor {
    public static void main(String[] args) {
        Movie movie1 = new Movie() ;
        movie1.setGenre("Horror");
        movie1.setMovieName("1920");

        Movie movie2 = new Movie(movie1) ;

        Movie movie3 = new Movie(movie1 , movie2) ;

        
        System.out.println(movie1.getMovieName() + " " + movie1.getGenre());
        System.out.println(movie2.getMovieName() + " " + movie2.getGenre());
        System.out.println(movie3.getMovieName());

    }
}
