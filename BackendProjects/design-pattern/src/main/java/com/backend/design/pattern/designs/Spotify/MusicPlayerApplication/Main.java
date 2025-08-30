package com.backend.design.pattern.designs.Spotify.MusicPlayerApplication;

import com.backend.design.pattern.designs.Spotify.MusicPlayerApplication.enums.DeviceType;
import com.backend.design.pattern.designs.Spotify.MusicPlayerApplication.enums.PlayStrategyType;

public class Main {

    public static void main(String[] args) {
        try {
            MusicPlayerApplication _spotify = getMusicPlayerApplication();

            // Connect device
            _spotify.connectAudioDevice(DeviceType.BLUETOOTH);

            //Play/pause a single song
            _spotify.playSingleSong("Zinda");
            _spotify.pauseCurrentSong("Zinda");
            _spotify.playSingleSong("Zinda");  // resume

            System.out.println("\n-- Sequential Playback --\n");
            _spotify.selectPlayStrategy(PlayStrategyType.SEQUENTIAL);
            _spotify.loadPlaylist("Bollywood Vibes");
            _spotify.playAllTracksInPlaylist();

            System.out.println("\n-- Random Playback --\n");
            _spotify.selectPlayStrategy(PlayStrategyType.RANDOM);
            _spotify.loadPlaylist("Bollywood Vibes");
            _spotify.playAllTracksInPlaylist();

            System.out.println("\n-- Custom Queue Playback --\n");
            _spotify.selectPlayStrategy(PlayStrategyType.CUSTOM_QUEUE);
            _spotify.loadPlaylist("Bollywood Vibes");
            _spotify.queueSongNext("Kesariya");
            _spotify.queueSongNext("Tum Hi Ho");
            _spotify.playAllTracksInPlaylist();

            System.out.println("\n-- Play Previous in Sequential --\n");
            _spotify.selectPlayStrategy(PlayStrategyType.SEQUENTIAL);
            _spotify.loadPlaylist("Bollywood Vibes");
            _spotify.playAllTracksInPlaylist();

            _spotify.playPreviousTrackInPlaylist();
            _spotify.playPreviousTrackInPlaylist();
        } catch (Exception error) {
            System.err.println("Error: " + error.getMessage());
        }
    }

    private static MusicPlayerApplication getMusicPlayerApplication() {
        MusicPlayerApplication _spotify = MusicPlayerApplication.getInstance();

        // Populate library
        _spotify.createSongInLibrary("Kesariya", "Arijit Singh", "/music/kesariya.mp3");
        _spotify.createSongInLibrary("Chaiyya Chaiyya", "Sukhwinder Singh", "/music/chaiyya_chaiyya.mp3");
        _spotify.createSongInLibrary("Tum Hi Ho", "Arijit Singh", "/music/tum_hi_ho.mp3");
        _spotify.createSongInLibrary("Jai Ho", "A. R. Rahman", "/music/jai_ho.mp3");
        _spotify.createSongInLibrary("Zinda", "Siddharth Mahadevan", "/music/zinda.mp3");

        // Create playlist and add songs
        _spotify.createPlaylist("Bollywood Vibes");
        _spotify.addSongToPlaylist("Bollywood Vibes", "Kesariya");
        _spotify.addSongToPlaylist("Bollywood Vibes", "Chaiyya Chaiyya");
        _spotify.addSongToPlaylist("Bollywood Vibes", "Tum Hi Ho");
        _spotify.addSongToPlaylist("Bollywood Vibes", "Jai Ho");
        return _spotify;
    }
}
