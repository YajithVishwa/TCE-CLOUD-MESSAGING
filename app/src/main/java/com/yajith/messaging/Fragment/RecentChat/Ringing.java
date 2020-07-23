package com.yajith.messaging.Fragment.RecentChat;

public class Ringing {
    private String calling,picked;

    public Ringing(String calling, String picked) {
        this.calling = calling;
        this.picked = picked;
    }

    public Ringing() {
    }

    public String getCalling() {
        return calling;
    }

    public void setCalling(String calling) {
        this.calling = calling;
    }

    public String getPicked() {
        return picked;
    }

    public void setPicked(String picked) {
        this.picked = picked;
    }
}
