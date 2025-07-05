package com.backend.design.pattern.structural.bridgePatternSecondWay.publisher;

import com.backend.design.pattern.structural.bridgePatternSecondWay.subscriber.Subscriber;

public class Email_Publisher extends Publisher {

    public Email_Publisher(Subscriber subscriber) {
        super(subscriber);
    }

    @Override
    public void sendMsg() {
        System.out.println("Published Email Msg !! ");
        super.getSubscriber().subscriberTo();
    }
}
