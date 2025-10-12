package com.backend.design.pattern.behavioural.iterator.collections;

import com.backend.design.pattern.behavioural.iterator.Iterator;
import com.backend.design.pattern.behavioural.iterator.Iterable;
import com.backend.design.pattern.behavioural.iterator.iterators.LinkedListIterator;

/**
 * A simple LinkedList implementation that demonstrates the Iterator pattern.
 * This class represents a singly linked list where each node contains an integer value
 * and a reference to the next node.
 * <p>
 * The LinkedList implements the Iterable interface, allowing it to provide
 * iterators for traversing its elements.
 *
 * @author Saurabh Maurya
 */
public class LinkedList implements Iterable<Integer> {

    /**
     * The data stored in this node
     */
    public int data;

    /**
     * Reference to the next node in the list
     */
    public LinkedList next;

    /**
     * Constructs a new LinkedList node with the specified value.
     *
     * @param value the integer value to store in this node
     */
    public LinkedList(int value) {
        this.data = value;
        this.next = null;
    }

    /**
     * Returns an iterator over the elements in this linked list.
     * The iterator will traverse the list from the current node to the end.
     *
     * @return an Iterator that traverses the linked list
     */
    @Override
    public Iterator<Integer> getIterator() {
        return new LinkedListIterator(this);
    }

    /**
     * Adds a new node with the specified value to the end of the list.
     *
     * @param value the value to add to the end of the list
     */
    public void add(int value) {
        if (next == null) {
            next = new LinkedList(value);
        } else {
            next.add(value);
        }
    }

    /**
     * Returns the size of the linked list starting from this node.
     *
     * @return the number of nodes in the list
     */
    public int size() {
        if (next == null) {
            return 1;
        }
        return 1 + next.size();
    }

    /**
     * Returns a string representation of the linked list.
     *
     * @return a string showing all values in the list
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        LinkedList current = this;
        while (current != null) {
            sb.append(current.data);
            if (current.next != null) {
                sb.append(" → ");
            }
            current = current.next;
        }
        return sb.toString();
    }
}
