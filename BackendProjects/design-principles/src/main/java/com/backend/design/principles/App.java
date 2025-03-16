package com.backend.design.principles;

import com.backend.design.pattern.singelton.Singleton;

public class App {

    public App() {

    }

    public void checkHashCode() {
        Singleton Instance = Singleton.getInstance();
        System.out.println(Instance.hashCode());

    }
}
