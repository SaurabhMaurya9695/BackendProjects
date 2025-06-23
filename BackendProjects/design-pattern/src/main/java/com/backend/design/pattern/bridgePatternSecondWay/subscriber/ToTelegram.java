package com.backend.design.pattern.bridgePatternSecondWay.subscriber;

public class ToTelegram implements Subscriber {

    private String msg = null;

    public ToTelegram(String msg) {
        this.msg = msg;
    }

    @Override
    public void subscriberTo() {
        System.out.println("Subscribing To The Telegram !! with the msg : " + this.msg);
    }
}

