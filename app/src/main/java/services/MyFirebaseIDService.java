package services;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFirebaseIDService extends FirebaseInstanceIdService {

   String TAG = "MyFirebaseIdService";

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
           Log.d(TAG, "Refreshed token: " + refreshedToken);
    }
}

