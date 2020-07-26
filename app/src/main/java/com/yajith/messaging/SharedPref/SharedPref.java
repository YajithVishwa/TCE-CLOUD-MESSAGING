package com.yajith.messaging.SharedPref;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class SharedPref {
    //private SharedPreferences preferences;
    //private SharedPreferences.Editor editor;
    public SharedPreferences encryptedSharedPreferences;
    public void first(Context context)
    {
        try {

            encryptedSharedPreferences=EncryptedSharedPreferences.create("Messages", MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC), context, EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV, EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM);
        }
        catch (GeneralSecurityException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
      //  preferences=context.getSharedPreferences("Message",Context.MODE_PRIVATE);
        //editor=preferences.edit();
    }
    public void insert(String ph)
    {
        encryptedSharedPreferences.edit().putString("ph",ph).apply();
        //editor.putString("ph",ph);
        //editor.commit();
    }
    public void uid(String uid)
    {
        encryptedSharedPreferences.edit().putString("uid",uid).apply();
        //editor.putString("uid",uid);
        //editor.commit();
    }
    public void token(String token)
    {
        encryptedSharedPreferences.edit().putString("token",token).apply();
       // editor.putString("token",token);
        //editor.commit();
    }
    public String gettoken()
    {
        //return preferences.getString("token",null);
        return encryptedSharedPreferences.getString("token",null);
    }
    public String getuid()
    {
       // return preferences.getString("uid",null);
        return encryptedSharedPreferences.getString("uid",null);
    }
    public String retrive()
    {
         return encryptedSharedPreferences.getString("ph",null);
       // return preferences.getString("ph",null);

    }
}
