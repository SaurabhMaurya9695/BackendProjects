package com.backend.design.pattern.bridgePatternSecondWay.subscriber;

public class ToPhone implements Subscriber {

    private String msg = null;

    public ToPhone(String msg) {
        this.msg = msg;
    }

    @Override
    public void subscriberTo() {
        System.out.println("Subscribing To The Phone !! with msg : " + this.msg);
    }
}
