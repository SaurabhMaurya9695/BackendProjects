package com.backend.design.pattern.designs.DocumentEditor.goodDesign.saveLLD;

public class SaveToFile extends Persistence {

    @Override
    public void save(String s) {
        System.out.println("Saving this file " + s + "  to FileSystem !! ");
    }
}
