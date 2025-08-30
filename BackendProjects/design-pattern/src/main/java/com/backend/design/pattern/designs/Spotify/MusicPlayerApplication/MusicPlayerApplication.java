package com.backend.design.pattern.designs.Spotify.MusicPlayerApplication;

import com.backend.design.pattern.designs.Spotify.MusicPlayerApplication.models.Song;
import com.backend.design.pattern.designs.Spotify.MusicPlayerApplication.managers.PlaylistManager;
import com.backend.design.pattern.designs.Spotify.MusicPlayerApplication.enums.DeviceType;
import com.backend.design.pattern.designs.Spotify.MusicPlayerApplication.enums.PlayStrategyType;

public class MusicPlayerApplication {

    private static MusicPlayerApplication _instance = null;
    private java.util.List<Song> _songLibrary;

    private MusicPlayerApplication() {
        _songLibrary = new java.util.ArrayList<>();
    }

    public static synchronized MusicPlayerApplication getInstance() {
        if (_instance == null) {
            _instance = new MusicPlayerApplication();
        }
        return _instance;
    }

    public void createSongInLibrary(String title, String artist, String path) {
        Song newSong = new Song(title, artist, path);
        _songLibrary.add(newSong);
    }

    public Song findSongByTitle(String title) {
        for (Song s : _songLibrary) {
            if (s.getTitle().equals(title)) {
                return s;
            }
        }
        return null;
    }

    public void createPlaylist(String playlistName) {
        PlaylistManager.getInstance().createPlaylist(playlistName);
    }

    public void addSongToPlaylist(String playlistName, String songTitle) {
        Song song = findSongByTitle(songTitle);
        if (song == null) {
            throw new RuntimeException("Song \"" + songTitle + "\" not found in library.");
        }
        PlaylistManager.getInstance().addSongToPlaylist(playlistName, song);
    }

    public void connectAudioDevice(DeviceType deviceType) {
        MusicPlayerFacade.getInstance().connectDevice(deviceType);
    }

    public void selectPlayStrategy(PlayStrategyType strategyType) {
        MusicPlayerFacade.getInstance().setPlayStrategy(strategyType);
    }

    public void loadPlaylist(String playlistName) {
        MusicPlayerFacade.getInstance().loadPlaylist(playlistName);
    }

    public void playSingleSong(String songTitle) {
        Song song = findSongByTitle(songTitle);
        if (song == null) {
            throw new RuntimeException("Song \"" + songTitle + "\" not found.");
        }
        MusicPlayerFacade.getInstance().playSong(song);
    }

    public void pauseCurrentSong(String songTitle) {
        Song song = findSongByTitle(songTitle);
        if (song == null) {
            throw new RuntimeException("Song \"" + songTitle + "\" not found.");
        }
        MusicPlayerFacade.getInstance().pauseSong(song);
    }

    public void playAllTracksInPlaylist() {
        MusicPlayerFacade.getInstance().playAllTracks();
    }

    public void playPreviousTrackInPlaylist() {
        MusicPlayerFacade.getInstance().playPreviousTrack();
    }

    public void queueSongNext(String songTitle) {
        Song song = findSongByTitle(songTitle);
        if (song == null) {
            throw new RuntimeException("Song \"" + songTitle + "\" not found.");
        }
        MusicPlayerFacade.getInstance().enqueueNext(song);
    }
}
