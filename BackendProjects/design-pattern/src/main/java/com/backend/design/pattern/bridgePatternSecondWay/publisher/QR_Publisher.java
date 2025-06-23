package com.backend.design.pattern.bridgePatternSecondWay.publisher;

import com.backend.design.pattern.bridgePatternSecondWay.subscriber.Subscriber;

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
