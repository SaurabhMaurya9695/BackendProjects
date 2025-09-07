package com.backend.design.pattern.designs.Zepto.Managers;

import com.backend.design.pattern.common.Utility;
import com.backend.design.pattern.designs.Zepto.Store.DarkStore;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Singleton manager responsible for handling {@link DarkStore} registration
 * and retrieving nearby stores based on user location.
 */
public class DarkStoreManager {

    private static DarkStoreManager _instance = null;
    private final List<DarkStore> _darkStores;

    private DarkStoreManager() {
        _darkStores = new ArrayList<>();
    }

    /**
     * Returns the singleton instance of {@link DarkStoreManager}.
     *
     * @return the singleton instance
     */
    public static synchronized DarkStoreManager getInstance() {
        if (_instance == null) {
            _instance = new DarkStoreManager();
        }
        return _instance;
    }

    /**
     * Registers a new {@link DarkStore} in the system.
     *
     * @param darkStore the dark store to be registered
     */
    public void registerDarkStore(DarkStore darkStore) {
        System.out.println("[DarkStoreManager] Dark Store Registered ..");
        _darkStores.add(darkStore);
    }

    /**
     * Finds nearby {@link DarkStore} objects within a given maximum distance
     * from the user's location. Results are sorted by proximity.
     *
     * @param ux          user's X coordinate
     * @param uy          user's Y coordinate
     * @param maxDistance maximum allowed distance
     * @return a sorted list of nearby dark stores
     */
    public List<DarkStore> getNearbyDarkStores(double ux, double uy, double maxDistance) {
        List<Utility.Pair<Double, DarkStore>> distList = new ArrayList<>();

        // Calculate distance and filter
        for (DarkStore store : _darkStores) {
            double distance = store.distanceTo(ux, uy);
            if (distance <= maxDistance) {
                distList.add(new Utility.Pair<>(distance, store));
            }
        }

        // Sort by distance
        distList.sort(Comparator.comparingDouble(pair -> pair.left));

        // Extract and return only DarkStore objects
        List<DarkStore> nearbyStores = new ArrayList<>();
        for (Utility.Pair<Double, DarkStore> pair : distList) {
            nearbyStores.add(pair.right);
        }

        return nearbyStores;
    }
}
