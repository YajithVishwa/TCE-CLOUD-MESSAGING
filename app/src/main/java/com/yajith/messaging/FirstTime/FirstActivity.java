package com.yajith.messaging.FirstTime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.yajith.messaging.R;
import com.yajith.messaging.SharedPref.SharedPref;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class FirstActivity extends AppCompatActivity {
    EditText editText,editText1;
    Button button,otp,resend;
    Context context;
    SharedPref sharedPref;
    Dialog dialog;
    BroadcastReceiver broadcastReceiver=null;
    String cridentials,ph,otps=null;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
        setContentView(R.layout.activity_first);
        dialog = new Dialog(FirstActivity.this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_progress_bar);
        if(savedInstanceState!=null)
        {
            ph=savedInstanceState.getString("phone");
        }
        editText=findViewById(R.id.phone);
        context=this;
        sharedPref=new SharedPref();
        sharedPref.first(getApplicationContext());
        editText1=findViewById(R.id.OTP);
        otp=findViewById(R.id.otp);
        editText1.setVisibility(View.INVISIBLE);
        otp.setVisibility(View.INVISIBLE);
        resend=findViewById(R.id.resend);
        button=findViewById(R.id.submit);
        resend.setVisibility(View.INVISIBLE);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ph=editText.getText().toString();
                if(ph.equals(""))
                {
                    AlertDialog.Builder builder=new AlertDialog.Builder(context);
                    builder.setTitle("Alert").setMessage("Enter Phone Number").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            return;
                        }
                    });
                    AlertDialog alertDialo=builder.create();
                    alertDialo.show();

                }
                else
                {
                    if(ph.startsWith("+91"))
                    {
                        ph=ph.substring(3,ph.length());

                    }
                    sendsms();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            resend.setVisibility(View.VISIBLE);
                        }
                    },59000);
                }
            }
        });
        otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                otps=editText1.getText().toString();
                if(otps.equals(""))
                {
                    AlertDialog.Builder builder=new AlertDialog.Builder(context);
                    builder.setTitle("Alert").setMessage("Enter OTP").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            return;
                        }
                    });
                    AlertDialog alertDialo=builder.create();
                    alertDialo.show();
                }
                else
                {
                    otp.setEnabled(false);
                    dialog.show();
                    PhoneAuthCredential phoneAuthCredential=PhoneAuthProvider.getCredential(cridentials,otps);
                    signInWithPhoneAuthCredential(phoneAuthCredential);
                }
            }
        });
        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendsms();
            }
        });

    }
    private void sendsms()
    {
        String phone="+91"+ph;
        editText1.setVisibility(View.VISIBLE);
        otp.setVisibility(View.VISIBLE);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(phone, 60, TimeUnit.SECONDS, FirstActivity.this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                otp.setEnabled(true);
                if(dialog.isShowing())
                {
                    dialog.dismiss();
                }
                Toast.makeText(context, "Error Occurred Contact Admin", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                cridentials=s;
                button.setEnabled(false);
                editText.setEnabled(false);
                Toast.makeText(context, "Otp Sent", Toast.LENGTH_SHORT).show();
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                broad();
            }

            @Override
            public void onCodeAutoRetrievalTimeOut(@NonNull String s) {
                super.onCodeAutoRetrievalTimeOut(s);
            }
        });
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = task.getResult().getUser();
                            String token=FirebaseInstanceId.getInstance().getToken();
                            sharedPref.token(token);
                            final String uid=user.getUid();
                            Users users=new Users(uid,ph,true);
                            Map<String,Object> map=new HashMap<>();
                            map.put("uid",uid);
                            map.put("ph",ph);
                            map.put("isOnline",true);
                            map.put("calling","no");
                            map.put("picked","picked");
                            sharedPref.uid(uid);
                            sharedPref.insert(ph);
                            FirebaseDatabase.getInstance().getReference().child("User").child(uid).setValue(users).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    if(dialog.isShowing())
                                    {
                                        dialog.dismiss();
                                    }
                                    Intent i=new Intent(FirstActivity.this,GetDetails.class);
                                    i.putExtra("phone",ph);
                                    i.putExtra("uid",uid);
                                    startActivity(i);
                                }
                            });
                        } else {
                            otp.setEnabled(true);
                            if(dialog.isShowing())
                            {
                                dialog.dismiss();
                            }
                            Toast.makeText(FirstActivity.this, "Sign Up Failed", Toast.LENGTH_SHORT).show();
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(FirstActivity.this, "InValid OTP", Toast.LENGTH_SHORT).show();
                            }
                            button.setEnabled(true);
                            editText.setEnabled(true);
                            editText1.setVisibility(View.INVISIBLE);
                            otp.setVisibility(View.INVISIBLE);
                        }
                    }
                });
    }
    private void broad()
    {
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
        broadcastReceiver=new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent) {
                SmsMessage[] smsMessages= Telephony.Sms.Intents.getMessagesFromIntent(intent);
                for(SmsMessage message:smsMessages)
                {
                    String body=message.getMessageBody();
                    String[] splits=body.split(" ");
                    if(splits[splits.length-1].equals("code."));
                    {
                        editText1.setText(splits[0]);
                    }
                }
            }
        };
        registerReceiver(broadcastReceiver,intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(broadcastReceiver!=null) {
            unregisterReceiver(broadcastReceiver);
        }
        if(dialog.isShowing())
        {
            dialog.dismiss();
        }
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("phone",ph);
        outState.putString("otp",otps);

    }
}
