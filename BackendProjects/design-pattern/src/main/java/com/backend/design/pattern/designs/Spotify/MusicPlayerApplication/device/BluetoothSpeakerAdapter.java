package com.backend.design.pattern.designs.Spotify.MusicPlayerApplication.device;

import com.backend.design.pattern.designs.Spotify.MusicPlayerApplication.models.Song;
import com.backend.design.pattern.designs.Spotify.MusicPlayerApplication.external.BluetoothSpeakerAPI;

public class BluetoothSpeakerAdapter implements IAudioOutputDevice {

    private BluetoothSpeakerAPI _speakerAPI;

    public BluetoothSpeakerAdapter(BluetoothSpeakerAPI api) {
        this._speakerAPI = api;
    }

    @Override
    public void playAudio(Song song) {
        String payload = song.getTitle() + " by " + song.getArtist();
        _speakerAPI.playSoundViaBluetooth(payload);
    }
}
