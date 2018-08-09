package services;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.IOException;

public class MyFirebaseIDService extends FirebaseInstanceIdService {

   String TAG = "MyFirebaseIdService";

    @Override
    public void onTokenRefresh() {
        String refreshedToken = null;
        try {
            refreshedToken = FirebaseInstanceId.getInstance().getToken("513854329238", FirebaseMessaging.INSTANCE_ID_SCOPE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ;
           Log.d(TAG, "Refreshed token: " + refreshedToken);
    }
}

