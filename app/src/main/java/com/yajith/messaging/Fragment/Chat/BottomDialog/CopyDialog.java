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

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.yajith.messaging.R;

public class CopyDialog extends BottomSheetDialogFragment {
    String text,date;
    boolean seen;
    int type;
    public CopyDialog(String text,String date,boolean seen,int type)
    {
        this.text=text;
        this.date=date;
        this.seen=seen;
        this.type=type;
    }
    public CopyDialog()
    {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root=inflater.inflate(R.layout.custom_dialog,container,false);
        TextView textView=root.findViewById(R.id.textcopy);
        Button button=root.findViewById(R.id.copy);
        Button info=root.findViewById(R.id.info);
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
        return root;
    }
}
