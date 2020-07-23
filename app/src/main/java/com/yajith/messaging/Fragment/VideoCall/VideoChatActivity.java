package com.yajith.messaging.Fragment.VideoCall;

import android.Manifest;
import android.content.Intent;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.opentok.android.OpentokError;
import com.opentok.android.Publisher;
import com.opentok.android.PublisherKit;
import com.opentok.android.Session;
import com.opentok.android.Stream;
import com.opentok.android.Subscriber;
import com.opentok.android.SubscriberKit;
import com.yajith.messaging.FirstTime.FirstActivity;
import com.yajith.messaging.MainActivity;
import com.yajith.messaging.R;
import com.yajith.messaging.SharedPref.SharedPref;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class VideoChatActivity extends AppCompatActivity implements  Session.SessionListener, PublisherKit.PublisherListener, SubscriberKit.SubscriberListener{
    private static String API_key="46835294";
    private static String SESSION_ID="2_MX40NjgzNTI5NH5-MTU5NDQ3ODIxOTQwMX5vU2hqbll1cE0xeGJuSWxrRXdxWFdjaWV-fg";
    private static String TOKEN="T1==cGFydG5lcl9pZD00NjgzNTI5NCZzaWc9MWZjN2I2NzI5MmUzOTc5OGEwZmZkZmFkOGMyNWIwOTEzMTJhZDI4ODpzZXNzaW9uX2lkPTJfTVg0ME5qZ3pOVEk1Tkg1LU1UVTVORFEzT0RJeE9UUXdNWDV2VTJocWJsbDFjRTB4ZUdKdVNXeHJSWGR4V0ZkamFXVi1mZyZjcmVhdGVfdGltZT0xNTk0NDc4MzY0Jm5vbmNlPTAuOTc4NDk3NTkzMTc1MjkxMyZyb2xlPXB1Ymxpc2hlciZleHBpcmVfdGltZT0xNTk3MDcwMzU3JmluaXRpYWxfbGF5b3V0X2NsYXNzX2xpc3Q9";
    private static final String LOG_TAG = VideoChatActivity.class.getSimpleName();
    private static final int RC_VIDEO_APP_PERM=124;
    private ImageView closeVideoChatBtn;
    private String userID;
    private FrameLayout mPublisherViewController;
    private FrameLayout mSubscriberViewController;
    private Session mSession;
    private SharedPref sharedPref;
    private Publisher mPublisher;
    private Subscriber mSubscriber;
    private String receiveruid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_chat);
        sharedPref=new SharedPref();
        sharedPref.first(getApplicationContext());
        userID=sharedPref.getuid();
        closeVideoChatBtn=findViewById(R.id.close_video_chat_btn);
        closeVideoChatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Flag.setCall(1);
                FirebaseDatabase.getInstance().getReference().child("User").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.child(userID).hasChild("Ringing"))
                        {
                            String senduid=dataSnapshot.child(userID).child("Ringing").child("calling").getValue(String.class);
                            FirebaseDatabase.getInstance().getReference().child("User").child(userID).child("Ringing").removeValue();
                            FirebaseDatabase.getInstance().getReference().child("User").child(senduid).child("Calling").removeValue();
                            if(mPublisher != null)
                            {
                                mPublisher.destroy();
                            }
                            if(mSubscriber != null)
                            {
                                mSubscriber.destroy();

                            }
                            
                           // startActivity(new Intent(VideoChatActivity.this,MainActivity.class));
                            finish();

                        }

                        if(dataSnapshot.child(userID).hasChild("Calling"))
                        {
                            String receiveruids=dataSnapshot.child(userID).child("Calling").child("calling").getValue(String.class);
                            FirebaseDatabase.getInstance().getReference().child("User").child(userID).child("Calling").removeValue();
                            FirebaseDatabase.getInstance().getReference().child("User").child(receiveruids).child("Ringing").removeValue();
                            if(mPublisher != null)
                            {
                                mPublisher.destroy();
                            }
                            if(mSubscriber != null)
                            {
                                mSubscriber.destroy();
                            }

                            //startActivity(new Intent(VideoChatActivity.this, MainActivity.class));
                            finish();
                        }
                      else {
                            if(mPublisher != null)
                            {
                                mPublisher.destroy();
                            }
                            if(mSubscriber != null)
                            {
                                mSubscriber.destroy();
                            }

                            //startActivity(new Intent(VideoChatActivity.this, MainActivity.class));
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
        requestPermissions();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseDatabase.getInstance().getReference().child("User").child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.hasChild("Calling")||!snapshot.hasChild("Ringing"))
                {
                    Log.i("yes","t");
                    //finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults,VideoChatActivity.this);

    }
    @AfterPermissionGranted(RC_VIDEO_APP_PERM)
    private void requestPermissions()
    {
        String[] perms={Manifest.permission.INTERNET, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO};
        if(EasyPermissions.hasPermissions(this,perms))
        {
            mPublisherViewController =findViewById(R.id.publisher_container);
            mSubscriberViewController =findViewById(R.id.subscriber_container);
            //1.Inialize and connect to session
            mSession=new Session.Builder(this,API_key,SESSION_ID).build();
            mSession.setSessionListener(VideoChatActivity.this);
            mSession.connect(TOKEN);

        }
        else
        {
            EasyPermissions.requestPermissions(this,"Hey this app needs Mic and Camera.Please allow",RC_VIDEO_APP_PERM,perms);
        }
    }

    @Override
    public void onStreamCreated(PublisherKit publisherKit, Stream stream) {
        Log.d(LOG_TAG, "onStreamCreated: Publisher Stream Created. Own stream "+stream.getStreamId());
    }

    @Override
    public void onStreamDestroyed(PublisherKit publisherKit, Stream stream) {
        Log.d(LOG_TAG, "onStreamDestroyed: Publisher Stream Destroyed. Own stream "+stream.getStreamId());
    }

    @Override
    public void onError(PublisherKit publisherKit, OpentokError opentokError) {
        Log.e(LOG_TAG, "onError: "+opentokError.getErrorDomain() + " : " +
                opentokError.getErrorCode() +  " - "+opentokError.getMessage());

        showOpenTokError(opentokError);
    }
