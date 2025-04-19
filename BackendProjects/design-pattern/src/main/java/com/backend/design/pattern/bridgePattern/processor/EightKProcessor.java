package com.backend.design.pattern.bridgePattern.processor;

public class EightKProcessor extends VideoProcessor {

    @Override
    public void play(String fileName) {
        System.out.println("processing 8k video " + fileName);
    }
}
