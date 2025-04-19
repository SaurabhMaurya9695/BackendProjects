package com.backend.design.pattern.bridgePattern.processor;

public class fourKProcessor extends VideoProcessor {

    @Override
    public void play(String fileName) {
        System.out.println("4k processing file: " + fileName);
    }
}
