package com.yajith.messaging.Fragment.RecentChat;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.iid.FirebaseInstanceId;
import com.yajith.messaging.FirstTime.FirstActivity;
import com.yajith.messaging.Fragment.Allcontacts.AllContacts;
import com.yajith.messaging.Fragment.Chat.Chat;
import com.yajith.messaging.Fragment.Chat.ChatActivity;
import com.yajith.messaging.Fragment.ContextClass;
import com.yajith.messaging.MainActivity;
import com.yajith.messaging.Notification.Token;
import com.yajith.messaging.R;
import com.yajith.messaging.SharedPref.SharedPref;

import java.util.ArrayList;

public class RecentChatFragment extends Fragment {
    ListView listView;
    ArrayList<String> name=new ArrayList<>();
    String myphone;
    String uid;
    SharedPref sharedPref;
    ArrayList<User> name1;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root=inflater.inflate(R.layout.recent_chat_fragment,container,false);
        listView=root.findViewById(R.id.recent);
        sharedPref=new SharedPref();
        sharedPref.first(getActivity().getApplicationContext());
        myphone=sharedPref.retrive(getActivity().getApplicationContext());
        uid=sharedPref.getuid();
        FirebaseDatabase.getInstance().getReference().child("Chat").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds:snapshot.getChildren())
                {
                    Chat chat=ds.getValue(Chat.class);
                    if(chat.getSender().equals(myphone))
                    {
                        String names=chat.getReceiver();
                        if(!name.contains(names)) {
                            name.add(names);
                        }
                    }
                    if(chat.getReceiver().equals(myphone))
                    {
                        String names=chat.getSender();
                        if(!name.contains(names)) {
                            name.add(names);
                        }

                    }
                }
                readchats();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(getActivity(), ChatActivity.class);
                intent.putExtra("name",name1.get(i).getName());
                intent.putExtra("ph",name1.get(i).getPh());
                startActivity(intent);
            }
        });
        updateToken(sharedPref.gettoken());
        return root;
    }
    private void updateToken(String refreshtoken)
    {
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Tokens");
        Token token=new Token(refreshtoken);
        databaseReference.child(uid).setValue(token);
    }

    private void readchats() {
        name1 = new ArrayList<>();
        if (getActivity() != null) {
            final CustomAdapter customAdapter = new CustomAdapter((MainActivity) getActivity(), name1);
            FirebaseDatabase.getInstance().getReference().child("User").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    name1.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                        String user = String.valueOf(dataSnapshot.child("phone").getValue());
                        boolean isOnline = dataSnapshot.child("isOnline").getValue(boolean.class);
                        if (name.contains(user)) {
                            String n = getContactName(user,ContextClass.context);
                            name1.add(new User(user, n, isOnline));
                        }
                    }
                    listView.setAdapter(customAdapter);
                    customAdapter.notifyDataSetChanged();
                }


                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


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
}
