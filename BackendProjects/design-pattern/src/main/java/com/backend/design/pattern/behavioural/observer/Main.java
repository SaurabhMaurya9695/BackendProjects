package com.backend.design.pattern.behavioural.observer;

import com.backend.design.pattern.behavioural.observer.Publisher.Channel;
import com.backend.design.pattern.behavioural.observer.Subscriber.Subscriber;

public class Main {

    public static void main(String[] args) {
        Channel channel = new Channel();
        Subscriber anuj = new Subscriber("Subscribe to Anuj ", channel);
        Subscriber saurabh = new Subscriber("Subscribe to saurabh ", channel);

        channel.subscribeChannel(saurabh);
        channel.subscribeChannel(anuj);

        channel.uploadVideo("XMan");

        channel.removeChannel(anuj);
        channel.uploadVideo("Marvel");
    }
}
