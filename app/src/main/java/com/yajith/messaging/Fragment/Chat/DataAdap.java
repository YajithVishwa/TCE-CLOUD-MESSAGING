package com.yajith.messaging.Fragment.Chat;

public class DataAdap {
    String text;
    String date;
    int type;
    boolean seen;
    public DataAdap(String text,int type,String date,boolean seen)
    {
        super();
        this.text=text;
        this.type=type;
        this.date=date;
        this.seen=seen;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
