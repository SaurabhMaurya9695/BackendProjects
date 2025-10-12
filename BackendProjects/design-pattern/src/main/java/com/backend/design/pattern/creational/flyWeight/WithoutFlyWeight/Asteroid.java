package com.backend.design.pattern.creational.flyWeight.WithoutFlyWeight;

class Asteroid {

    // Intrinsic properties (same for many asteroids) - DUPLICATED FOR EACH OBJECT
    private int length;
    private int width;
    private int weight;
    private String color;
    private String texture;
    private String material;

    // Extrinsic properties (unique for each asteroid)
    private int posX, posY;
    private int velocityX, velocityY;

    public Asteroid(int l, int w, int wt, String col, String tex, String mat, int posX, int posY, int velX, int velY) {
        this.length = l;
        this.width = w;
        this.weight = wt;
        this.color = col;
        this.texture = tex;
        this.material = mat;
        this.posX = posX;
        this.posY = posY;
        this.velocityX = velX;
        this.velocityY = velY;
    }

    public void render() {
        System.out.println(
                "Rendering " + color + ", " + texture + ", " + material + " asteroid at (" + posX + "," + posY
                        + ") Size: " + length + "x" + width + " Velocity: (" + velocityX + ", " + velocityY + ")");
    }

    // Calculate approximate memory usage per object
    public static long getMemoryUsage() {
        return Integer.BYTES * 7 +                // length, width, weight, x, y, velocityX, velocityY
                40 * 3;                            // Approximate string data (assuming average 10 chars each)
    }
}
