package com.yajith.messaging.Fragment.VideoCall;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yajith.messaging.MainActivity;
import com.yajith.messaging.R;
import com.yajith.messaging.SharedPref.SharedPref;

import java.util.HashMap;

public class AnswerActivity extends AppCompatActivity {
    TextView name;
    ImageView accept,reject;
    MediaPlayer mediaPlayer;
    String checker="",uid;
    String textname,receiveruid;
    private String callingId="",ringingId="";
    SharedPref sharedPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);
        textname=getIntent().getExtras().getString("name");
        receiveruid=getIntent().getExtras().getString("receiveruid");
        name=findViewById(R.id.name_calling);
        name.setText(textname);
        accept=findViewById(R.id.make_call);
        sharedPref=new SharedPref();
        sharedPref.first(getApplicationContext());
        uid=sharedPref.getuid();
        mediaPlayer = MediaPlayer.create(this, Settings.System.DEFAULT_RINGTONE_URI);
        mediaPlayer.start();
        reject=findViewById(R.id.cancel_call);
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                final HashMap<String, Object> callingPickUpMap = new HashMap<>();
                callingPickUpMap.put("picked","picked");
                FirebaseDatabase.getInstance().getReference().child("User").child(uid).child("Ringing").updateChildren(callingPickUpMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(AnswerActivity.this, "Call Answered", Toast.LENGTH_SHORT).show();
                        finish();
                        startActivity(new Intent(AnswerActivity.this,VideoChatActivity.class));
                    }
                });

            }
        });
        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Flag.setCall(1);
                mediaPlayer.stop();
                checker="clicked";
                cancelCallBtn();
            }
        });
    }
    private void cancelCallBtn(){
        //from sender side
        FirebaseDatabase.getInstance().getReference().child("User").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() && dataSnapshot.hasChild("Calling")){
                    callingId=dataSnapshot.child("Calling").child("calling").getValue().toString();
                    FirebaseDatabase.getInstance().getReference().child("User").child(callingId).child("Ringing").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
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
        FirebaseDatabase.getInstance().getReference().child("User").child(uid).child("Ringing").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() && dataSnapshot.hasChild("Ringing")){
                    ringingId=dataSnapshot.child("calling").getValue().toString();
                    FirebaseDatabase.getInstance().getReference().child("User").child(ringingId).child("Calling").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
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
    }
}