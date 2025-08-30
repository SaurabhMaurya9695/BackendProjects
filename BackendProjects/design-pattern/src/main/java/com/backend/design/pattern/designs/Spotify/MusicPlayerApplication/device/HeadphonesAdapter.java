package com.backend.design.pattern.designs.Spotify.MusicPlayerApplication.device;

import com.backend.design.pattern.designs.Spotify.MusicPlayerApplication.models.Song;
import com.backend.design.pattern.designs.Spotify.MusicPlayerApplication.external.HeadphonesAPI;

public class HeadphonesAdapter implements IAudioOutputDevice {

    private HeadphonesAPI _headphonesAPI;

    public HeadphonesAdapter(HeadphonesAPI api) {
        this._headphonesAPI = api;
    }

    @Override
    public void playAudio(Song song) {
        String payload = song.getTitle() + " by " + song.getArtist();
        _headphonesAPI.playSoundViaJack(payload);
    }
}
