package com.backend.design.pattern.behavioural.iterator.models;

/**
 * Represents a song with title and artist information.
 * This class is used as the data element in the Playlist collection
 * to demonstrate the Iterator pattern with custom objects.
 * 
 * @author Saurabh Maurya
 */
public class Song {
    
    /** The title of the song */
    public String title;
    
    /** The artist who performed the song */
    public String artist;

    /**
     * Constructs a new Song with the specified title and artist.
     * 
     * @param title the title of the song
     * @param artist the artist who performed the song
     */
    public Song(String title, String artist) {
        this.title = title;
        this.artist = artist;
    }
    
    /**
     * Returns the title of the song.
     * 
     * @return the song title
     */
    public String getTitle() {
        return title;
    }
    
    /**
     * Returns the artist of the song.
     * 
     * @return the song artist
     */
    public String getArtist() {
        return artist;
    }
    
    /**
     * Returns a string representation of the song.
     * 
     * @return a formatted string showing the song title and artist
     */
    @Override
    public String toString() {
        return "\"" + title + "\" by " + artist;
    }
    
    /**
     * Checks if this song is equal to another object.
     * Two songs are considered equal if they have the same title and artist.
     * 
     * @param obj the object to compare with
     * @return true if the songs are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Song song = (Song) obj;
        return title.equals(song.title) && artist.equals(song.artist);
    }
    
    /**
     * Returns a hash code for this song.
     * 
     * @return the hash code based on title and artist
     */
    @Override
    public int hashCode() {
        return title.hashCode() * 31 + artist.hashCode();
    }
}
