package com.backend.design.pattern.designs.Spotify.MusicPlayerApplication.core;

import com.backend.design.pattern.designs.Spotify.MusicPlayerApplication.models.Song;
import com.backend.design.pattern.designs.Spotify.MusicPlayerApplication.device.IAudioOutputDevice;

public class AudioEngine {

    private Song _currentSong;
    private boolean _songIsPaused;

    public AudioEngine() {
        _currentSong = null;
        _songIsPaused = false;
    }

    public String getCurrentSongTitle() {
        if (_currentSong != null) {
            return _currentSong.getTitle();
        }
        return "";
    }

    public boolean isPaused() {
        return _songIsPaused;
    }

    public void play(IAudioOutputDevice aod, Song song) {
        if (song == null) {
            throw new RuntimeException("Cannot play a null song.");
        }
        // Resume if same song was paused
        if (_songIsPaused && song == _currentSong) {
            _songIsPaused = false;
            System.out.println("Resuming song: " + song.getTitle());
            aod.playAudio(song);
            return;
        }

        _currentSong = song;
        _songIsPaused = false;
        System.out.println("Playing song: " + song.getTitle());
        aod.playAudio(song);
    }

    public void pause() {
        if (_currentSong == null) {
            throw new RuntimeException("No song is currently playing to pause.");
        }
        if (_songIsPaused) {
            throw new RuntimeException("Song is already paused.");
        }
        _songIsPaused = true;
        System.out.println("Pausing song: " + _currentSong.getTitle());
    }
}
