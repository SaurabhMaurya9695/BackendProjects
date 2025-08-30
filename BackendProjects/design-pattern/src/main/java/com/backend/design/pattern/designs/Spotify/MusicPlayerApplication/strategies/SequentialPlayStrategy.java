package com.backend.design.pattern.designs.Spotify.MusicPlayerApplication.strategies;

import com.backend.design.pattern.designs.Spotify.MusicPlayerApplication.models.Playlist;
import com.backend.design.pattern.designs.Spotify.MusicPlayerApplication.models.Song;

public class SequentialPlayStrategy implements PlayStrategy {

    private Playlist _currentPlaylist;
    private int _currentIndex;

    public SequentialPlayStrategy() {
        _currentPlaylist = null;
        _currentIndex = -1;
    }

    @Override
    public void setPlaylist(Playlist playlist) {
        _currentPlaylist = playlist;
        _currentIndex = -1;
    }

    @Override
    public boolean hasNext() {
        return ((_currentIndex + 1) < _currentPlaylist.getSize());
    }

    // Next in Loop
    @Override
    public Song next() {
        if (_currentPlaylist == null || _currentPlaylist.getSize() == 0) {
            throw new RuntimeException("No playlist loaded or playlist is empty.");
        }
        _currentIndex = _currentIndex + 1;
        return _currentPlaylist.getSongs().get(_currentIndex);
    }

    @Override
    public boolean hasPrevious() {
        return (_currentIndex - 1 > 0);
    }

    // previous in Loop
    @Override
    public Song previous() {
        if (_currentPlaylist == null || _currentPlaylist.getSize() == 0) {
            throw new RuntimeException("No playlist loaded or playlist is empty.");
        }
        _currentIndex = _currentIndex - 1;
        return _currentPlaylist.getSongs().get(_currentIndex);
    }
}
