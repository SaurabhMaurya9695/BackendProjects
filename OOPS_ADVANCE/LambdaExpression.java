package OOPS_ADVANCE;

import java.util.ArrayList;

public class LambdaExpression {
    public static void main(String[] args) {
        int [] arr = {10 ,20 ,50 , 5 , 7 , 9};

        // normal Printing 
        for(int i = 0 ; i < arr.length ; i ++){
            System.out.print(arr[i]+ " ");
        }
        System.out.println();

        // forEach Loop
        for(Integer x : arr){
            System.out.print(x + " ");
        }
        System.out.println();

        // we can use forEach methods on collections only
        ArrayList<Integer> lst = new ArrayList<>();
        lst.add(10);
        lst.add(30);
        lst.add(1);
        lst.add(100);
        lst.add(17);
        

        // printing using forEach Methods
        lst.forEach(x -> System.out.print(x + " "));
        System.out.println();


        lst.forEach(x -> {
            if(x % 2  == 0){
                System.out.print(x + " ") ;
            }
        });
        System.out.println();



         
    }
}