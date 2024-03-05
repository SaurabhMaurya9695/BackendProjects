package OOPS_ADVANCE.Collections;

import java.util.*;

public class CountOcuurenceOfNumber {
    public static void main(String[] args) {
        List<Integer> lst = List.of( 1, 13, 4, 1, 41, 31,
                                 31, 4, 13, 2  );
        
        // count the number of occurence of an element and get the response in a sorted manner
        HashMap<Integer , Integer > mp = new HashMap<>();
        
        // {1 , 2};
        // {4 , 2};

        // mp.getOrDefault(val, 0) -> if values is present for the corresponding key then take that 
        // value or else take zero by default 
        lst.forEach(val ->{
            mp.put(val, mp.getOrDefault(val, 0) + 1);
        });

        // print 
        for (Integer val : mp.keySet()) {
            System.out.println(val + " " + mp.get(val));
        }
        System.out.println();

        // how many unique values is present in lst ;

        


    }
}
