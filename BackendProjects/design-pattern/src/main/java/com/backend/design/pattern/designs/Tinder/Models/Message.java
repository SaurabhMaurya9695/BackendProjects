package com.backend.design.pattern.designs.Tinder.Models;

import com.backend.design.pattern.designs.Tomato.utils.TimeUtils;

import java.util.Timer;

public class Message {

    private String senderId;
    private String content;
    private Timer _timer;

    public Message() {
    }

    public Message(String senderId, String content) {
        this.senderId = senderId;
        this.content = content;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timer getTimer() {
        return _timer;
    }

    public void setTimer(Timer timer) {
        _timer = timer;
    }

    public String getFormattedTime() {
        return TimeUtils.getCurrentTime();
    }
}
