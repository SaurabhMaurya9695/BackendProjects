package com.backend.system.design.LLD.Algo;

/**
 * Doubly Linked List Node.
 * Used internally by DoublyLinkedList for maintaining order of elements.
 * 
 * @param <E> The type of element stored in this node
 */
public class DLLNode<E> {

    DLLNode<E> next;
    DLLNode<E> prev;
    E element;

    public DLLNode(E element) {
        this.element = element;
        this.next = null;
        this.prev = null;
    }

    /**
     * Gets the element stored in this node.
     * 
     * @return The element
     */
    public E getElement() {
        return element;
    }

    /**
     * Sets the element stored in this node.
     * 
     * @param element The element to store
     */
    public void setElement(E element) {
        this.element = element;
    }
}