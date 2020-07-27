package com.yajith.messaging.Fragment.Chat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.BundleCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.yajith.messaging.FirstTime.Users;
import com.yajith.messaging.Fragment.APIService;
import com.yajith.messaging.Fragment.Chat.BottomDialog.AttachDialog;
import com.yajith.messaging.Fragment.Chat.BottomDialog.CopyDialog;
import com.yajith.messaging.Fragment.Chat.BottomDialog.SelectDialog;
import com.yajith.messaging.Fragment.VideoCall.CallingActivity;
import com.yajith.messaging.Notification.Client;
import com.yajith.messaging.Notification.MyResponse;
import com.yajith.messaging.Notification.NotifiData;
import com.yajith.messaging.Notification.Sender;
import com.yajith.messaging.Notification.Token;
import com.yajith.messaging.R;
import com.yajith.messaging.SharedPref.SharedPref;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity {
    String name,receiverphone,myphone;
    FloatingActionButton floatingActionButton;
    EmojiconEditText editText;
    ListView listView;
    Activity activity;
    String uid;
    View root;
    DatabaseReference databaseReference;
    ValueEventListener valueEventListener;
    SharedPref sharedPref;
    ArrayList<DataAdap> adaps;
    APIService apiService;
    ImageView imageView,camera;
    Vibrator v;
    Context context;
    boolean notific=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
        setContentView(R.layout.activity_chat);
        sharedPref=new SharedPref();
        context=getApplicationContext();
        name=getIntent().getExtras().getString("name");
        receiverphone=getIntent().getExtras().getString("ph");
        v=(Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
        if(receiverphone.startsWith("+91 "))
        {
            receiverphone=receiverphone.substring(4,receiverphone.length());
        }
        imageView=findViewById(R.id.imageemoji);
        camera=findViewById(R.id.imagecamera);
        root=findViewById(R.id.root);
        sharedPref.first(getApplicationContext());
        uid=sharedPref.getuid();
        myphone=sharedPref.retrive();
        apiService= Client.getRetrofit("https://fcm.googleapis.com/fcm/send/").create(APIService.class);
        activity=this;
        adaps=new ArrayList<>();
        getSupportActionBar().setTitle(name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        listView=findViewById(R.id.list_view);
        floatingActionButton=findViewById(R.id.floating_action_button);
        editText=findViewById(R.id.chattext);
        EmojIconActions emojIconActions=new EmojIconActions(this,root,editText,imageView);
        emojIconActions.setUseSystemEmoji(true);
        emojIconActions.setKeyboardListener(new EmojIconActions.KeyboardListener() {
            @Override
            public void onKeyboardOpen() {

            }

            @Override
            public void onKeyboardClose() {

            }
        });
        readMessage();
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text=editText.getText().toString();
                notific=true;
                if(text.isEmpty())
                {
                    return;
                }
                else
                {
                    sendmes(text,receiverphone,myphone);
                }
            }
        });
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(ChatActivity.this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(ChatActivity.this,new String[]{Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE},100);
                }
                else
                {
                    AttachDialog dialog=new AttachDialog();
                    dialog.show(getSupportFragmentManager(),"Attach");
                }
            }
        });
        seenMessage(myphone);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.call_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        switch (id)
        {
            case R.id.call:
                if(ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CALL_PHONE},100);
                }
                else
                {
                    Intent call=new Intent(Intent.ACTION_CALL);
                    call.setData(Uri.parse("tel:"+receiverphone));
                    startActivity(call);
                }
                break;
            case R.id.video:
                if(ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CALL_PHONE,Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.RECORD_AUDIO},100);
                }
                else
                {

                    Intent intent=new Intent(ChatActivity.this,CallingActivity.class);
                    intent.putExtra("sender",myphone);
                    intent.putExtra("receiver",receiverphone);
                    intent.putExtra("name",name);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                    break;

                }
                break;
            case android.R.id.home:
                finish();

                break;

        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==100)
        {
            if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                Intent call=new Intent(Intent.ACTION_CALL);
                call.setData(Uri.parse("tel:"+receiverphone));
                startActivity(call);
            }
        }
    }

    private void seenMessage(final String myphones)
    {
        try {

            databaseReference = FirebaseDatabase.getInstance().getReference("Chat");
            valueEventListener = databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Chat chat = dataSnapshot.getValue(Chat.class);
                        if (chat.getReceiver().equals(myphones) && chat.getSender().equals(receiverphone)) {
                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("isseen", true);
                            dataSnapshot.getRef().updateChildren(hashMap);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        catch (NullPointerException e)
        {
            Toast.makeText(activity, "Error Occurs", Toast.LENGTH_SHORT).show();
            finish();
        }


    }



    private void sendmes(final String text, final String phone, String myphone)
    {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String formattedDate = df.format(c.getTime());
        //adaps.add(new DataAdap(text,1,formattedDate));
        HashMap<String,Object> map=new HashMap<>();
        map.put("sender",myphone);
        map.put("receiver",phone);
        map.put("msg",text);
        map.put("date",formattedDate);
        map.put("isseen",false);
        FirebaseDatabase.getInstance().getReference().child("Chat")
                .push().setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                editText.getText().clear();
                CustomAdapter customAdapter=new CustomAdapter(activity,0,adaps);
                listView.setAdapter(customAdapter);
                listView.setStackFromBottom(true);
                customAdapter.notifyDataSetChanged();
            }
        });
        final String msg=text;
        FirebaseDatabase.getInstance().getReference().child("User").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users user=snapshot.getValue(Users.class);
                if(notific) {
                    sendNotification(phone,user.getUid(), text);
                }
                notific=false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void sendNotification(final String phone, final String name, final String text) {
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Token");
        Query query=databaseReference.orderByKey().equalTo(phone);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    Token token=dataSnapshot.getValue(Token.class);
                    NotifiData notifiData=new NotifiData(uid,R.mipmap.ic_launcher,phone+": "+text,"New Message",phone);
                    Sender sender=new Sender(notifiData,token.getToken());
                    apiService.sendNotification(sender).enqueue(new Callback<MyResponse>() {
                        @Override
                        public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                            if(response.code()==200)
                            {
                                if(response.body().success==1)
                                {
                                    Toast.makeText(ChatActivity.this, "Error Occurred", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<MyResponse> call, Throwable t) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void readMessage()
    {
        final CustomAdapter customAdapter=new CustomAdapter(activity,0,adaps);
        FirebaseDatabase.getInstance().getReference().child("Chat").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                adaps.clear();
                for(DataSnapshot ds:snapshot.getChildren())
                {
                    Chat chat=ds.getValue(Chat.class);
                    if((chat.getReceiver().equals(myphone)&&chat.getSender().equals(receiverphone)))
                    {
                        adaps.add(new DataAdap(chat.getMsg(),0,chat.getDate(),chat.isIsseen()));
                    }
                    if(chat.getReceiver().equals(receiverphone)&&chat.getSender().equals(myphone))
                    {
                        adaps.add(new DataAdap(chat.getMsg(),1,chat.getDate(),chat.isIsseen()));
                    }
                }

                listView.setAdapter(customAdapter);
                listView.setStackFromBottom(true);
                customAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                String copy=adaps.get(i).text;
                String date=adaps.get(i).date;
                boolean seen=adaps.get(i).seen;
                int type=adaps.get(i).type;
                PowerManager powerManager = (PowerManager)getSystemService(Context.POWER_SERVICE);
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
                        && powerManager.isPowerSaveMode()) {

                }
                else
                {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        v.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        v.vibrate(100);
                    }
                }
                CopyDialog copyDialog=new CopyDialog(copy,date,seen,type,myphone);
                copyDialog.show(getSupportFragmentManager(),"Copy");

                return true;
            }
        });
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
    protected void onPause() {
        super.onPause();
        isOnline(false);
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
        databaseReference.removeEventListener(valueEventListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isOnline(false);
    }
}
