package OOPS_ADVANCE;

import java.util.Arrays;

public class CustomLambdaExpression {
    public static void main(String[] args) {
        int [] arr = {10 , 9 , 67 , 65 , 1 , 8 , 7};

        // how we can sort ?
        Arrays.sort(arr);
        // Arrays.sort(arr , comparator); // so we can't pass the comp or lambda functions to primitive dataType

        for (Integer val : arr) {
            System.out.print(val + " ");
        }
        System.out.println();
        System.out.println(arr);

        
        // we converted into wrapper data type array to use lambda / compartor logic 

        Integer [] copy = new Integer[arr.length];
        for(int i = 0 ; i < arr.length ; i ++){
            copy[i] = arr[i];
        }

        Arrays.sort(copy , (a , b) ->{
            // if you value is  -ive then means b > a -> dec 
            // if you value is  +ive then means a > b -> inc
            return b - a;
        });

        for (Integer val : copy) {
            System.out.print(val + " ");
        }
        System.out.println();


    }
}
