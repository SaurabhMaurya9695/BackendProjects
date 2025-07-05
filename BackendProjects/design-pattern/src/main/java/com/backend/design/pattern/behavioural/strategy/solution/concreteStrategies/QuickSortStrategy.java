package com.backend.design.pattern.behavioural.strategy.solution.concreteStrategies;

import com.backend.design.pattern.behavioural.strategy.solution.SortingStrategy;

public class QuickSortStrategy implements SortingStrategy {

    @Override
    public void sort(int[] array) {
        // Implement Quick Sort algorithm
        System.out.println("Sorting using Quick Sort");
    }
}