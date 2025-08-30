package com.backend.design.pattern.designs.Spotify.MusicPlayerApplication.device;

import com.backend.design.pattern.designs.Spotify.MusicPlayerApplication.models.Song;

public interface IAudioOutputDevice {
    void playAudio(Song song);
}
