package com.backend.design.pattern.behavioural.iterator;

import com.backend.design.pattern.behavioural.iterator.collections.BinaryTree;
import com.backend.design.pattern.behavioural.iterator.collections.LinkedList;
import com.backend.design.pattern.behavioural.iterator.collections.Playlist;
import com.backend.design.pattern.behavioural.iterator.models.Song;

/**
 * Demonstration class for the Iterator Pattern implementation.
 * <p>
 * The Iterator Pattern provides a way to access the elements of an aggregate object
 * sequentially without exposing its underlying representation. This pattern decouples
 * the client code from the internal structure of the collection.
 * <p>
 * This demo showcases three different collection types:
 * 1. LinkedList - A simple linked list of integers
 * 2. BinaryTree - A binary tree with inorder traversal
 * 3. Playlist - A collection of songs
 * <p>
 * Key Benefits of Iterator Pattern:
 * - Uniform interface for traversing different collection types
 * - Decouples iteration logic from collection implementation
 * - Supports multiple simultaneous iterations
 * - Simplifies the collection interface
 *
 * @author Saurabh Maurya
 */
public class IteratorPatternDemo {

    public static void main(String[] args) {
        System.out.println("=== Iterator Pattern Demonstration ===\n");

        // Demonstrate LinkedList iteration
        demonstrateLinkedListIteration();

        System.out.println("\n" + "=".repeat(50) + "\n");

        // Demonstrate BinaryTree iteration
        demonstrateBinaryTreeIteration();

        System.out.println("\n" + "=".repeat(50) + "\n");

        // Demonstrate Playlist iteration
        demonstratePlaylistIteration();

        System.out.println("\n=== Iterator Pattern Benefits ===");
        System.out.println("✓ Uniform interface for different collections");
        System.out.println("✓ Hides internal collection structure");
        System.out.println("✓ Supports multiple simultaneous iterations");
        System.out.println("✓ Easy to add new collection types");
        System.out.println("\n=== Demo Complete ===");
    }

    /**
     * Demonstrates iteration over a LinkedList.
     */
    private static void demonstrateLinkedListIteration() {
        System.out.println("🔗 LINKED LIST ITERATION DEMO");
        System.out.println("==============================");

        // Create LinkedList: 1 → 2 → 3
        LinkedList list = new LinkedList(1);
        list.next = new LinkedList(2);
        list.next.next = new LinkedList(3);

        System.out.println("LinkedList structure: " + list);
        System.out.println("Size: " + list.size());

        // Iterate using the iterator
        Iterator<Integer> iterator1 = list.getIterator();
        System.out.print("Iterating through LinkedList: ");
        while (iterator1.hasNext()) {
            System.out.print(iterator1.next() + " ");
        }
        System.out.println();

        // Demonstrate multiple iterations
        System.out.println("\nDemonstrating multiple simultaneous iterations:");
        Iterator<Integer> iter1 = list.getIterator();
        Iterator<Integer> iter2 = list.getIterator();

        System.out.println("Iterator 1 - First element: " + (iter1.hasNext() ? iter1.next() : "None"));
        System.out.println("Iterator 2 - All elements: ");
        while (iter2.hasNext()) {
            System.out.print("  " + iter2.next());
        }
        System.out.println();
        System.out.println("Iterator 1 - Remaining elements: ");
        while (iter1.hasNext()) {
            System.out.print("  " + iter1.next());
        }
        System.out.println();
    }

    /**
     * Demonstrates iteration over a BinaryTree.
     */
    private static void demonstrateBinaryTreeIteration() {
        System.out.println("🌳 BINARY TREE ITERATION DEMO");
        System.out.println("==============================");

        // Create BinaryTree:
        //    2
        //   / \
        //  1   3
        BinaryTree root = new BinaryTree(2);
        root.left = new BinaryTree(1);
        root.right = new BinaryTree(3);

        System.out.println("Binary Tree structure:");
        System.out.println(root);
        System.out.println("Tree height: " + root.height());
        System.out.println("Tree size: " + root.size());

        // Iterate using inorder traversal
        Iterator<Integer> iterator2 = root.getIterator();
        System.out.print("Inorder traversal: ");
        while (iterator2.hasNext()) {
            System.out.print(iterator2.next() + " ");
        }
        System.out.println();

        // Create a larger binary search tree
        System.out.println("\nCreating a larger Binary Search Tree:");
        BinaryTree bst = new BinaryTree(5);
        int[] values = { 3, 7, 2, 4, 6, 8, 1, 9 };
        for (int value : values) {
            bst.insert(value);
        }

        System.out.println("BST structure:");
        System.out.println(bst);

        System.out.print("BST inorder traversal (sorted): ");
        Iterator<Integer> bstIterator = bst.getIterator();
        while (bstIterator.hasNext()) {
            System.out.print(bstIterator.next() + " ");
        }
        System.out.println();
    }

    /**
     * Demonstrates iteration over a Playlist.
     */
    private static void demonstratePlaylistIteration() {
        System.out.println("🎵 PLAYLIST ITERATION DEMO");
        System.out.println("===========================");

        // Create playlist with songs
        Playlist playlist = new Playlist("My Favorites");
        playlist.addSong(new Song("Admirin You", "Karan Aujla"));
        playlist.addSong(new Song("Husn", "Anuv Jain"));
        playlist.addSong("Mockingbird", "Eminem");
        playlist.addSong("Tum Hi Ho", "Arijit Singh");
        playlist.addSong("Blinding Lights", "The Weeknd");

        System.out.println("Playlist details:");
        System.out.println("Size: " + playlist.size() + " songs");
        System.out.println("Is empty: " + playlist.isEmpty());
        System.out.println();

        // Iterate through all songs
        Iterator<Song> iterator3 = playlist.getIterator();
        System.out.println("All songs in playlist:");
        int songNumber = 1;
        while (iterator3.hasNext()) {
            Song song = iterator3.next();
            System.out.println("  " + songNumber++ + ". " + song);
        }

        // Demonstrate filtering during iteration
        System.out.println("\nFiltering songs by specific artist (Karan Aujla):");
        Iterator<Song> filterIterator = playlist.getIterator();
        while (filterIterator.hasNext()) {
            Song song = filterIterator.next();
            if (song.getArtist().equals("Karan Aujla")) {
                System.out.println("  ♪ " + song);
            }
        }

        // Demonstrate playlist operations
        System.out.println("\nPlaylist operations:");
        System.out.println("First song: " + playlist.getSong(0));
        System.out.println("Last song: " + playlist.getSong(playlist.size() - 1));

        // Add another song and show updated playlist
        playlist.addSong("Bohemian Rhapsody", "Queen");
        System.out.println("\nAfter adding 'Bohemian Rhapsody':");
        System.out.println("New size: " + playlist.size());

        Iterator<Song> updatedIterator = playlist.getIterator();
        System.out.println("Updated playlist:");
        songNumber = 1;
        while (updatedIterator.hasNext()) {
            System.out.println("  " + songNumber++ + ". " + updatedIterator.next());
        }
    }
}
