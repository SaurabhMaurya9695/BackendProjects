package com.backend.design.pattern.singelton;

/**
 * Singleton class implementing the Double-Checked Locking pattern to ensure thread safety while creating a single instance.
 *
 * <p>This implementation guarantees that:
 * <ul>
 *     <li>The instance is fully initialized before any thread accesses it.</li>
 *     <li>The volatile keyword ensures visibility of changes across threads.</li>
 *     <li>Synchronization is only used when necessary to improve performance.</li>
 * </ul>
 *
 * @author Saurabh Maurya
 */
public class MultiThreadSingleton {

    /**
     * The volatile keyword ensures that the instance is fully initialized before any thread reads it.
     */
    private static volatile MultiThreadSingleton instance;

    /**
     * Private constructor to prevent instantiation from other classes.
     */
    private MultiThreadSingleton() {
    }

    /**
     * Returns the singleton instance of {@code MultiThreadSingleton}, ensuring thread safety using double-checked locking.
     *
     * <p>The first null check prevents unnecessary synchronization once the instance is created.
     * The synchronized block ensures only one thread initializes the instance.
     * The second null check ensures that another thread does not create a new instance after acquiring the lock.</p>
     *
     * @return the singleton instance of {@code MultiThreadSingleton}
     */
    public static MultiThreadSingleton getInstance() {
        if (instance == null) {
            synchronized (MultiThreadSingleton.class) {
                if (instance == null) {
                    instance = new MultiThreadSingleton();
                }
            }
        }
        return instance;
    }
}
