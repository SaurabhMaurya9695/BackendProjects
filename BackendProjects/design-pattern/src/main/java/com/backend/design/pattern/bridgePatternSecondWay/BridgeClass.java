package com.backend.design.pattern.bridgePatternSecondWay;

import com.backend.design.pattern.bridgePatternSecondWay.publisher.Email_Publisher;
import com.backend.design.pattern.bridgePatternSecondWay.publisher.QR_Publisher;
import com.backend.design.pattern.bridgePatternSecondWay.subscriber.ToPhone;
import com.backend.design.pattern.bridgePatternSecondWay.subscriber.ToTelegram;
import com.backend.design.pattern.bridgePatternSecondWay.subscriber.ToWp;

public class BridgeClass {

    public static void main(String[] args) {
        QR_Publisher qr_publisher = new QR_Publisher(new ToPhone("Phone"));
        qr_publisher.sendMsg();

        QR_Publisher publisher = new QR_Publisher(new ToTelegram("Hey Saurabh In Telegram. "));
        publisher.sendMsg();

        Email_Publisher emailPublisher = new Email_Publisher(new ToWp("Hey Saurabh in WP"));
        emailPublisher.sendMsg();
    }
}
