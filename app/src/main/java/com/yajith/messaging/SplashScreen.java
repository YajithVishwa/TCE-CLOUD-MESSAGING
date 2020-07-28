package com.yajith.messaging;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.yajith.messaging.FirstTime.FirstActivity;
import com.yajith.messaging.FirstTime.LoadContact;
import com.yajith.messaging.FirstTime.Swipe.SwipeActivity;
import com.yajith.messaging.Fragment.Chat.ChatActivity;
import com.yajith.messaging.SharedPref.SharedPref;

import java.io.File;

public class SplashScreen extends AppCompatActivity {
    SharedPref sharedPref;
    double version=1.0;
    Context context;
    FirebaseStorage firebaseStorage;
    ProgressDialog progressBar;
    StorageReference storageReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
        setContentView(R.layout.activity_splash_screen);
        sharedPref=new SharedPref();
        progressBar=new ProgressDialog(this);
        progressBar.setTitle("Downloading");
        firebaseStorage=FirebaseStorage.getInstance();
        storageReference=firebaseStorage.getReference();
        context=this;
        sharedPref.first(getApplicationContext());
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_CONTACTS,Manifest.permission.READ_SMS,Manifest.permission.RECEIVE_SMS},1000);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)
        {
            case 1000:
                if(grantResults.length>0&&grantResults[0]== PackageManager.PERMISSION_GRANTED&&grantResults[1]==PackageManager.PERMISSION_GRANTED)
                {
                        if (sharedPref.retrive() != null) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    finish();
                                    startActivity(new Intent(SplashScreen.this, MainActivity.class));
                                }
                            }, 1000);
                        } else {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    finish();
                                    startActivity(new Intent(SplashScreen.this, SwipeActivity.class));

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

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }
}
