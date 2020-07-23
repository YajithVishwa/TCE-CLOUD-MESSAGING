package com.yajith.messaging.FirstTime;

public class Users {
    String uid;
    String phone;
    boolean isOnline;

    public Users(String uid, String phone, boolean isOnline) {
        this.uid = uid;
        this.phone = phone;
        this.isOnline = isOnline;
    }
    public Users()
    {

    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(boolean isOnline) {
        this.isOnline = isOnline;
    }
}
