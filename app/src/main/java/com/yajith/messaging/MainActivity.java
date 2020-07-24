package com.yajith.messaging;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yajith.messaging.Fragment.Allcontacts.AllContacts;
import com.yajith.messaging.Fragment.ContextClass;
import com.yajith.messaging.Fragment.RecentChat.RecentChatFragment;
import com.yajith.messaging.Fragment.VideoCall.AnswerActivity;
import com.yajith.messaging.Fragment.VideoCall.CallingActivity;
import com.yajith.messaging.Fragment.VideoCall.Flag;
import com.yajith.messaging.Fragment.VideoCall.VideoCallFragment;
import com.yajith.messaging.SharedPref.SharedPref;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView navigationView;
    FloatingActionButton actionButton;
    SharedPref sharedPref;
    String uid,myphone;
    Context context;
    String name;
    Activity activity;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
        setContentView(R.layout.activity_main);
        navigationView=findViewById(R.id.navigation);
        navigationView.setSelectedItemId(R.id.chat);
        activity=this;
        ContextClass.context=getApplicationContext();
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Refreshing Contacts");
        context=this;
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frag,new RecentChatFragment());
        fragmentTransaction.commit();
        sharedPref=new SharedPref();
        sharedPref.first(getApplicationContext());
        uid=sharedPref.getuid();
        myphone=sharedPref.retrive();
        actionButton=findViewById(R.id.floating_action_button);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id=item.getItemId();
                switch(id)
                {
                    case R.id.chat:
                        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frag,new RecentChatFragment());
                        fragmentTransaction.commit();break;
                    case R.id.call:
                        FragmentTransaction fragmentTransaction1=getSupportFragmentManager().beginTransaction();
                        fragmentTransaction1.replace(R.id.frag,new VideoCallFragment());
                        fragmentTransaction1.commit();break;
                    default:
                        FragmentTransaction fragmentTransaction2=getSupportFragmentManager().beginTransaction();
                        fragmentTransaction2.replace(R.id.frag,new RecentChatFragment());
                        fragmentTransaction2.commit();break;
                }
                return true;
            }
        });
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context,AllContacts.class));

            }
        });
        callcheck();
    }
    private void callcheck()
    {
        Flag.setCall(1);
        FirebaseDatabase.getInstance().getReference().child("User").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()&&snapshot.hasChild("Ringing"))
                {
                    final String callinguid=snapshot.child("Ringing").child("calling").getValue(String.class);
                    if(!snapshot.child("Ringing").hasChild("picked")) {
                        if(Flag.getCall()==1) {
                            FirebaseDatabase.getInstance().getReference().child("User").child(callinguid).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(Flag.getCall()==1) {
                                        Flag.setCall(2);
                                        String callingph = snapshot.child("phone").getValue(String.class);
                                        Intent intent = new Intent(MainActivity.this, AnswerActivity.class);
                                        intent.putExtra("receiveruid", callinguid);
                                        intent.putExtra("name", getContactName(callingph, getApplicationContext()));
                                        startActivity(intent);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public String getContactName(final String phoneNumber, Context context)
    {
        Uri uri=Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI,Uri.encode(phoneNumber));

        String[] projection = new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME};

        String contactName="";
        Cursor cursor=context.getContentResolver().query(uri,projection,null,null,null);

        if (cursor != null) {
            if(cursor.moveToFirst()) {
                contactName=cursor.getString(0);
            }
            cursor.close();
        }

        return contactName;
    }

    private void isOnline(boolean isOnline)
    {
        Map<String,Object> map=new HashMap<>();
        map.put("isOnline",isOnline);

        FirebaseDatabase.getInstance().getReference().child("User").child(uid).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
              //  Toast.makeText(MainActivity.this, "Changed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        isOnline(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        isOnline(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        isOnline(false);
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
        progressDialog.dismiss();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isOnline(false);
    }
}
