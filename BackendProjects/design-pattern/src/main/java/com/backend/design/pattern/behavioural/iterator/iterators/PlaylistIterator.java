package com.backend.design.pattern.behavioural.iterator.iterators;

import com.backend.design.pattern.behavioural.iterator.Iterator;
import com.backend.design.pattern.behavioural.iterator.models.Song;

import java.util.List;

/**
 * Concrete iterator implementation for Playlist.
 * This iterator traverses the playlist's song collection using index-based access.
 * 
 * The iterator maintains an index that points to the current position in the
 * song list and increments the index on each call to next().
 * 
 * @author Saurabh Maurya
 */
public class PlaylistIterator implements Iterator<Song> {
    
    /** The list of songs to iterate over */
    private List<Song> songs;
    
    /** The current index in the iteration */
    private int index = 0;

    /**
     * Constructs a new PlaylistIterator for the specified list of songs.
     * 
     * @param songs the list of songs to iterate over
     */
    public PlaylistIterator(List<Song> songs) {
        this.songs = songs;
    }

    /**
     * Returns true if the iteration has more elements.
     * This method checks if the current index is within the bounds of the song list.
     * 
     * @return true if there are more songs to iterate over
     */
    @Override
    public boolean hasNext() {
        return index < songs.size();
    }

    /**
     * Returns the next song in the iteration and advances the iterator.
     * 
     * @return the song at the current index
     * @throws java.util.NoSuchElementException if there are no more songs
     */
    @Override
    public Song next() {
        if (!hasNext()) {
            throw new java.util.NoSuchElementException("No more songs in the playlist");
        }
        
        return songs.get(index++);
    }
    
    /**
     * Returns the current index of the iterator.
     * This method can be useful for tracking the current position during iteration.
     * 
     * @return the current index (0-based)
     */
    public int getCurrentIndex() {
        return index - 1; // Return the index of the last returned element
    }
    
    /**
     * Resets the iterator to the beginning of the playlist.
     * After calling this method, the iterator will start from the first song again.
     */
    public void reset() {
        index = 0;
    }
    
    /**
     * Returns the total number of songs in the playlist.
     * 
     * @return the size of the song list
     */
    public int getTotalSongs() {
        return songs.size();
    }
}
