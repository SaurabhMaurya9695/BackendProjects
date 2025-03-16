package com.backend;

import com.backend.design.pattern.singelton.Singleton;
import com.backend.design.principles.App;

public class EntryPoint {

    public static void main(String[] args) {

        Singleton _INSTANCE = Singleton.getInstance();
        System.out.println(_INSTANCE.hashCode());

        App app = new App();
        app.checkHashCode();


    }
}
