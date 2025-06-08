package com.backend.design.pattern.designs.DocumentEditor.goodDesign.saveLLD;

public class SaveToDb extends Persistence {

    @Override
    public void save(String s) {
        System.out.println("Saving this " + s + " file to Db");
    }
}
