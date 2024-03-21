package OOPS_ADVANCE;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class StreamApi {
    public static void main(String[] args) {

        // old way of creating list 
        /*
            ArrayList<Integer> lst = new ArrayList<>() ;
            lst.add(10);
            lst.add(20);
            lst.add(-2);
            lst.add(23);
        */

        // List<Integer> lst = List.of(10 , 20, -2 , 23 , 24); // Java 8 Features 
        
        // List<Integer> squaresList = new ArrayList<>();

        /*
            // normal for loop
            for(int i = 0; i< lst.size() ; i++){
                System.out.print( lst.get(i) + " ");
            }
            
            // normal forEach loop
            System.out.println();
            for (Integer val : lst) {
                System.out.print( val + " ");
            }
            System.out.println();
            
            // normal forEach method
            lst.forEach((val) -> System.out.print(val + " "));
            System.out.println();

            // direct print
            System.out.println(lst);
        */
        
        // simpler way to get the square of all elements 
        // for(int i = 0; i< lst.size() ; i++){
        //     squaresList.add(lst.get(i) * lst.get(i));
        // }

        // for(int i = 0; i< squaresList.size() ; i++){
        //     System.out.print( squaresList.get(i) + " ");
        // }

        // System.out.println(squaresList);

        // solving above question using stream Api ;
        List<Integer> lst = List.of(10 , 20, -2 , 23 , 24); 


        // List<Integer> sqList = lst.stream().map((val) -> {
        //     return val * val ;
        // }).collect(Collectors.toList());

        // System.out.println(sqList);
        
        // find all even values in the lst
        // List<Integer> evenList = lst.stream().filter((val) -> (val % 2 == 0)).collect(Collectors.toList());
        // System.out.println(evenList); // printing the even element lst

        // Q -> in the above lst add 10 to all element and return those elements which are odd ;
        // List<Integer> ans = lst.stream().map((val) -> {return val + 10 ; }).filter((val) -> (val % 2 == 1)).collect(Collectors.toList());
        // System.out.println(ans);
        
        lst.stream().map((val) -> {return val + 10 ; }).filter((val) -> (val % 2 == 1)).forEach(x ->{
            System.out.println(x);
        });;


        


    }
}
