package com.backend.design.pattern.designs.Spotify.MusicPlayerApplication.strategies;

import com.backend.design.pattern.designs.Spotify.MusicPlayerApplication.models.Playlist;
import com.backend.design.pattern.designs.Spotify.MusicPlayerApplication.models.Song;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;

public class RandomPlayStrategy implements PlayStrategy {

    private Playlist _currentPlaylist;
    private List<Song> _remainingSongs;
    private Stack<Song> _history;
    private Random _random;

    public RandomPlayStrategy() {
        _currentPlaylist = null;
        _random = new Random();
    }

    @Override
    public void setPlaylist(Playlist playlist) {
        _currentPlaylist = playlist;
        if (_currentPlaylist == null || _currentPlaylist.getSize() == 0) {
            return;
        }

        _remainingSongs = new ArrayList<>(_currentPlaylist.getSongs());
        _history = new Stack<>();
    }

    @Override
    public boolean hasNext() {
        return _currentPlaylist != null && !_remainingSongs.isEmpty();
    }

    // Next in Loop
    @Override
    public Song next() {
        if (_currentPlaylist == null || _currentPlaylist.getSize() == 0) {
            throw new RuntimeException("No playlist loaded or playlist is empty.");
        }
        if (_remainingSongs.isEmpty()) {
            throw new RuntimeException("No songs left to play");
        }

        int idx = _random.nextInt(_remainingSongs.size());
        Song selectedSong = _remainingSongs.get(idx);

        // Remove the selectedSong from the list. (Swap and pop to remove in O(1))
        int lastIndex = _remainingSongs.size() - 1;
        _remainingSongs.set(idx, _remainingSongs.get(lastIndex));
        _remainingSongs.remove(lastIndex);

        _history.push(selectedSong);
        return selectedSong;
    }

    @Override
    public boolean hasPrevious() {
        return _history.size() > 0;
    }

    @Override
    public Song previous() {
        if (_history.isEmpty()) {
            throw new RuntimeException("No previous song available.");
        }

        Song song = _history.pop();
        return song;
    }
}
