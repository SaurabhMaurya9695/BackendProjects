package com.backend.design.pattern.designs.DocumentEditor.goodDesign.addLogicLL;

public class AddText extends Add {

    @Override
    void add(String text) {
        System.out.println("Adding " + text + " to the documents ");
    }
}
