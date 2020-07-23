package com.yajith.messaging.FirstTime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.yajith.messaging.R;

import java.util.HashMap;
import java.util.Map;

public class GetDetails extends AppCompatActivity {
    EditText editText,editText1,editText2,editText3;
    String name,age,email,ph,uid,aadhar;
    Button button;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_details);
        ph=getIntent().getExtras().getString("phone");
        uid=getIntent().getExtras().getString("uid");
        editText=findViewById(R.id.name);
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
                if(name.equals(""))
                {

                }
                else if(age.equals(""))
                {

                }
                else if(email.equals(""))
                {

                }
                else if(aadhar.equals(""))
                {

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
                    Map<String,Object> map=new HashMap<>();
                    map.put("name",name);
                    map.put("age",age);
                    map.put("email",email);
                    map.put("aadhar",aadhar);
                    FirebaseFirestore.getInstance().collection("Message").document(ph).set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()) {
                                Intent intent = new Intent(GetDetails.this, LoadContact.class);
                                intent.putExtra("ph", ph);
                                intent.putExtra("uid",uid);
                                startActivity(intent);
                            }
                            else
                            {
                                button.setEnabled(true);
                                Toast.makeText(GetDetails.this, "Error Contact Admin", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }

            }
        });

    }
}
