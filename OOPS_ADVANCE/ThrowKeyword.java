package OOPS_ADVANCE;



class AgeNotValidException extends RuntimeException{
    public AgeNotValidException(){
        super();
    }

    public AgeNotValidException(String msg){
        super(msg);
    }
}
public class ThrowKeyword {
    public static void main(String[] args) {
        int age = 17 ;
        if(age < 18){
            throw new AgeNotValidException("Age Can't be Below 18 for voting");
        }
        else{
            System.out.println("Eligible for vote");
        }
    }
}
