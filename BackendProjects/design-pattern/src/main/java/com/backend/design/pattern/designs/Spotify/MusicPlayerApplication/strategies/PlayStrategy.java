package com.backend.design.pattern.designs.Spotify.MusicPlayerApplication.strategies;

import com.backend.design.pattern.designs.Spotify.MusicPlayerApplication.models.Playlist;
import com.backend.design.pattern.designs.Spotify.MusicPlayerApplication.models.Song;

public interface PlayStrategy {

    void setPlaylist(Playlist playlist);

    Song next();

    boolean hasNext();

    Song previous();

    boolean hasPrevious();

    default void addToNext(Song song) {
    }
}
