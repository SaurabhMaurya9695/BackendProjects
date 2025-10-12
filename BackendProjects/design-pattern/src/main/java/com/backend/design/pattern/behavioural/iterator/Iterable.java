package com.backend.design.pattern.behavioural.iterator;

/**
 * Generic Iterable interface for collections that can be iterated over.
 * This interface defines the contract for objects that can provide iterators.
 * <p>
 * The Iterable interface allows objects to be the target of the "for-each loop"
 * statement and provides a standard way to obtain an iterator.
 *
 * @param <T> the type of elements returned by the iterator
 * @author Saurabh Maurya
 */
public interface Iterable<T> {

    /**
     * Returns an iterator over elements of type T.
     *
     * @return an Iterator that can traverse the collection
     */
    Iterator<T> getIterator();
}
