package com.backend.design.pattern.behavioural.strategy.solution;

public class SortingContext {

    private SortingStrategy _sortingStrategy;

    // passing the Strategy here, and based on that Strategy, we are initializing its constructor and performing a
    // sorting for that strategy
    public SortingContext(SortingStrategy sortingStrategy) {
        this._sortingStrategy = sortingStrategy;
    }

    public void setSortingStrategy(SortingStrategy sortingStrategy) {
        this._sortingStrategy = sortingStrategy;
    }

    public void performSort(int[] array) {
        _sortingStrategy.sort(array);
    }
}
