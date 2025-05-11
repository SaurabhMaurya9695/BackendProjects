package com.backend.dsa.atoz.hashmapAndHeaps;

import javax.swing.CellEditor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Given that the list of employee , in the given example A is reporting to C , and B is reporting to C and C is
 * reporting to F, and same goes on ...
 * we have to find the ceo of the company
 */
public class FindCEO_08 {

    public FindCEO_08(HashMap<String, String> employee) {
        findCeo(employee);
    }

    private void findCeo(HashMap<String, String> employee) {
        HashMap<String, HashSet<String>> map = new HashMap<>();
        String ceo = "";
        for (Map.Entry<String, String> entry : employee.entrySet()) {
            String emp = entry.getKey();
            String manager = entry.getValue();
            if (map.containsKey(emp)) {
                ceo = emp;
            } else {
                if (map.containsKey(manager)) {
                    map.get(manager).add(emp);
                } else {
                    HashSet<String> set = new HashSet<>();
                    set.add(emp);
                    map.put(manager, set);
                }
            }
        }

        System.out.println("CEO of the company is " + ceo);
    }
}
