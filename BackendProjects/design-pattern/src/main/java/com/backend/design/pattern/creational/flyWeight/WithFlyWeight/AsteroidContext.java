package com.backend.design.pattern.creational.flyWeight.WithFlyWeight;

// Context - Stores EXTRINSIC state only
class AsteroidContext {

    private AsteroidFlyweight flyweight;
    private int posX, posY; // 8 bytes (position)
    private int velocityX, velocityY; // 8 bytes (velocity)

    public AsteroidContext(AsteroidFlyweight fw, int posX, int posY, int velX, int velY) {
        this.flyweight = fw;
        this.posX = posX;
        this.posY = posY;
        this.velocityX = velX;
        this.velocityY = velY;
    }

    public void render() {
        flyweight.render(posX, posY, velocityX, velocityY);
    }

    public static long getMemoryUsage() {
        return 8 + Integer.BYTES * 4; // approximate pointer + ints
    }
}
