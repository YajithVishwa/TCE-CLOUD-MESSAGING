package com.yajith.messaging.Fragment.Chat.BottomDialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.yajith.messaging.R;

public class InfoDialog extends BottomSheetDialogFragment {
    String text,date;boolean seen;int type;

    public InfoDialog(String text,String date, boolean seen, int type) {
        this.date = date;
        this.text=text;
        this.seen = seen;
        this.type = type;
    }

    public InfoDialog()
    {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root=inflater.inflate(R.layout.custom_info_dialog,container,false);
        if(savedInstanceState!=null) {
            text = savedInstanceState.getString("text");
            date=savedInstanceState.getString("date");
            seen=savedInstanceState.getBoolean("seen");
            type=savedInstanceState.getInt("type");
        }
        TextView textView=root.findViewById(R.id.copytext);
        TextView textView1=root.findViewById(R.id.date);
        TextView textView2=root.findViewById(R.id.seen);
        TextView textView3=root.findViewById(R.id.type);
        textView.setText(text);
        textView1.setText(date);
        if(seen==true)
        {
            textView2.setText("Seen");
        }
        else
        {
            textView2.setText("Delivered");
        }
        if(type==0)
        {
            textView3.setText("Received");
        }
        else
        {
            textView3.setText("Send");
        }

        return root;
    }
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("text",text);
        outState.putString("date",date);
        outState.putBoolean("seen",seen);
        outState.putInt("type",type);
    }
}
