package com.backend.design.pattern.creational.flyWeight.WithFlyWeight;

import java.util.HashMap;
import java.util.Map;

// Flyweight Factory
class AsteroidFactory {

    private static Map<String, AsteroidFlyweight> flyweights = new HashMap<>();

    public static AsteroidFlyweight getAsteroid(int length, int width, int weight, String color, String texture,
            String material) {

        String key = length + "_" + width + "_" + weight + "_" + color + "_" + texture + "_" + material;

        if (!flyweights.containsKey(key)) {
            flyweights.put(key, new AsteroidFlyweight(length, width, weight, color, texture, material));
        }

        return flyweights.get(key);
    }

    public static int getFlyweightCount() {
        return flyweights.size();
    }

    public static long getTotalFlyweightMemory() {
        return flyweights.size() * AsteroidFlyweight.getMemoryUsage();
    }

    public static void cleanup() {
        flyweights.clear();
    }
}
