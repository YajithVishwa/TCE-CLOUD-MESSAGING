package com.yajith.messaging.Fragment.Chat.BottomDialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.yajith.messaging.R;

import java.util.ArrayList;
import java.util.Locale;

import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;

public class AttachDialog extends BottomSheetDialogFragment {
    SpeechRecognizer speechRecognizer;
    int count=0;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.custom_attach_dialog,container,false);
        ImageButton cam=view.findViewById(R.id.cam);
        ImageButton mic=view.findViewById(R.id.mic);
        speechRecognizer=SpeechRecognizer.createSpeechRecognizer(getContext());
        cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectDialog selectDialog=new SelectDialog();
                selectDialog.show(getFragmentManager(),"select");
                dismiss();
            }
        });
        mic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                MicDialog micDialog=new MicDialog();
                micDialog.show(getFragmentManager(),"MIC");
            }
        });
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(speechRecognizer!=null) {
            speechRecognizer.destroy();
        }
        }
}
