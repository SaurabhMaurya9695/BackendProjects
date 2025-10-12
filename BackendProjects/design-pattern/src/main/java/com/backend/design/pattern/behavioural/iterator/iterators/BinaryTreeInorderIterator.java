package com.backend.design.pattern.behavioural.iterator.iterators;

import com.backend.design.pattern.behavioural.iterator.Iterator;
import com.backend.design.pattern.behavioural.iterator.collections.BinaryTree;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Concrete iterator implementation for BinaryTree that performs inorder traversal.
 * Inorder traversal visits nodes in the order: left subtree, root, right subtree.
 * <p>
 * This iterator uses an iterative approach with a stack (Deque) to simulate
 * the recursive inorder traversal algorithm. The stack keeps track of nodes
 * to be processed.
 * <p>
 * For a binary search tree, inorder traversal produces elements in ascending order.
 *
 * @author Saurabh Maurya
 */
public class BinaryTreeInorderIterator implements Iterator<Integer> {

    /**
     * Stack to keep track of nodes during iteration
     */
    private Deque<BinaryTree> stack = new ArrayDeque<>();

    /**
     * Helper method to push all left children of a node onto the stack.
     * This method traverses down the left spine of the tree starting from
     * the given node and pushes all nodes onto the stack.
     *
     * @param node the starting node to push left children from
     */
    private void pushLefts(BinaryTree node) {
        while (node != null) {
            stack.push(node);
            node = node.left;
        }
    }

    /**
     * Constructs a new BinaryTreeInorderIterator starting from the specified root.
     * The constructor initializes the stack by pushing all left children
     * of the root onto the stack.
     *
     * @param root the root node of the binary tree to iterate over
     */
    public BinaryTreeInorderIterator(BinaryTree root) {
        pushLefts(root);
    }

    /**
     * Returns true if the iteration has more elements.
     * This method checks if the stack is not empty.
     *
     * @return true if there are more elements to iterate over
     */
    @Override
    public boolean hasNext() {
        return !stack.isEmpty();
    }

    /**
     * Returns the next element in the inorder traversal and advances the iterator.
     * <p>
     * The algorithm works as follows:
     * 1. Pop a node from the stack (this is the next node in inorder)
     * 2. If the popped node has a right child, push all left children
     * of the right subtree onto the stack
     * 3. Return the data of the popped node
     *
     * @return the data value of the next node in inorder traversal
     * @throws java.util.NoSuchElementException if there are no more elements
     */
    @Override
    public Integer next() {
        if (!hasNext()) {
            throw new java.util.NoSuchElementException("No more elements in the binary tree");
        }

        BinaryTree node = stack.pop();
        int value = node.data;

        // If the node has a right child, push all left children of the right subtree
        if (node.right != null) {
            pushLefts(node.right);
        }

        return value;
    }
}
