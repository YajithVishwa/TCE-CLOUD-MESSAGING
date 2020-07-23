package com.yajith.messaging.Notification;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.yajith.messaging.SharedPref.SharedPref;

public class MyFirebaseIdService extends FirebaseInstanceIdService {
    SharedPref sharedPref;
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        String refreshtoken=FirebaseInstanceId.getInstance().getToken();
        if(firebaseUser!=null)
        {
            updateToken(refreshtoken);
        }
    }


    private void updateToken(String refreshtoken) {
        sharedPref=new SharedPref();
        sharedPref.first(getApplicationContext());
        String uid=sharedPref.getuid();
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Tokens");
        Token token=new Token(refreshtoken);
        databaseReference.child(uid).setValue(token);
    }
}
