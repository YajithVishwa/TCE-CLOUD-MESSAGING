package com.yajith.messaging.Fragment.Allcontacts;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.common.internal.DialogRedirect;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.yajith.messaging.FirstTime.GetDetails;
import com.yajith.messaging.FirstTime.LoadContact;
import com.yajith.messaging.FirstTime.Name;
import com.yajith.messaging.Fragment.Chat.ChatActivity;
import com.yajith.messaging.MainActivity;
import com.yajith.messaging.R;
import com.yajith.messaging.SharedPref.SharedPref;

import java.util.ArrayList;

public class AllContacts extends AppCompatActivity {
    ListView listView;
    String[] name,phone;
    Activity activity;
    ArrayList<Name> arrayList;
    Context context;
    String myphone;
    SharedPref sharedPref;
    Dialog dialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
        setContentView(R.layout.all_contacts_fragment);
        dialog = new Dialog(AllContacts.this, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_progress_bar);
        dialog.show();
        sharedPref=new SharedPref();
        sharedPref.first(getApplicationContext());
        myphone=sharedPref.retrive();
        getSupportActionBar().setTitle("AllContacts");
        listView=findViewById(R.id.list);
        activity=this;
        context=this;
        Async async=new Async();
        async.execute();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                finish();
                Intent intent=new Intent(AllContacts.this, ChatActivity.class);
                intent.putExtra("name",name[i]);
                intent.putExtra("ph",phone[i]);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(dialog.isShowing())
        {
            dialog.dismiss();
        }
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
    class Async extends AsyncTask<Void,ArrayList<Name>,String>
    {
        @Override
        protected String doInBackground(Void... voids) {
            final ArrayList<Name> arrayList=new ArrayList<>();
            DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("User");
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    for(DataSnapshot dataSnapshot:snapshot.getChildren())
                    {
                        if(dataSnapshot.child("phone").getValue().equals(myphone))
                        {

                        }
                        else {
                            String phone = "+91 " + String.valueOf(dataSnapshot.child("phone").getValue());
                            String name = getContactName(phone, getApplicationContext());
                            if(name.equals(""))
                            {

                            }
                            else {
                                arrayList.add(new Name(name, phone));
                            }
                        }
                    }
                    publishProgress(arrayList);
                    return;
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            return "null";
        }

        @Override
        protected void onProgressUpdate(ArrayList<Name>... values) {
            super.onProgressUpdate(values);
            if(values[0]!=null)
            {
                arrayList=values[0];
                int count=arrayList.size();
                name=new String[count];
                phone=new String[count];
                int j=0;
                for (Name arrayList1:arrayList)
                {
                    String nameContact=arrayList1.getName();
                    String phonec=arrayList1.getPh();
                    phone[j]=phonec;
                    name[j]=nameContact;
                    j++;
                }
                CustomAdapter customAdapter=new CustomAdapter(activity,name,phone);
                listView.setAdapter(customAdapter);
                if(dialog.isShowing())
                {
                    dialog.dismiss();
                }
            }
            else
            {
                if(dialog.isShowing())
                {
                    dialog.dismiss();
                }
                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                builder.setTitle("Alert").setMessage("Error Occured").setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });
                AlertDialog alertDialog=builder.create();
                alertDialog.show();
            }

        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(dialog.isShowing())
        {
            dialog.dismiss();
        }
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }
}
