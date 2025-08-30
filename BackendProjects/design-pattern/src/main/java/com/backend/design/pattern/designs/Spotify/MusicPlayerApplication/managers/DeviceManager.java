package com.backend.design.pattern.designs.Spotify.MusicPlayerApplication.managers;

import com.backend.design.pattern.designs.Spotify.MusicPlayerApplication.device.IAudioOutputDevice;
import com.backend.design.pattern.designs.Spotify.MusicPlayerApplication.enums.DeviceType;
import com.backend.design.pattern.designs.Spotify.MusicPlayerApplication.factories.DeviceFactory;

public class DeviceManager {

    private static DeviceManager _instance = null;
    private IAudioOutputDevice _currentOutputDevice;

    private DeviceManager() {
        _currentOutputDevice = null;
    }

    public static synchronized DeviceManager getInstance() {
        if (_instance == null) {
            _instance = new DeviceManager();
        }
        return _instance;
    }

    public void connect(DeviceType deviceType) {
        if (_currentOutputDevice != null) {
            // In C++: delete currentOutputDevice;
            // In Java, garbage collector handles it, so no explicit delete.
        }

        _currentOutputDevice = DeviceFactory.createDevice(deviceType);

        switch (deviceType) {
            case BLUETOOTH:
                System.out.println("Bluetooth device connected ");
                break;
            case WIRED:
                System.out.println("Wired device connected ");
                break;
            case HEADPHONES:
                System.out.println("Headphones connected ");
                break;
        }
    }

    public IAudioOutputDevice getOutputDevice() {
        if (_currentOutputDevice == null) {
            throw new RuntimeException("No output device is connected.");
        }
        return _currentOutputDevice;
    }

    public boolean hasOutputDevice() {
        return _currentOutputDevice != null;
    }
}
