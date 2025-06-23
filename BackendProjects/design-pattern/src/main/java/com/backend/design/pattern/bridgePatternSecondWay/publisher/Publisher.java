package com.backend.design.pattern.bridgePatternSecondWay.publisher;

import com.backend.design.pattern.bridgePatternSecondWay.subscriber.Subscriber;

// there can be many types of Publisher Sender such as, as a Email , as a QR
public abstract class Publisher {

    // publisher has subscriber where he wants to subscribe
    private final Subscriber _subscriber;

    public Publisher(Subscriber subscriber) {
        _subscriber = subscriber;
    }

    public abstract void sendMsg();

    public Subscriber getSubscriber() {
        return _subscriber;
    }
}
