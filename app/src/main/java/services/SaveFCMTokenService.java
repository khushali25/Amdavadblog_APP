package services;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.security.Key;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import Model.FCM_Device_Tokens;

public class SaveFCMTokenService extends Service {
    int count;
    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub

        if(intent != null){

            Bundle b = intent.getExtras();

            if(b != null) {
                String token = b.getString("TOKEN");

                sendRegistrationToServer(token);
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    private void sendRegistrationToServer(final String token) {
        // Add custom implementation, as needed.

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        DatabaseReference fcmDatabaseRef = ref.child("FCM_Device_Tokens");

        UUID uuid = UUID.randomUUID();
        String randomUUIDString = uuid.toString();

        HashMap<String,Object> myMap = new HashMap<>();
        myMap.put(randomUUIDString,token);

        fcmDatabaseRef.updateChildren(myMap);
    }
}

