package com.backend.design.pattern.behavioural.iterator.collections;

import com.backend.design.pattern.behavioural.iterator.Iterator;
import com.backend.design.pattern.behavioural.iterator.Iterable;
import com.backend.design.pattern.behavioural.iterator.models.Song;
import com.backend.design.pattern.behavioural.iterator.iterators.PlaylistIterator;

import java.util.ArrayList;
import java.util.List;

/**
 * A Playlist implementation that demonstrates the Iterator pattern.
 * This class represents a collection of songs that can be iterated over
 * using the custom Iterator interface.
 * <p>
 * The Playlist uses an internal ArrayList to store songs and provides
 * an iterator for traversing the song collection.
 *
 * @author Saurabh Maurya
 */
public class Playlist implements Iterable<Song> {

    /**
     * The internal list storing the songs
     */
    public List<Song> _songs;

    /**
     * Constructs an empty playlist.
     */
    public Playlist() {
        // Initialize with empty list (already done above)
        _songs = new ArrayList<>();
    }

    /**
     * Constructs a playlist with the specified name.
     *
     * @param name the name of the playlist
     */
    public Playlist(String name) {
        this(); // Initialize empty playlist
        // Note: name is not stored in this simple implementation
        // but could be added as a field if needed
    }

    /**
     * Adds a song to the playlist.
     *
     * @param song the song to add to the playlist
     */
    public void addSong(Song song) {
        if (song != null) {
            _songs.add(song);
        }
    }

    /**
     * Adds a song to the playlist by creating a new Song object.
     *
     * @param title  the title of the song
     * @param artist the artist of the song
     */
    public void addSong(String title, String artist) {
        addSong(new Song(title, artist));
    }

    /**
     * Removes a song from the playlist.
     *
     * @param song the song to remove
     * @return true if the song was removed, false if it wasn't found
     */
    public boolean removeSong(Song song) {
        return _songs.remove(song);
    }

    /**
     * Removes a song from the playlist at the specified index.
     *
     * @param index the index of the song to remove
     * @return the removed song
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    public Song removeSong(int index) {
        return _songs.remove(index);
    }

    /**
     * Returns the song at the specified index.
     *
     * @param index the index of the song to return
     * @return the song at the specified index
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    public Song getSong(int index) {
        return _songs.get(index);
    }

    /**
     * Returns the number of songs in the playlist.
     *
     * @return the number of songs in the playlist
     */
    public int size() {
        return _songs.size();
    }

    /**
     * Returns true if the playlist is empty.
     *
     * @return true if the playlist contains no songs
     */
    public boolean isEmpty() {
        return _songs.isEmpty();
    }

    /**
     * Clears all songs from the playlist.
     */
    public void clear() {
        _songs.clear();
    }

    /**
     * Checks if the playlist contains the specified song.
     *
     * @param song the song to check for
     * @return true if the playlist contains the song
     */
    public boolean contains(Song song) {
        return _songs.contains(song);
    }

    /**
     * Returns an iterator over the songs in this playlist.
     * The iterator will traverse the songs in the order they were added.
     *
     * @return an Iterator that traverses the playlist
     */
    @Override
    public Iterator<Song> getIterator() {
        return new PlaylistIterator(_songs);
    }

    /**
     * Returns a string representation of the playlist.
     *
     * @return a string showing all songs in the playlist
     */
    @Override
    public String toString() {
        if (_songs.isEmpty()) {
            return "Empty Playlist";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Playlist (").append(_songs.size()).append(" songs):\n");

        int index = 1;
        for (Song song : _songs) {
            sb.append("  ").append(index++).append(". ").append(song).append("\n");
        }

        return sb.toString().trim();
    }
}
