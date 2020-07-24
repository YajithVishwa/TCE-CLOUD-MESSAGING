package com.yajith.messaging.FirstTime;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import com.yajith.messaging.MainActivity;
import com.yajith.messaging.R;
import com.yajith.messaging.SharedPref.SharedPref;

public class LoadContact extends AppCompatActivity {
    ProgressDialog progressDialog;
    SharedPref sharedPref;
    String ph,uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
        setContentView(R.layout.activity_load_contact);
        progressDialog=new ProgressDialog(this);
        sharedPref=new SharedPref();
        sharedPref.first(getApplicationContext());
        ph=getIntent().getExtras().getString("ph");
        uid=getIntent().getExtras().getString("uid");
        sharedPref.insert(ph);
        sharedPref.uid(uid);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Getting Contacts");
        progressDialog.show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
                startActivity(new Intent(LoadContact.this, MainActivity.class));
            }
        },100);
       // progressDialog.dismiss();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        progressDialog.dismiss();
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
        progressDialog.dismiss();
    }
}
