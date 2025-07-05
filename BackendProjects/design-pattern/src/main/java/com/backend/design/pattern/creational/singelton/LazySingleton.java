package com.backend.design.pattern.creational.singelton;

import java.io.Serializable;

/**
 * Lazy initialization mean application will create instance when it is requested.
 * However, this can be used when you have non-thread-safe application. If used in multi threading it might break,
 * Why? because your get instance method if invoked by two thread at same time then!!!!
 * <p>
 * When to use?  Non thread safe and creating common resource like db connection.
 *
 * @author : Saurabh Maurya
 */
public class LazySingleton implements Serializable {

    private static LazySingleton instance = null;

    private LazySingleton() {

    }

    public static LazySingleton getInstance() {
        if (instance == null) {
            instance = new LazySingleton();
        }
        return instance;
    }
}
