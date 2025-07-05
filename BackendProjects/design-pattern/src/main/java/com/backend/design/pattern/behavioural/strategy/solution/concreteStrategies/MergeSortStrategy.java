package com.backend.design.pattern.behavioural.strategy.solution.concreteStrategies;

import com.backend.design.pattern.behavioural.strategy.solution.SortingStrategy;

public class MergeSortStrategy implements SortingStrategy {

    @Override
    public void sort(int[] array) {
        // Implement Merge Sort algorithm
        System.out.println("Sorting using Merge Sort");
    }
}
