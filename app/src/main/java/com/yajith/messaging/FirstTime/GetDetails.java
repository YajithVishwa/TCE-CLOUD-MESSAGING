package com.yajith.messaging.FirstTime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.yajith.messaging.R;
import com.yajith.messaging.SharedPref.SharedPref;

import java.util.HashMap;
import java.util.Map;

public class GetDetails extends AppCompatActivity {
    EditText editText,editText1,editText2,editText3;
    String name,age,email,ph,uid,aadhar;
    Button button;
    Context context;
    Dialog dialog;
    SharedPref sharedPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
        setContentView(R.layout.activity_get_details);
        ph=getIntent().getExtras().getString("phone");
        uid=getIntent().getExtras().getString("uid");
        editText=findViewById(R.id.name);
        sharedPref=new SharedPref();
        sharedPref.first(getApplicationContext());
        dialog = new Dialog(GetDetails.this, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_progress_bar);
        FirebaseDatabase.getInstance().getReference("User").keepSynced(true);
        editText1=findViewById(R.id.age);
        editText2=findViewById(R.id.email);
        editText3=findViewById(R.id.aadhar);
        button=findViewById(R.id.next);
        context=this;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name=editText.getText().toString();
                age=editText1.getText().toString();
                email=editText2.getText().toString();
                aadhar=editText3.getText().toString();
                if(isNetworkAvailable()==false)
                {
                    AlertDialog.Builder builder=new AlertDialog.Builder(context);
                    builder.setTitle("Alert").setMessage("InActive Network").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            return;
                        }
                    });
                    AlertDialog alertDialo=builder.create();
                    alertDialo.show();
                    return;
                }
                if(name.equals(""))
                {
                    AlertDialog.Builder builder=new AlertDialog.Builder(context);
                    builder.setTitle("Alert").setMessage("Enter Name").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            return;
                        }
                    });
                    AlertDialog alertDialo=builder.create();
                    alertDialo.show();
                }
                else if(age.equals(""))
                {
                    AlertDialog.Builder builder=new AlertDialog.Builder(context);
                    builder.setTitle("Alert").setMessage("Enter Age").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            return;
                        }
                    });
                    AlertDialog alertDialo=builder.create();
                    alertDialo.show();
                }
                else if(email.equals(""))
                {
                    AlertDialog.Builder builder=new AlertDialog.Builder(context);
                    builder.setTitle("Alert").setMessage("Enter Email").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            return;
                        }
                    });
                    AlertDialog alertDialo=builder.create();
                    alertDialo.show();
                }
                else if(aadhar.equals(""))
                {
                    AlertDialog.Builder builder=new AlertDialog.Builder(context);
                    builder.setTitle("Alert").setMessage("Enter Aadhar Number").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            return;
                        }
                    });
                    AlertDialog alertDialo=builder.create();
                    alertDialo.show();
                }
                else if(aadhar.length()!=12)
                {
                    AlertDialog.Builder builder=new AlertDialog.Builder(context);
                    builder.setTitle("Alert").setMessage("InValid Aadhar Number").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            return;
                        }
                    });
                    AlertDialog alertDialo=builder.create();
                    alertDialo.show();
                }
                else {
                    button.setEnabled(false);
                    if(!dialog.isShowing())
                    {
                        dialog.show();
                    }
                    Map<String,Object> map=new HashMap<>();
                    map.put("name",name);
                    map.put("age",age);
                    map.put("email",email);
                    map.put("aadhar",aadhar);
                    FirebaseFirestore.getInstance().collection("Message").document(ph).set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()) {
                                if(dialog.isShowing())
                                {
                                    dialog.dismiss();
                                }
                                Intent intent = new Intent(GetDetails.this, LoadContact.class);
                                sharedPref.uid(uid);
                                sharedPref.insert(ph);
                                intent.putExtra("ph", ph);
                                intent.putExtra("uid",uid);
                                startActivity(intent);
                            }
                            else
                            {
                                if(dialog.isShowing())
                                {
                                    dialog.dismiss();
                                }
                                button.setEnabled(true);
                                Toast.makeText(GetDetails.this, "Error Contact Admin", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }

            }
        });

    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }
}
