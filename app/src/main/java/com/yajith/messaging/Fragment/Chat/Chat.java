package com.yajith.messaging.Fragment.Chat;

public class Chat {
    private String msg;
    private String sender;
    private String receiver;
    private String date;
    private boolean isseen;

    public Chat(String msg, String sender, String receiver,String date,boolean isseen) {
        this.msg = msg;
        this.sender = sender;
        this.receiver = receiver;
        this.date=date;
        this.isseen=isseen;
    }
    public Chat()
    {

    }

    public boolean isIsseen() {
        return isseen;
    }

    public void setIsseen(boolean isseen) {
        this.isseen = isseen;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }
}
