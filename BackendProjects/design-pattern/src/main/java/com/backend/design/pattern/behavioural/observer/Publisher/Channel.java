package com.backend.design.pattern.behavioural.observer.Publisher;

import com.backend.design.pattern.behavioural.observer.Subscriber.IObserver_Subscriber;

import java.util.ArrayList;
import java.util.List;

public class Channel implements IObservable_Channel {

    private List<IObserver_Subscriber> _listener = new ArrayList<>();
    private String name;
    private String latestVideo;

    @Override
    public void subscribeChannel(IObserver_Subscriber subscriber) {
        if (!_listener.contains(subscriber)) {
            _listener.add(subscriber);
        }
    }

    @Override
    public void removeChannel(IObserver_Subscriber subscriber) {
        if (_listener != null && !_listener.isEmpty() && _listener.contains(subscriber)) {
            _listener.remove(subscriber);
        }
    }

    @Override
    public void notifyChannel() {
        for (IObserver_Subscriber subscriber : _listener) {
            subscriber.update();
        }
    }

    public void uploadVideo(String title) {
        latestVideo = title;
        System.out.println("Video added to channel");
        notifyChannel();
    }

    public String getLatestVideo() {
        System.out.println("Ooohaaa ! , new Video added " + latestVideo);
        return "Ooohaaa ! , new Video added " + latestVideo;
    }

    public List<IObserver_Subscriber> getListener() {
        return _listener;
    }

    public void setListener(List<IObserver_Subscriber> listener) {
        _listener = listener;
    }
}
