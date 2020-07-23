package com.yajith.messaging.Fragment.RecentChat;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yajith.messaging.Fragment.Chat.Chat;
import com.yajith.messaging.R;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<User> {
    ArrayList<User> name;
    Activity activity;
    String lastmsg;
    String myphone="9384902231";
    public CustomAdapter(@NonNull Activity context, ArrayList<User> name) {
        super(context, R.layout.custom_list_all,name);
        this.activity=context;
        this.name=name;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater=activity.getLayoutInflater();
        View root=layoutInflater.inflate(R.layout.custom_list_recent,null,false);
        TextView nametex=root.findViewById(R.id.nameid);
        nametex.setText(name.get(position).getName());
        TextView last=root.findViewById(R.id.last_msg);
        ImageView online=root.findViewById(R.id.online);
        if(name.get(position).isOnline()==true)
        {
            checklastmsg(name.get(position).getPh(),last);
        }
        else
        {
            //last.setText("**No Text**");
            last.setVisibility(View.INVISIBLE);
        }
        if(name.get(position).isOnline()==true)
        {
            online.setVisibility(View.VISIBLE);
        }
        else
        {
            online.setVisibility(View.INVISIBLE);
        }


        return root;
    }
    private void checklastmsg(final String ph, final TextView last)
    {
        lastmsg="No Msg";
        FirebaseDatabase.getInstance().getReference().child("Chat").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    Chat chat=dataSnapshot.getValue(Chat.class);
                    if(chat.getReceiver().equals(myphone)&&chat.getSender().equals(ph)||(chat.getSender().equals(myphone)&&chat.getReceiver().equals(ph)))
                    {
                        lastmsg=chat.getMsg();
                    }
                }
                switch (lastmsg)
                {
                    case "No Msg":
                        last.setText("No Text");
                        break;
                    default:
                        last.setText(lastmsg);
                }
                lastmsg="No Msg";
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
