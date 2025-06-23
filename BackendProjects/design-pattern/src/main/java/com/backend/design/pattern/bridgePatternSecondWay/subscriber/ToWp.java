package com.backend.design.pattern.bridgePatternSecondWay.subscriber;

public class ToWp implements Subscriber {

    private String msg = null;

    public ToWp(String msg) {
        this.msg = msg;
    }

    @Override
    public void subscriberTo() {
        System.out.println("Subscribing To The WP !! and the msg is : " + this.msg);
    }
}
