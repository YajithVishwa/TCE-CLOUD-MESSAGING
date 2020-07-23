package com.yajith.messaging.SharedPref;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPref {
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    public void first(Context context)
    {
        preferences=context.getSharedPreferences("Message",Context.MODE_PRIVATE);
        editor=preferences.edit();
    }
    public void insert(String ph)
    {
        editor.putString("ph",ph);
        editor.commit();
    }
    public void uid(String uid)
    {
        editor.putString("uid",uid);
        editor.commit();
    }
    public void token(String token)
    {
        editor.putString("token",token);
        editor.commit();
    }
    public String gettoken()
    {
        return preferences.getString("token",null);
    }
    public String getuid()
    {
        return preferences.getString("uid",null);
    }
    public String retrive()
    {
        return preferences.getString("ph",null);
    }
}
