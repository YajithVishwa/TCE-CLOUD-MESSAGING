package com.yajith.messaging.Notification;

public class Sender {
    public NotifiData notifiData;
    public String to;

    public Sender(NotifiData notifiData, String to) {
        this.notifiData = notifiData;
        this.to = to;
    }
}
