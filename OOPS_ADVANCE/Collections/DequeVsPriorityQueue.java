package OOPS_ADVANCE.Collections;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.PriorityQueue;

public class DequeVsPriorityQueue {
    public static void main(String[] args) {
        Deque<Integer> dq = new ArrayDeque<>() ;
        // all the below opertaion is done in O(1) ; 
        dq.addFirst(2); // 2 
        dq.addLast(3); // 2 3
        dq.addLast(5); // 2 3 5 
        dq.addFirst(7); // 7 2 3 5
        System.out.println(dq);
        
        System.out.println("Now perform Remove Operation");
        dq.removeFirst() ; // 2 3 5
        System.out.println(dq);

        dq.removeLast() ; // 2 3 
        System.out.println(dq);


        PriorityQueue<Integer> pq = new PriorityQueue<>() ;
        pq.add(12);
        pq.add(23);
        pq.add(42);
        pq.add(22);
        pq.add(342);
        pq.add(12);
        pq.add(1);

        // System.out.println(pq); // give you random order ;

        // if you want sorted order 
        while(pq.size() > 0){
            // int top = pq.poll();
            // pq.remove();  this  will remove the element from the top and print the 
            // top element
            System.out.println(pq.remove() + " "); // if will give you min heap order
        }

        

    }
}
