package OOPS_ADVANCE;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;

public class IteratorVsIterable {

    public static void main(String[] args) {
        ArrayList<Integer> lst = new ArrayList<>();
        lst.add(12);
        lst.add(23);
        lst.add(2);
        lst.add(24);
        lst.add(4);

        // 1st Method 
        for(int i = 0 ; i < lst.size() ; i++){
            System.out.print(lst.get(i) + " ");
        }
        System.out.println();

        // 2nd Method -> Iterable  -> forEach loop 
        for(Integer val : lst){
            System.out.print(val + " ");
        }
        System.out.println();


        //3rd Method using StreamApi -> ForEach Method -> using Lambda
        lst.forEach((val) -> System.out.print(val + " "));
        System.out.println();


        //4th Method -> using Iterator 

        // itr { 1, 2 , 4 ,5,}
        Iterator<Integer> iter = lst.iterator();
        // this itr has 4 methods ->
        // 1 : next() -> go to the next element
        // 2 : hasNext() -> it will return true or false .. 
        // if element has the next value then it will return true ; 
        // 3 : remove() -> remove the element 
        // 4 : forEachRemaining();
        
        while (iter.hasNext()) {
            int nextElement = iter.next(); 
            System.out.print(nextElement + " ");
            // if(nextElement % 2 == 0){
                //     iter.remove();
                // }
            }
            
            System.out.println();
            
        // ListIterator<Integer> lstIterator = lst.listIterator();
        // // using lstPrevious
        // int firstelem = 0 ;
        // while (lstIterator.hasPrevious()) {
        //     int prevElement = lstIterator.previous(); 
        //     System.out.print(prevElement + " " + firstelem);
        //     firstelem ++ ;
        // }
        // System.out.print(lst);
        // System.out.print(firstelem);






        

    }
}