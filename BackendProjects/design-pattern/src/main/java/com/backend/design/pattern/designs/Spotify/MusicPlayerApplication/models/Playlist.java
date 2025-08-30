package com.backend.design.pattern.designs.Spotify.MusicPlayerApplication.models;

import java.util.ArrayList;
import java.util.List;

public class Playlist {

    private String _playlistName;
    private List<Song> _songList;

    public Playlist(String name) {
        this._playlistName = name;
        this._songList = new ArrayList<>();
    }

    public String getPlaylistName() {
        return _playlistName;
    }

    public List<Song> getSongs() {
        return _songList;
    }

    public int getSize() {
        return _songList.size();
    }

    public void addSongToPlaylist(Song song) {
        if (song == null) {
            throw new RuntimeException("Cannot add null song to playlist.");
        }
        _songList.add(song);
    }
}
