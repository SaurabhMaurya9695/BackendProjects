package com.backend.design.pattern.singelton;

import java.io.Serial;
import java.io.Serializable;

public class Singleton implements Serializable {

    // 1 : The volatile keyword ensures that the object is fully initialized before any thread reads it.
    // 2 : volatile ensures that all threads see the latest value of instance and don't use an outdated or partially
    //     constructed object.
    // 3 : Object creation is fully completed before any other thread uses it.
    private static volatile Singleton instance;

    private Singleton() {
        if (instance != null) {
            throw new RuntimeException("Use getInstance() method to create");
        }
    }

    public static Singleton getInstance() {
        if (instance == null) {
            synchronized (Singleton.class) {
                if (instance == null) {
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }

    @Serial
    protected Object readResolve() {
        return getInstance();
    }
}
