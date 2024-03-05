package OOPS_ADVANCE.Collections;

import java.util.*;

public class SetCollections {
    public static void main(String[] args) {
        
        // we have three type of sets 
        // 1 : HashSet 
        // 2 : LinkedHashSet
        // 3 : TreeSet

        // Syntax 1 : Set<Integer> hashSet = new HashSet<>();
        // Syntax 2 : HashSet<Integer> hashSet = new HashSet<>();

        Set<Integer> hashSet = new HashSet<>();
        hashSet.add(30);
        hashSet.add(20);
        hashSet.add(10);
        hashSet.add(40);
        hashSet.add(20);
        hashSet.add(30);
        // System.out.println(hashSet);

        // Syntax 1 : Set<Integer> linkedHashSet = new LinkedHashSet<>();
        // Syntax 2 : LinkedHashSet<Integer> hashSet = new LinkedHashSet<>();
        Set<Integer> linkedHashSet = new LinkedHashSet<>();
        linkedHashSet.add(30);
        linkedHashSet.add(20);
        linkedHashSet.add(10);
        linkedHashSet.add(40);
        linkedHashSet.add(20);
        linkedHashSet.add(30);
        // System.out.println(linkedHashSet);

        // Syntax 1 : Set<Integer> treeSet = new TreeSet<>();
        // Syntax 2 : TreeSet<Integer> hashSet = new TreeSet<>();
        Set<Integer> treeSet = new TreeSet<>();
        treeSet.add(30);
        treeSet.add(20);
        treeSet.add(10);
        treeSet.add(40);
        treeSet.add(20);
        treeSet.add(30);
        // System.out.println(treeSet);

        //using forEach Method
        // hashSet.forEach(x -> System.out.print(x + " "));
        // System.out.println();
        // linkedHashSet.forEach(x -> System.out.print(x + " "));
        // System.out.println();
        // treeSet.forEach(x -> System.out.print(x + " "));
        // System.out.println();

        //using forEach Loop
        for (Integer val : hashSet) {
            System.out.print(val + " ");
        }
        System.out.println();

        for (Integer val : linkedHashSet) {
            System.out.print(val + " ");
        }
        System.out.println();

        for (Integer val : treeSet) {
            System.out.print(val + " ");
        }
        System.out.println();

    }
}