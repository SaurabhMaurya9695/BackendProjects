package OOPS_ADVANCE.Collections;

import java.util.*;

public class MapCollections {
    public static void main(String[] args) {
        // we have three type of maps 
        // 1 : HashMap 
        // 2 : LinkedHashMap
        // 3 : TreeMap

        // they Store KeyValue things 
        // Syntax 1 : Map<K , V> hashMap = new HashMap<>();
        // Syntax 2 : HashMap<K , V> hashMap = new HashMap<>();

        Map<String , Integer> hashMap = new HashMap<>();
        hashMap.put("DemonSlayer", 3);
        hashMap.put("Figher", 4);
        hashMap.put("Figher", 4);
        hashMap.put("Shiataan", 3);
        hashMap.put("Leo", 5);
        hashMap.put(null, 40);
        hashMap.put("DoctorG", null);


        Map<String , Integer> linkedHashMap = new LinkedHashMap<>();
        linkedHashMap.put("DemonSlayer", 3);
        linkedHashMap.put("Figher", 4);
        linkedHashMap.put("Figher", 4);
        linkedHashMap.put("Shiataan", 3);
        linkedHashMap.put("Leo", 5);
        linkedHashMap.put(null, 40);
        linkedHashMap.put("DoctorG", null);

        Map<String , Integer> treeMap = new TreeMap<>();
        treeMap.put("DemonSlayer", 3);
        treeMap.put("Figher", 4);
        treeMap.put("Figher", 5);
        treeMap.put("Shiataan", 3);
        treeMap.put("Leo", 5);
        // treeMap.put(null, 40); // in TreeMap , Key should not be null 
        treeMap.put("DoctorG", null);

        // print 
        for (String val : hashMap.keySet()) {
            System.out.print(val + " " + hashMap.get(val) + " ");
        }
        System.out.println();

        for (String val : linkedHashMap.keySet()) {
            System.out.print(val + " " + linkedHashMap.get(val) + " ");
        }
        System.out.println();

        for (String val : treeMap.keySet()) {
            System.out.print(val + " " + treeMap.get(val) + " ");
        }
        System.out.println();

    }
}