//2.Publishing a stream to the session
    @Override
    public void onConnected(Session session) {
        Log.i(LOG_TAG,"Session Connected");
        mPublisher=new Publisher.Builder(this).build();
        mPublisher.setPublisherListener(VideoChatActivity.this);
        mPublisherViewController.addView(mPublisher.getView());
        if(mPublisher.getView() instanceof GLSurfaceView)
        {
            ((GLSurfaceView) mPublisher.getView()).setZOrderOnTop(true);
        }
        mSession.publish(mPublisher);

    }

    @Override
    public void onDisconnected(Session session) {
        Log.i(LOG_TAG,"Stream Disconnected");


    }
//3.Subscribing through the streams
    @Override
    public void onStreamReceived(Session session, Stream stream) {
                Log.i(LOG_TAG,"Stream Received");
                if(mSubscriber==null)
                {
                    mSubscriber=new Subscriber.Builder(this,stream).build();
                    mSubscriber.setSubscriberListener(this);
                    mSession.subscribe(mSubscriber);
                    mSubscriberViewController.addView(mSubscriber.getView());
                }



    }

    @Override
    public void onStreamDropped(Session session, Stream stream) {
        Log.i(LOG_TAG,"Stream Dropped");
        if(mSubscriber!=null)
        {
            mSubscriber= null;
            mSubscriberViewController.removeAllViews();
        }
    }

    @Override
    public void onError(Session session, OpentokError opentokError) {
        Log.e(LOG_TAG, "Session error: " + opentokError.getMessage());
        Log.e(LOG_TAG, "onError: "+ opentokError.getErrorDomain() + " : " +
                opentokError.getErrorCode() + " - "+opentokError.getMessage() + " in session: "+ session.getSessionId());

        showOpenTokError(opentokError);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onConnected(SubscriberKit subscriberKit) {
        Log.d(LOG_TAG, "onConnected: Subscriber connected. Stream: "+subscriberKit.getStream().getStreamId());
    }

    @Override
    public void onDisconnected(SubscriberKit subscriberKit) {
        Log.d(LOG_TAG, "onDisconnected: Subscriber disconnected. Stream: "+subscriberKit.getStream().getStreamId());
    }

    @Override
    public void onError(SubscriberKit subscriberKit, OpentokError opentokError) {
        Log.e(LOG_TAG, "onError: "+opentokError.getErrorDomain() + " : " +
                opentokError.getErrorCode() +  " - "+opentokError.getMessage());

        showOpenTokError(opentokError);
    }
    private void showOpenTokError(OpentokError opentokError) {

        Toast.makeText(this, opentokError.getErrorDomain().name() +": " +opentokError.getMessage() + " Please, see the logcat.", Toast.LENGTH_LONG).show();
        finish();
    }
}
