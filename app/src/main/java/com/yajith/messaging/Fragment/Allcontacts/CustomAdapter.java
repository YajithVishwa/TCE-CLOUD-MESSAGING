package com.yajith.messaging.Fragment.Allcontacts;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.yajith.messaging.R;

public class CustomAdapter extends ArrayAdapter<String> {
    String[] name,phone;
    Activity activity;
    public CustomAdapter(@NonNull Activity context,String[] name,String[] phone) {
        super(context, R.layout.custom_list_all,name);
        this.activity=context;
        this.name=name;
        this.phone=phone;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater=activity.getLayoutInflater();
        View root=layoutInflater.inflate(R.layout.custom_list_all,null,false);
        TextView nametex=root.findViewById(R.id.nameid);
        TextView ph=root.findViewById(R.id.phoneid);
        nametex.setText(name[position]);
        ph.setText(phone[position]);
        return root;
    }
}
