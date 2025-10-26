package com.backend.system.design.LLD.Algo;

// The list only needs to be generic over the element type E.
public class DoublyLinkedList<E> {

    // Dummy head and tail nodes to simplify operations (no need to check for null).
    private final DLLNode<E> _head;
    private final DLLNode<E> _tail;

    // The capacity/size should be an int.
    private final int capacity;
    private int currentSize;

    public DoublyLinkedList(int capacity) {
        this.capacity = capacity;
        this.currentSize = 0;

        // 1. Initialize the dummy nodes. They don't store actual data (hence 'null').
        this._head = new DLLNode<>(null);
        this._tail = new DLLNode<>(null);

        // 2. Link the dummy nodes together. The list is initially empty.
        this._head.next = this._tail;
        this._tail.prev = this._head;
    }

    /**
     * Adds a node right after the head (making it the Most Recently Used).
     *
     * @param node The node to add.
     */
    public void addNodeToFront(DLLNode<E> node) {
        DLLNode<E> nextNode = _head.next;
        _head.next = node;
        node.prev = _head;

        node.next = nextNode;
        nextNode.prev = node;

        this.currentSize++;
    }

    /**
     * Removes a node from its current position.
     *
     * @param node The node to remove.
     */
    public void removeNode(DLLNode<E> node) {
        // Unlink the node from its neighbors - Imagine with 4 nodes first having
        // head and last, and you want to delete the 3rd node
        node.prev.next = node.next;
        node.next.prev = node.prev;

        // Decrement size only if it's a real element node (not the dummy head/tail)
        // In LRU, we assume you only call this on actual elements.
        currentSize--;
    }

    /**
     * Gets the key for eviction (the Least Recently Used node, which is before the tail).
     *
     * @return The DLLNode before the tail, or null if the list is empty.
     */
    public DLLNode<E> getLRUNode() {
        // Check if the list is empty (head's next is tail)
        if (_head.next == _tail) {
            return null;
        }
        return _tail.prev; // This is the LRU node
    }

    public int getCurrentSize() {
        return currentSize;
    }

    public int getCapacity() {
        return capacity;
    }
}