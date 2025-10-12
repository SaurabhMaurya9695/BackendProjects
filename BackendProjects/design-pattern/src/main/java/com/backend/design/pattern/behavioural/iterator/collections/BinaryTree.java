package com.backend.design.pattern.behavioural.iterator.collections;

import com.backend.design.pattern.behavioural.iterator.Iterator;
import com.backend.design.pattern.behavioural.iterator.Iterable;
import com.backend.design.pattern.behavioural.iterator.iterators.BinaryTreeInorderIterator;

/**
 * A simple Binary Tree implementation that demonstrates the Iterator pattern.
 * This class represents a binary tree node where each node contains an integer value
 * and references to left and right child nodes.
 * <p>
 * The BinaryTree implements the Iterable interface, allowing it to provide
 * iterators for traversing its elements in different orders (inorder by default).
 *
 * @author Saurabh Maurya
 */
public class BinaryTree implements Iterable<Integer> {

    /**
     * The data stored in this node
     */
    public int data;

    /**
     * Reference to the left child node
     */
    public BinaryTree left;

    /**
     * Reference to the right child node
     */
    public BinaryTree right;

    /**
     * Constructs a new BinaryTree node with the specified value.
     *
     * @param value the integer value to store in this node
     */
    public BinaryTree(int value) {
        this.data = value;
        this.left = null;
        this.right = null;
    }

    /**
     * Returns an inorder iterator over the elements in this binary tree.
     * Inorder traversal visits nodes in the order: left subtree, root, right subtree.
     * For a binary search tree, this produces values in ascending order.
     *
     * @return an Iterator that traverses the tree in inorder
     */
    @Override
    public Iterator<Integer> getIterator() {
        return new BinaryTreeInorderIterator(this);
    }

    /**
     * Inserts a new value into the binary search tree.
     * Values less than the current node go to the left subtree,
     * values greater than or equal go to the right subtree.
     *
     * @param value the value to insert into the tree
     */
    public void insert(int value) {
        if (value < this.data) {
            if (left == null) {
                left = new BinaryTree(value);
            } else {
                left.insert(value);
            }
        } else {
            if (right == null) {
                right = new BinaryTree(value);
            } else {
                right.insert(value);
            }
        }
    }

    /**
     * Searches for a value in the binary search tree.
     *
     * @param value the value to search for
     * @return true if the value is found, false otherwise
     */
    public boolean search(int value) {
        if (value == this.data) {
            return true;
        } else if (value < this.data && left != null) {
            return left.search(value);
        } else if (value > this.data && right != null) {
            return right.search(value);
        }
        return false;
    }

    /**
     * Returns the height of the binary tree.
     *
     * @return the height of the tree (number of edges from root to deepest leaf)
     */
    public int height() {
        int leftHeight = (left != null) ? left.height() : -1;
        int rightHeight = (right != null) ? right.height() : -1;
        return 1 + Math.max(leftHeight, rightHeight);
    }

    /**
     * Returns the number of nodes in the binary tree.
     *
     * @return the total number of nodes in the tree
     */
    public int size() {
        int leftSize = (left != null) ? left.size() : 0;
        int rightSize = (right != null) ? right.size() : 0;
        return 1 + leftSize + rightSize;
    }

    /**
     * Returns a string representation of the binary tree structure.
     * This method provides a visual representation of the tree.
     *
     * @return a string showing the tree structure
     */
    @Override
    public String toString() {
        return toString("", true, true);
    }

    /**
     * Helper method for creating a visual representation of the tree.
     *
     * @param prefix the prefix for the current line
     * @param isLast whether this is the last child of its parent
     * @param isRoot whether this is the root node
     * @return a string representation of the tree from this node
     */
    private String toString(String prefix, boolean isLast, boolean isRoot) {
        StringBuilder result = new StringBuilder();

        if (!isRoot) {
            result.append(prefix);
            result.append(isLast ? "└── " : "├── ");
        }
        result.append(data).append("\n");

        String childPrefix = prefix + (isLast && !isRoot ? "    " : "│   ");

        if (left != null && right != null) {
            result.append(left.toString(childPrefix, false, false));
            result.append(right.toString(childPrefix, true, false));
        } else if (left != null) {
            result.append(left.toString(childPrefix, true, false));
        } else if (right != null) {
            result.append(right.toString(childPrefix, true, false));
        }

        return result.toString();
    }
}
