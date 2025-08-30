package com.backend.design.pattern.designs.Spotify.MusicPlayerApplication.strategies;

import com.backend.design.pattern.designs.Spotify.MusicPlayerApplication.models.Playlist;
import com.backend.design.pattern.designs.Spotify.MusicPlayerApplication.models.Song;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class CustomQueueStrategy implements PlayStrategy {

    private Playlist _currentPlaylist;
    private int _currentIndex;
    private Queue<Song> _nextQueue;
    private Stack<Song> _prevStack;

    private Song nextSequential() {
        if (_currentPlaylist.getSize() == 0) {
            throw new RuntimeException("Playlist is empty.");
        }
        _currentIndex = _currentIndex + 1;
        return _currentPlaylist.getSongs().get(_currentIndex);
    }

    private Song previousSequential() {
        if (_currentPlaylist.getSize() == 0) {
            throw new RuntimeException("Playlist is empty.");
        }
        _currentIndex = _currentIndex - 1;
        return _currentPlaylist.getSongs().get(_currentIndex);
    }

    public CustomQueueStrategy() {
        _currentPlaylist = null;
        _currentIndex = -1;
        _nextQueue = new LinkedList<>();
        _prevStack = new Stack<>();
    }

    @Override
    public void setPlaylist(Playlist playlist) {
        _currentPlaylist = playlist;
        _currentIndex = -1;
        _nextQueue.clear();
        _prevStack.clear();
    }

    @Override
    public boolean hasNext() {
        return ((_currentIndex + 1) < _currentPlaylist.getSize());
    }

    @Override
    public Song next() {
        if (_currentPlaylist == null || _currentPlaylist.getSize() == 0) {
            throw new RuntimeException("No playlist loaded or playlist is empty.");
        }

        if (!_nextQueue.isEmpty()) {
            Song s = _nextQueue.poll();
            _prevStack.push(s);

            // update index to match queued song
            for (int i = 0; i < _currentPlaylist.getSongs().size(); ++i) {
                if (_currentPlaylist.getSongs().get(i) == s) {
                    _currentIndex = i;
                    break;
                }
            }
            return s;
        }

        // Otherwise sequential
        return nextSequential();
    }

    @Override
    public boolean hasPrevious() {
        return (_currentIndex - 1 > 0);
    }

    @Override
    public Song previous() {
        if (_currentPlaylist == null || _currentPlaylist.getSize() == 0) {
            throw new RuntimeException("No playlist loaded or playlist is empty.");
        }

        if (!_prevStack.isEmpty()) {
            Song s = _prevStack.pop();

            // update index to match stacked song
            for (int i = 0; i < _currentPlaylist.getSongs().size(); ++i) {
                if (_currentPlaylist.getSongs().get(i) == s) {
                    _currentIndex = i;
                    break;
                }
            }
            return s;
        }

        // Otherwise sequential
        return previousSequential();
    }

    @Override
    public void addToNext(Song song) {
        if (song == null) {
            throw new RuntimeException("Cannot enqueue null song.");
        }
        _nextQueue.add(song);
    }
}
