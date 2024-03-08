package OOPS_ADVANCE.Collections;

import java.util.ArrayList;
import java.util.LinkedList;

public class ArrayListVSLinkedList {
    
    public static void main(String[] args) {
        ArrayList<Integer> arrList = new ArrayList<>(); // contiguious manner 
        arrList.add(2);
        arrList.add(3);
        arrList.add(4);
        arrList.add(1);

        LinkedList<Integer> lst = new LinkedList<>(); // non - contiguious manner -> DLL
        lst.add(2);
        lst.add(3);
        lst.add(4);
        lst.add(1);


        System.out.println(arrList);
        System.out.println(lst);



    }

}