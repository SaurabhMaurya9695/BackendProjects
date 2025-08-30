package com.backend.design.pattern.designs.Spotify.MusicPlayerApplication.factories;

import com.backend.design.pattern.designs.Spotify.MusicPlayerApplication.device.IAudioOutputDevice;
import com.backend.design.pattern.designs.Spotify.MusicPlayerApplication.device.BluetoothSpeakerAdapter;
import com.backend.design.pattern.designs.Spotify.MusicPlayerApplication.device.HeadphonesAdapter;
import com.backend.design.pattern.designs.Spotify.MusicPlayerApplication.device.WiredSpeakerAdapter;
import com.backend.design.pattern.designs.Spotify.MusicPlayerApplication.external.BluetoothSpeakerAPI;
import com.backend.design.pattern.designs.Spotify.MusicPlayerApplication.external.HeadphonesAPI;
import com.backend.design.pattern.designs.Spotify.MusicPlayerApplication.external.WiredSpeakerAPI;
import com.backend.design.pattern.designs.Spotify.MusicPlayerApplication.enums.DeviceType;

public class DeviceFactory {

    public static IAudioOutputDevice createDevice(DeviceType deviceType) {
        switch (deviceType) {
            case BLUETOOTH:
                return new BluetoothSpeakerAdapter(new BluetoothSpeakerAPI());
            case WIRED:
                return new WiredSpeakerAdapter(new WiredSpeakerAPI());
            case HEADPHONES:
            default:
                return new HeadphonesAdapter(new HeadphonesAPI());
        }
    }
}
