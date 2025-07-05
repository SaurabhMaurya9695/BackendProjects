package com.backend.design.pattern.structural.bridgePatternSecondWay.publisher;

import com.backend.design.pattern.structural.bridgePatternSecondWay.subscriber.Subscriber;

public class QR_Publisher extends Publisher {

    public QR_Publisher(Subscriber subscriber) {
        super(subscriber);
    }

    @Override
    public void sendMsg() {
        System.out.println("Published QR code !!");
        super.getSubscriber().subscriberTo();
    }
}
