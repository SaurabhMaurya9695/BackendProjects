package com.backend.design.pattern.behavioural.iterator;

/**
 * Generic Iterator interface for traversing collections.
 * This interface defines the contract for iterating over elements of type T.
 * 
 * The Iterator pattern provides a way to access the elements of an aggregate 
 * object sequentially without exposing its underlying representation.
 *
 * @param <T> the type of elements returned by this iterator
 * 
 * @author Saurabh Maurya
 */
public interface Iterator<T> {
    
    /**
     * Returns true if the iteration has more elements.
     * 
     * @return true if the iteration has more elements, false otherwise
     */
    boolean hasNext();
    
    /**
     * Returns the next element in the iteration.
     * 
     * @return the next element in the iteration
     * @throws java.util.NoSuchElementException if the iteration has no more elements
     */
    T next();
}
