package com.backend.design.pattern.bridgePatternSecondWay.publisher;

import com.backend.design.pattern.bridgePatternSecondWay.subscriber.Subscriber;

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
