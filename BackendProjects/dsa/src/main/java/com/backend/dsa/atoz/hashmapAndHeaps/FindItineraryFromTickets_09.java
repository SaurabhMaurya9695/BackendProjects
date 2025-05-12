package com.backend.dsa.atoz.hashmapAndHeaps;

import com.backend.dsa.atoz.CommonUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * You have to find the starting point of this Itinerary : chennai -> bangalore, bombay -> delhi
 * goa -> chennai, delhi -> goa
 */
public class FindItineraryFromTickets_09 {

    public FindItineraryFromTickets_09(HashMap<String, String> itinerary) {
        findItineraryFromTickets(itinerary);
    }

    private void findItineraryFromTickets(HashMap<String, String> itinerary) {
        Map<String, Boolean> map = new HashMap<>();
        for (String key : itinerary.keySet()) {
            String src = key;
            String dest = itinerary.get(key);

            // jo city ko hum visit ni kre rhe dubara means waha se starting point hai
            map.put(dest, false);    // src
            if (!map.containsKey(src)) {
                // if it is true it means ki yeh hmara src point hai or abhi ye kisi ka dest banne jaa rha hai
                map.put(src, true);
            }
        }

        String startingPoint = "";
        for (String key : map.keySet()) {
            if (map.get(key)) {
                startingPoint = key;
                break;
            }
        }

        System.out.println("Starting point of Itinerary is : " + startingPoint);

        while (true) {
            if (map.containsKey(startingPoint)) {
                System.out.print(startingPoint + " -> ");
                startingPoint = itinerary.get(startingPoint);
            } else {
                System.out.print(" . ");
                break;
            }
        }
        System.out.println();
    }
}
