package com.backend.design.pattern.behavioural.observer.Subscriber;

import com.backend.design.pattern.behavioural.observer.Publisher.Channel;

public class Subscriber implements IObserver_Subscriber {

    private String name;
    private Channel _channel;

    public Subscriber(String name) {
        this.name = name;
    }

    public Subscriber(String name, Channel channel) {
        this.name = name;
        _channel = channel;
    }

    @Override
    public void update() {
        System.out.println("This video " + name + " is updated " + _channel.getLatestVideo() + "\n");
    }
}
