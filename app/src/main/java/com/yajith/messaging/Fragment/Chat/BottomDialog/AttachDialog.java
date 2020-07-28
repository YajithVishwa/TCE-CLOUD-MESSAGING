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
                final Dialog dialog=new Dialog(getContext());
                dialog.setContentView(R.layout.custom_dialog_speech);
                final ImageButton micbutton=dialog.findViewById(R.id.micon);
                final Intent speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                speechRecognizer.setRecognitionListener(new RecognitionListener() {
                    @Override
                    public void onReadyForSpeech(Bundle bundle) {

                    }

                    @Override
                    public void onBeginningOfSpeech() {
                        Toast.makeText(getActivity(), "Speak", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onRmsChanged(float v) {

                    }

                    @Override
                    public void onBufferReceived(byte[] bytes) {

                    }

                    @Override
                    public void onEndOfSpeech() {

                    }

                    @Override
                    public void onError(int i) {
                        dialog.dismiss();
                        Toast.makeText(getActivity().getApplicationContext(), "Error Occurred", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onResults(Bundle bundle) {
                        ArrayList<String> data = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                        if(data.size()==0)
                        {
                            dialog.dismiss();
                        }
                        else
                        {
                            if(getActivity()!=null)
                            {
                                if(getActivity().findViewById(R.id.chattext)!=null)
                                {
                                    EmojiconEditText emojiconEditText=getActivity().findViewById(R.id.chattext);
                                    emojiconEditText.setText(data.get(0));
                                    Toast.makeText(getActivity().getApplicationContext(), data.get(0), Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                            }
                        }
                    }

                    @Override
                    public void onPartialResults(Bundle bundle) {

                    }

                    @Override
                    public void onEvent(int i, Bundle bundle) {

                    }
                });
                micbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        count++;
                        if(count==1) {
                            speechRecognizer.startListening(speechRecognizerIntent);
                            micbutton.setImageResource(R.drawable.speak);
                        }
                        else
                        {
                            count=0;
                            speechRecognizer.stopListening();
                            micbutton.setImageResource(R.drawable.mic);
                            dialog.dismiss();
                        }
                    }
                });
                dialog.show();
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
