package com.yajith.messaging.FirstTime;

public class Name {
    String name,ph;

    public Name(String name, String ph) {
        this.name = name;
        this.ph = ph;
    }
    public Name()
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
