package com.backend.design.pattern.designs.Spotify.MusicPlayerApplication;

import com.backend.design.pattern.designs.Spotify.MusicPlayerApplication.core.AudioEngine;
import com.backend.design.pattern.designs.Spotify.MusicPlayerApplication.models.Playlist;
import com.backend.design.pattern.designs.Spotify.MusicPlayerApplication.models.Song;
import com.backend.design.pattern.designs.Spotify.MusicPlayerApplication.strategies.PlayStrategy;
import com.backend.design.pattern.designs.Spotify.MusicPlayerApplication.enums.DeviceType;
import com.backend.design.pattern.designs.Spotify.MusicPlayerApplication.enums.PlayStrategyType;
import com.backend.design.pattern.designs.Spotify.MusicPlayerApplication.managers.DeviceManager;
import com.backend.design.pattern.designs.Spotify.MusicPlayerApplication.managers.PlaylistManager;
import com.backend.design.pattern.designs.Spotify.MusicPlayerApplication.managers.StrategyManager;
import com.backend.design.pattern.designs.Spotify.MusicPlayerApplication.device.IAudioOutputDevice;

public class MusicPlayerFacade {

    private static MusicPlayerFacade _instance = null;
    private AudioEngine _audioEngine;
    private Playlist _loadedPlaylist;
    private PlayStrategy _playStrategy;

    private MusicPlayerFacade() {
        _loadedPlaylist = null;
        _playStrategy = null;
        _audioEngine = new AudioEngine();
    }

    public static synchronized MusicPlayerFacade getInstance() {
        if (_instance == null) {
            _instance = new MusicPlayerFacade();
        }
        return _instance;
    }

    public void connectDevice(DeviceType deviceType) {
        DeviceManager.getInstance().connect(deviceType);
    }

    public void setPlayStrategy(PlayStrategyType strategyType) {
        _playStrategy = StrategyManager.getInstance().getStrategy(strategyType);
    }

    public void loadPlaylist(String name) {
        _loadedPlaylist = PlaylistManager.getInstance().getPlaylist(name);
        if (_playStrategy == null) {
            throw new RuntimeException("Play strategy not set before loading.");
        }
        _playStrategy.setPlaylist(_loadedPlaylist);
    }

    public void playSong(Song song) {
        if (!DeviceManager.getInstance().hasOutputDevice()) {
            throw new RuntimeException("No audio device connected.");
        }
        IAudioOutputDevice device = DeviceManager.getInstance().getOutputDevice();
        _audioEngine.play(device, song);
    }

    public void pauseSong(Song song) {
        if (!_audioEngine.getCurrentSongTitle().equals(song.getTitle())) {
            throw new RuntimeException("Cannot pause \"" + song.getTitle() + "\"; not currently playing.");
        }
        _audioEngine.pause();
    }

    public void playAllTracks() {
        if (_loadedPlaylist == null) {
            throw new RuntimeException("No playlist loaded.");
        }
        while (_playStrategy.hasNext()) {
            Song nextSong = _playStrategy.next();
            IAudioOutputDevice device = DeviceManager.getInstance().getOutputDevice();
            _audioEngine.play(device, nextSong);
        }
        System.out.println("Completed playlist: " + _loadedPlaylist.getPlaylistName());
    }

    public void playNextTrack() {
        if (_loadedPlaylist == null) {
            throw new RuntimeException("No playlist loaded.");
        }
        if (_playStrategy.hasNext()) {
            Song nextSong = _playStrategy.next();
            IAudioOutputDevice device = DeviceManager.getInstance().getOutputDevice();
            _audioEngine.play(device, nextSong);
        } else {
            System.out.println("Completed playlist: " + _loadedPlaylist.getPlaylistName());
        }
    }

    public void playPreviousTrack() {
        if (_loadedPlaylist == null) {
            throw new RuntimeException("No playlist loaded.");
        }
        if (_playStrategy.hasPrevious()) {
            Song prevSong = _playStrategy.previous();
            IAudioOutputDevice device = DeviceManager.getInstance().getOutputDevice();
            _audioEngine.play(device, prevSong);
        } else {
            System.out.println("Completed playlist: " + _loadedPlaylist.getPlaylistName());
        }
    }

    public void enqueueNext(Song song) {
        _playStrategy.addToNext(song);
    }
}
