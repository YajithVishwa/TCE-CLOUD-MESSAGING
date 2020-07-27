package com.yajith.messaging.Fragment.Chat.BottomDialog;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yajith.messaging.R;

public class CopyDialog extends BottomSheetDialogFragment {
    String text,date;
    boolean seen;
    int type;
    String myphone;
    public CopyDialog(String text,String date,boolean seen,int type,String myphone)
    {
        this.text=text;
        this.date=date;
        this.seen=seen;
        this.type=type;
        this.myphone=myphone;
    }
    public CopyDialog()
    {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root=inflater.inflate(R.layout.custom_dialog,container,false);
        if(savedInstanceState!=null) {
            text = savedInstanceState.getString("text");
            date=savedInstanceState.getString("date");
            seen=savedInstanceState.getBoolean("seen");
            type=savedInstanceState.getInt("type");
        }
        TextView textView=root.findViewById(R.id.textcopy);
        Button button=root.findViewById(R.id.copy);
        Button info=root.findViewById(R.id.info);
        Button unsend=root.findViewById(R.id.unsend);
        if(type==0)
        {
            unsend.setVisibility(View.GONE);
        }
        textView.setText(text);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboardManager=(ClipboardManager)getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData=ClipData.newPlainText("text",text);
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(getContext().getApplicationContext(), "Copied : "+text, Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                InfoDialog infoDialog=new InfoDialog(text,date,seen,type);
                infoDialog.show(getParentFragmentManager(),"info");
            }
        });
        unsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Thread thread=new Thread(new Runnable() {
                    @Override
                    public void run() {
                        FirebaseDatabase.getInstance().getReference().child("Chat").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for(DataSnapshot dataSnapshot:snapshot.getChildren())
                                {
                                    if(dataSnapshot.child("msg").getValue(String.class).equals(text))
                                    {
                                        if(dataSnapshot.child("sender").getValue(String.class).equals(myphone))
                                        {
                                            FirebaseDatabase.getInstance().getReference().child("Chat").child(dataSnapshot.getKey()).removeValue();
                                            dismiss();
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(getActivity(), "Error Occurs", Toast.LENGTH_SHORT).show();

                            }
                        });
                    }
                });
                thread.start();

            }
        });
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
