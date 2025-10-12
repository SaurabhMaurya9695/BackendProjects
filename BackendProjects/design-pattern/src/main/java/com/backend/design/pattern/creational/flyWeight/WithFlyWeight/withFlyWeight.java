package com.backend.design.pattern.creational.flyWeight.WithFlyWeight;

public class withFlyWeight {

    public static void main(String[] args) {
        final int ASTEROID_COUNT = 1_000_000;

        System.out.println("\nTESTING WITH FLYWEIGHT PATTERN");
        SpaceGameWithFlyweight game = new SpaceGameWithFlyweight();

        game.spawnAsteroids(ASTEROID_COUNT);

        // Show first 5 asteroids to see the pattern
        game.renderAll();

        // Calculate and display memory usage
        long totalMemory = game.calculateMemoryUsage();

        System.out.println("\n=== MEMORY USAGE ===");
        System.out.println("Total asteroids: " + ASTEROID_COUNT);
        System.out.println("Memory per asteroid: " + AsteroidContext.getMemoryUsage() + " bytes");
        System.out.println("Total memory used: " + totalMemory + " bytes");
        System.out.println("Memory in MB: " + (totalMemory / (1024.0 * 1024.0)) + " MB");
    }
}