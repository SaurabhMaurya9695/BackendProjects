package com.backend.design.pattern.behavioural.iterator.iterators;

import com.backend.design.pattern.behavioural.iterator.Iterator;
import com.backend.design.pattern.behavioural.iterator.collections.LinkedList;

/**
 * Concrete iterator implementation for LinkedList.
 * This iterator traverses the linked list from the starting node to the end,
 * following the next references.
 * <p>
 * The iterator maintains a reference to the current node and advances
 * through the list on each call to next().
 *
 * @author Saurabh Maurya
 */
public class LinkedListIterator implements Iterator<Integer> {

    /**
     * The current node in the iteration
     */
    private LinkedList current;

    /**
     * Constructs a new LinkedListIterator starting from the specified head node.
     *
     * @param head the starting node of the linked list
     */
    public LinkedListIterator(LinkedList head) {
        this.current = head;
    }

    /**
     * Returns true if the iteration has more elements.
     * This method checks if the current node is not null.
     *
     * @return true if there are more elements to iterate over
     */
    @Override
    public boolean hasNext() {
        return current != null;
    }

    /**
     * Returns the next element in the iteration and advances the iterator.
     *
     * @return the data value of the current node
     * @throws java.util.NoSuchElementException if there are no more elements
     */
    @Override
    public Integer next() {
        if (!hasNext()) {
            throw new java.util.NoSuchElementException("No more elements in the linked list");
        }

        int value = current.data;
        current = current.next;
        return value;
    }
}
