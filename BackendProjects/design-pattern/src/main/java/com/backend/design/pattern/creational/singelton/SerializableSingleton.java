package com.backend.design.pattern.creational.singelton;

import java.io.Serial;
import java.io.Serializable;

/**
 * Let's say your singleton has implemented serialization. Now what will happen if you serialize object and deserialize.
 * During de-serialization it will create the new object every time if we go in traditional way.
 * To resolve it add readResolve method which will ensure that during deserialize we return same instance.
 * <p>
 * Check Main class for violation example
 *
 * @author : Saurabh Maurya
 */
public class SerializableSingleton implements Serializable {

    private static SerializableSingleton instance = null;

    private SerializableSingleton() {

    }

    public static SerializableSingleton getInstance() {
        if (instance == null) {
            instance = new SerializableSingleton();
        }
        return instance;
    }

    /**
     * This is the key method which is responsible during deserialization process
     * This method get invoked, and we are simply returning already created object
     */
    @Serial
    protected Object readResolve() {
        return instance;
    }
}
