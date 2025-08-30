package com.backend.design.pattern.designs.Spotify.MusicPlayerApplication.device;

import com.backend.design.pattern.designs.Spotify.MusicPlayerApplication.models.Song;
import com.backend.design.pattern.designs.Spotify.MusicPlayerApplication.external.WiredSpeakerAPI;

public class WiredSpeakerAdapter implements IAudioOutputDevice {

    private WiredSpeakerAPI _wiredSpeakerAPI;

    public WiredSpeakerAdapter(WiredSpeakerAPI api) {
        this._wiredSpeakerAPI = api;
    }

    @Override
    public void playAudio(Song song) {
        String payload = song.getTitle() + " by " + song.getArtist();
        _wiredSpeakerAPI.playSoundViaCable(payload);
    }
}
