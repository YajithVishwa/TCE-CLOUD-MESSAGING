package com.yajith.messaging.Fragment.VideoCall;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yajith.messaging.Fragment.Chat.ChatActivity;
import com.yajith.messaging.Fragment.RecentChat.User;
import com.yajith.messaging.MainActivity;
import com.yajith.messaging.R;
import com.yajith.messaging.SharedPref.SharedPref;

import java.io.Serializable;
import java.util.HashMap;

public class CallingActivity extends AppCompatActivity {
private TextView nameContact;
private String receivername;
private ImageView cancelCallBtn;
private String receiverphone;
private String senderphone,checker="";
private String callingId="",ringingId="";
private SharedPref pref;
private Receiver receiver;
private String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calling);
        pref=new SharedPref();
        receiver=new Receiver();
        pref.first(getApplicationContext());
        uid=pref.getuid();
        senderphone= getIntent().getExtras().getString("sender");
        receiverphone=getIntent().getExtras().getString("receiver");
        receivername=getIntent().getExtras().getString("name");
        nameContact=findViewById(R.id.name_calling);
        nameContact.setText(receivername);
        cancelCallBtn=findViewById(R.id.cancel_call);
        cancelCallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checker="clicked";
                cancelCallBtn();
            }
        });
        checkCancel();

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseDatabase.getInstance().getReference().child("User").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(final DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    //User user=dataSnapshot.getValue(User.class);
                    String ph=dataSnapshot.child("phone").getValue(String.class);
                    String uids=dataSnapshot.child("uid").getValue(String.class);
                    if(ph.equals(receiverphone))
                    {
                        final String receiveruid=uids;
                        receiver.setReceiveruid(uids);
                        FirebaseDatabase.getInstance().getReference().child("User").child(receiveruid).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(!checker.equals("clicked")&&!dataSnapshot.hasChild("Calling")&&!dataSnapshot.hasChild("Ringing"))
                                {
                                    final HashMap<String, Object> callingInfo = new HashMap<>();
                                    callingInfo.put("calling",receiveruid);
                                    FirebaseDatabase.getInstance().getReference().child("User").child(uid).child("Calling").updateChildren(callingInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                final HashMap<String, Object> ringingInfo = new HashMap<>();
                                                ringingInfo.put("calling",uid);
                                                FirebaseDatabase.getInstance().getReference().child("User").child(receiveruid).child("Ringing").updateChildren(ringingInfo);

                                            }
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
                FirebaseDatabase.getInstance().getReference().child("User").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot:snapshot.getChildren())
                        {
                            String phs=dataSnapshot.child("phone").getValue(String.class);
                            if(phs.equals(receiverphone))
                            {
                                String receiveruids=dataSnapshot.child("uid").getValue(String.class);
                                receiver.setReceiveruid(dataSnapshot.child("uid").getValue(String.class));
                                if(snapshot.child(receiver.getReceiveruid()).hasChild("Ringing")) {
                                    if (snapshot.child(receiver.getReceiveruid()).child("Ringing").hasChild("picked")) {
                                        Intent intent = new Intent(CallingActivity.this, VideoChatActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                    if (snapshot.child(uid).child("Ringing").child("picked").hasChild("picked")) {
                                        Intent intent = new Intent(CallingActivity.this, VideoChatActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                                if(dataSnapshot.child(receiver.getReceiveruid()).hasChild("Calling")&&!dataSnapshot.child(uid).hasChild("Ringing")){
                                    FirebaseDatabase.getInstance().getReference().child("User").child(receiver.getReceiveruid()).child("Calling").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            //startActivity(new Intent(CallingActivity.this,MainActivity.class));
                                            finish();
                                        }
                                    });
                                }
                                if(dataSnapshot.child(receiver.getReceiveruid()).hasChild("Ringing")&&!dataSnapshot.child(uid).hasChild("Calling")){
                                    FirebaseDatabase.getInstance().getReference().child("User").child(receiver.getReceiveruid()).child("Ringing").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            // cancelforreceiver();
                                            // startActivity(new Intent(CallingActivity.this,MainActivity.class));
                                            finish();
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

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void checkCancel()
    {
        FirebaseDatabase.getInstance().getReference().child("User").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild("Calling"))
                {

                }
                else
                {
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void cancelCallBtn(){
        final DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("User");
        databaseReference
        //from sender side
        .child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() && dataSnapshot.hasChild("Calling")){
                    callingId=dataSnapshot.child("Calling").child("calling").getValue().toString();
                    databaseReference.child(callingId).child("Ringing").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                FirebaseDatabase.getInstance().getReference().child("User").child(uid).child("Calling").removeValue();
                                //startActivity(new Intent(AnswerActivity.this, MainActivity.class));
                                finish();
                            }

                        }
                    });
                }
                /*else {
                    startActivity(new Intent(CallingActivity.this,RegistrationActivity.class));
                    finish();
                }*/

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //from receiver side
        databaseReference.child(uid).child("Ringing").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() && dataSnapshot.hasChild("Ringing")){
                    ringingId=dataSnapshot.child("calling").getValue().toString();
                   databaseReference.child("User").child(ringingId).child("Calling").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                //startActivity(new Intent(AnswerActivity.this,MainActivity.class));
                                finish();
                            }
                        }

                    });
                }
                /*else {
                    startActivity(new Intent(CallingActivity.this,RegistrationActivity.class));
                    finish();
                }*/

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        finish();
    }
}
