package com.yajith.messaging.Fragment.Chat;

import android.app.Activity;
import android.content.Context;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.yajith.messaging.R;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<DataAdap> {
    Activity activity;
    ArrayList<DataAdap> adaps;
    CustomAdapter(Activity activity,int resource, ArrayList<DataAdap> adaps)
    {
        super(activity,resource,adaps);
        this.activity=activity;
        this.adaps=adaps;
    }


    @Override
    public int getItemViewType(int position) {
        return adaps.get(position).getType();
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v=view;
        int type=getItemViewType(i);
        if(v==null)
        {
            LayoutInflater layoutInflater=activity.getLayoutInflater();
            if(type==0)
            {
                v=layoutInflater.inflate(R.layout.chat_left,null,false);
            }
            else {
                v = layoutInflater.inflate(R.layout.chat_right, null, false);
                TextView seen=v.findViewById(R.id.seen);
                if(i==adaps.size()-1)
                {
                    if(adaps.get(i).isSeen())
                    {
                        seen.setText("Seen");
                    }
                    else
                    {
                        seen.setText("Delivered");
                    }
                }
                else
                {
                    seen.setVisibility(View.INVISIBLE);
                }
            }
            TextView textView=v.findViewById(R.id.text);
            TextView date=v.findViewById(R.id.date);
            textView.setText(adaps.get(i).getText());
            date.setText(adaps.get(i).getDate());

        }
        return v;
    }
}
