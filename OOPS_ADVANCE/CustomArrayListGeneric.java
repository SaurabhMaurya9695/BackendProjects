package OOPS_ADVANCE;

import java.util.Arrays;

class MyArrayList<T> {
    // array , capacity , idx
    private Object [] arr = new Object[5];
    // we can't create an array of generic class so we created as an object 

    private int capacity = 5; // maximum array can hold 5 element 
    private int idx = 0 ; // current idx is zero 

    // set the value at given idx 
    public void setValue(T val , int idx){
        arr[idx] = val;
    }

    // get the value at given idx 
    public T getValue(int idx){
        return ((T)arr[idx]); // downcasting 
    }

    // add the value at the end 
    public void add(T val){

        // if we have idx value 5 and we want to add the next value 
        if(idx == capacity){
            capacity = capacity + (capacity / 2);
            Object [] copyArray = new Object[capacity];
            for(int idx = 0 ;idx < arr.length ; idx ++){
                copyArray[idx] = arr[idx];
            }
            arr = copyArray;
        }

        // for first time value set at zero idx 
        arr[idx] = val;
        idx ++ ; 
        // for the second time value set at second idx 
    }

    //remove last value ;
    public T remove(){
        T removedValue = ((T)arr[idx]);
        arr[idx] = 0;
        idx -- ;
        return removedValue;
    }

    @Override
    public String toString() {
        // "[1 , 2 , 3 , 5]" ;
        StringBuilder sb = new StringBuilder('[') ;
        for(Object i : arr){
            sb.append(i + ",");
        }
        sb.setCharAt(sb.length() - 1, ']');
        // every class has toString method
        return sb.toString();
    }

    
    

}

public class CustomArrayListGeneric {
    public static void main(String[] args) {
        MyArrayList lst = new MyArrayList();
        lst.add(12);
        lst.add(1);
        lst.add(189);
        lst.add(1891);
        lst.add(18);
        lst.add(183);
         System.out.println(lst.toString());
        System.out.println("removed Value is " + lst.remove());
         System.out.println(lst.toString());
        
    }
}
