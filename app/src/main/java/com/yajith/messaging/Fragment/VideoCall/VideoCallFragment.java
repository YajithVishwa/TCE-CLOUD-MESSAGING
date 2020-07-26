package com.yajith.messaging.Fragment.VideoCall;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yajith.messaging.FirstTime.Name;
import com.yajith.messaging.Fragment.Allcontacts.AllContacts;
import com.yajith.messaging.Fragment.Allcontacts.CustomAdapter;
import com.yajith.messaging.MainActivity;
import com.yajith.messaging.R;
import com.yajith.messaging.SharedPref.SharedPref;

import java.util.ArrayList;


public class VideoCallFragment extends Fragment {
    ListView listView;
    String[] name,phone;
    Activity activity;
    ArrayList<Name> arrayList;
    Context context;
    Context mainContext;
    SharedPref pref;
    ProgressDialog progressDialog;
    String myphone;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.videocall_fragment,container,false);
        progressDialog=new ProgressDialog(getContext());
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        pref=new SharedPref();
        mainContext=getActivity().getApplicationContext();
        pref.first(getActivity().getApplicationContext());
        myphone=pref.retrive();
        listView=view.findViewById(R.id.list);
        activity=getActivity();
        context=getContext();
        Async async=new Async();
        async.execute();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(getActivity(),CallingActivity.class);
                intent.putExtra("sender",myphone);
                intent.putExtra("receiver",arrayList.get(i).getPh());
                intent.putExtra("name",arrayList.get(i).getName());
                startActivity(intent);
            }
        });

        return view;
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
          arrayList=new ArrayList<>();
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
                            String name = getContactName(phone, mainContext);
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
                progressDialog.dismiss();
            }
            else
            {
                progressDialog.dismiss();
                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                builder.setTitle("Alert").setMessage("Error Occured").setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
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

}
