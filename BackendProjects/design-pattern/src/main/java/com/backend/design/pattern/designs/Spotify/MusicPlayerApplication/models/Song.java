package com.backend.design.pattern.designs.Spotify.MusicPlayerApplication.models;

public class Song {

    private String _title;
    private String _artist;
    private String _filePath;

    public Song(String title, String artist, String filePath) {
        this._title = title;
        this._artist = artist;
        this._filePath = filePath;
    }

    public String getTitle() {
        return _title;
    }

    public String getArtist() {
        return _artist;
    }

    public String getFilePath() {
        return _filePath;
    }
}
