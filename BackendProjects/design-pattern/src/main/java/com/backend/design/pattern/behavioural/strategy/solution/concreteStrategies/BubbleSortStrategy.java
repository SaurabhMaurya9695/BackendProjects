package com.backend.design.pattern.behavioural.strategy.solution.concreteStrategies;

import com.backend.design.pattern.behavioural.strategy.solution.SortingStrategy;

public class BubbleSortStrategy implements SortingStrategy {

    @Override
    public void sort(int[] array) {
        // Implement Bubble Sort algorithm
        System.out.println("Sorting using Bubble Sort");
    }
}
