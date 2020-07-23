package com.yajith.messaging.Fragment.RecentChat;

public class User {
    private String name;
    private String ph;
    private boolean isOnline;
    private Ringing ringing;
    private Calling calling;



    public User(String ph, String name, boolean isOnline) {
        this.name=name;
        this.ph = ph;
        this.isOnline=isOnline;

    }
    public User(String ph, String name, boolean isOnline,Ringing ringing) {
        this(ph, name, isOnline);
        this.ringing=ringing;
    }
    public User(String ph, String name, boolean isOnline, String picked,Calling calling) {
        this(ph, name, isOnline);
        this.calling=calling;
    }

    public Ringing getRinging() {
        return ringing;
    }

    public void setRinging(Ringing ringing) {
        this.ringing = ringing;
    }

    public Calling getCalling() {
        return calling;
    }

    public void setCalling(Calling calling) {
        this.calling = calling;
    }

    public Ringing getSubUser() {
        return ringing;
    }

    public void setSubUser(Ringing subUser) {
        this.ringing = subUser;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public User()
    {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPh() {
        return ph;
    }

    public void setPh(String ph) {
        this.ph = ph;
    }
}
