package com.backend.design.pattern.designs.Spotify.MusicPlayerApplication.managers;

import com.backend.design.pattern.designs.Spotify.MusicPlayerApplication.models.Playlist;
import com.backend.design.pattern.designs.Spotify.MusicPlayerApplication.models.Song;

import java.util.HashMap;
import java.util.Map;

public class PlaylistManager {

    private static PlaylistManager _instance = null;
    private Map<String, Playlist> _playlistMap;

    private PlaylistManager() {
        _playlistMap = new HashMap<>();
    }

    public static synchronized PlaylistManager getInstance() {
        if (_instance == null) {
            _instance = new PlaylistManager();
        }
        return _instance;
    }

    public void createPlaylist(String name) {
        if (_playlistMap.containsKey(name)) {
            throw new RuntimeException("Playlist \"" + name + "\" already exists.");
        }
        _playlistMap.put(name, new Playlist(name));
    }

    public void addSongToPlaylist(String playlistName, Song song) {
        if (!_playlistMap.containsKey(playlistName)) {
            throw new RuntimeException("Playlist \"" + playlistName + "\" not found.");
        }
        _playlistMap.get(playlistName).addSongToPlaylist(song);
    }

    public Playlist getPlaylist(String name) {
        if (!_playlistMap.containsKey(name)) {
            throw new RuntimeException("Playlist \"" + name + "\" not found.");
        }
        return _playlistMap.get(name);
    }
}
