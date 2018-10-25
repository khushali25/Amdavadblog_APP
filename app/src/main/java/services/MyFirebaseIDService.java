package services;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.util.Log;
import com.crashlytics.android.Crashlytics;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessaging;
import java.io.IOException;

public class MyFirebaseIDService extends FirebaseInstanceIdService {

   String TAG = "MyFirebaseIdService";
    Snackbar snackbar;
    @Override
    public void onTokenRefresh() {
        String refreshedToken = null;
        try {
            refreshedToken = FirebaseInstanceId.getInstance().getToken("513854329238", FirebaseMessaging.INSTANCE_ID_SCOPE);
        } catch (IOException e) {
            Crashlytics.logException(e);
            e.printStackTrace();
        }
        Log.d(TAG, "Refreshed token Called: " + refreshedToken);
        Intent intent = new Intent(MyFirebaseIDService.this, SaveFCMTokenService.class);
        intent.putExtra("TOKEN",refreshedToken);
        MyFirebaseIDService.this.startService(intent);
    }
}

