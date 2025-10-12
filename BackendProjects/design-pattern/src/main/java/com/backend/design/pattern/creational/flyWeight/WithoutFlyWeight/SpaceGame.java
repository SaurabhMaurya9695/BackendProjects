package com.backend.design.pattern.creational.flyWeight.WithoutFlyWeight;

import java.util.ArrayList;
import java.util.List;

class SpaceGame {
    private List<Asteroid> asteroids = new ArrayList<>();

    public void spawnAsteroids(int count) {
        System.out.println("\n=== Spawning " + count + " asteroids ===");

        String[] colors = { "Red", "Blue", "Gray" };
        String[] textures = { "Rocky", "Metallic", "Icy" };
        String[] materials = { "Iron", "Stone", "Ice" };
        int[] sizes = { 25, 35, 45 };

        for (int i = 0; i < count; i++) {
            int type = i % 3;

            asteroids.add(new Asteroid(sizes[type], sizes[type], sizes[type] * 10, colors[type], textures[type],
                    materials[type], 100 + i * 50,         // Simple x: 100, 150, 200, 250...
                    200 + i * 30,         // Simple y: 200, 230, 260, 290...
                    1,                    // All move right with velocity 1
                    2                     // All move down with velocity 2
            ));
        }

        System.out.println("Created " + asteroids.size() + " asteroid objects");
    }

    public void renderAll() {
        System.out.println("\n--- Rendering first 5 asteroids ---");
        for (int i = 0; i < Math.min(5, asteroids.size()); i++) {
            asteroids.get(i).render();
        }
    }

    public long calculateMemoryUsage() {
        return asteroids.size() * Asteroid.getMemoryUsage();
    }

    public int getAsteroidCount() {
        return asteroids.size();
    }
}
