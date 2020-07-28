package com.yajith.messaging.Fragment.Chat.BottomDialog;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;
import com.yajith.messaging.Fragment.Chat.ChatActivity;
import com.yajith.messaging.R;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;

public class SelectDialog extends BottomSheetDialogFragment {
    ImageButton camera,gallery;
    String text="";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root=inflater.inflate(R.layout.custom_dialog_select,container,false);
        camera=root.findViewById(R.id.camera);
        gallery=root.findViewById(R.id.gallery);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, 1002);
            }
        });
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), 1004);
            }
        });
        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==1002)
        {
            if(resultCode== Activity.RESULT_OK)
            {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                sendphoto(photo);
            }
        }
        if(requestCode==1004)
        {
            if(resultCode== Activity.RESULT_OK)
            {
                try {
                    InputStream inputStream=getActivity().getContentResolver().openInputStream(data.getData());
                    Bitmap bitmap=BitmapFactory.decodeStream(inputStream);
                    sendphoto(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }


            }
        }
    }
    public void sendphoto(Bitmap bitmap)
    {
        final FirebaseVisionImage firebaseVisionImage=FirebaseVisionImage.fromBitmap(bitmap);
        FirebaseVisionTextRecognizer firebaseVisionTextRecognizer=FirebaseVision.getInstance().getOnDeviceTextRecognizer();
        firebaseVisionTextRecognizer.processImage(firebaseVisionImage).addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
            @Override
            public void onSuccess(FirebaseVisionText firebaseVisionText) {
                List<FirebaseVisionText.TextBlock> list=firebaseVisionText.getTextBlocks();
                if(list.size()==0) {
                    Toast.makeText(getActivity(), "No Text", Toast.LENGTH_SHORT).show();
                    dismiss();
                }
                else
                {
                    for(FirebaseVisionText.TextBlock block:firebaseVisionText.getTextBlocks())
                    {
                        text+=block.getText()+"  ";
                        //Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
                    }
                    if(getActivity()!=null) {
                        if(getActivity().findViewById(R.id.chattext)!=null) {
                            EmojiconEditText emojiconEditText = getActivity().findViewById(R.id.chattext);
                            emojiconEditText.setText(text);
                        }
                    }
                    dismiss();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });
    }
}
