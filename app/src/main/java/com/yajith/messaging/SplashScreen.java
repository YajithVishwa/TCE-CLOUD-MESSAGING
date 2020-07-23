package com.yajith.messaging;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;
import com.yajith.messaging.FirstTime.FirstActivity;
import com.yajith.messaging.FirstTime.LoadContact;
import com.yajith.messaging.Fragment.Chat.ChatActivity;
import com.yajith.messaging.SharedPref.SharedPref;

public class SplashScreen extends AppCompatActivity {
    SharedPref sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        sharedPref=new SharedPref();
        sharedPref.first(getApplicationContext());
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_CONTACTS},1000);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)
        {
            case 1000:
                if(grantResults.length>0&&grantResults[0]== PackageManager.PERMISSION_GRANTED)
                {
                    if(sharedPref.retrive()!=null) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                                startActivity(new Intent(SplashScreen.this, MainActivity.class));
                            }
                        }, 1000);
                    }
                    else
                    {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                                startActivity(new Intent(SplashScreen.this, FirstActivity.class));
                            }
                        }, 3000);
                    }
                }
                else
                {
                    Toast.makeText(this, "Accept All Permission", Toast.LENGTH_SHORT).show();
                    finish();
                }
        }
    }
}
