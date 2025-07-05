package com.backend.design.pattern.behavioural.strategy.problem;

public class Sorting {

    // 1: It is not following Open close principles
    // 2: too many if blocks for different - different algorithms
    // 3: This is not a correct way of doing this
    public void sortArray(String algorithm) {
        if (algorithm.equals("BUBBLE SORT")) {
            System.out.println("Sorting array using " + algorithm);
        } else if (algorithm.equals("INSERTION SORT")) {
            System.out.println("Sorting array using " + algorithm);
        } else if (algorithm.equals("MERGE SORT")) {
            System.out.println("Sorting array using " + algorithm);
        } else {
            System.out.println("SORTING BY DEFAULT");
        }
    }
}
