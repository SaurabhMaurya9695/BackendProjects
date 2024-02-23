// public class FinalKeyword {
//     final int speedlimit = 90;// final variable

//     void run() {
//         speedlimit = 400;
//     }

//     public static void main(String args[]) {
//         FinalKeyword obj = new FinalKeyword();
//         obj.run();
//     }
// }


class FinalKeyword
{  
  int cube(final int n){  
       n=n+2;//can't be changed as n is final  
      return n*n*n;  
  }  
  public static void main(String args[]){  
        Bike11 b=new Bike11();  
        System.out.println(b.cube(5));  
    }  
}  
