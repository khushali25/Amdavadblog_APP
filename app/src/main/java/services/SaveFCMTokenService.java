package services;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.common.StringUtils;

import java.util.HashMap;
import java.util.UUID;

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

    private boolean sendRegistrationToServer(final String token) {
        // Add custom implementation, as needed.
        UUID uuid = UUID.randomUUID();
        String randomUUIDString = uuid.toString();
        String randomstring = randomUUIDString.substring(0,10);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        switch (currentapiVersion) {

            case 23:
                DatabaseReference api23devices = ref.child("FCM_Device_Tokens").child("API_23_Devices");
                HashMap<String, Object> api23 = new HashMap<>();
                api23.put(randomstring, token);
                api23devices.updateChildren(api23);
                return true;

            case 24:
                DatabaseReference api24devices = ref.child("FCM_Device_Tokens").child("API_24_Devices");
                HashMap<String, Object> api24 = new HashMap<>();
                api24.put(randomstring, token);
                api24devices.updateChildren(api24);
                return true;

            case 25:
                DatabaseReference api25devices = ref.child("FCM_Device_Tokens").child("API_25_Devices");
                HashMap<String, Object> api25 = new HashMap<>();
                api25.put(randomstring, token);
                api25devices.updateChildren(api25);
                return true;

            case 26:
                DatabaseReference api26devices = ref.child("FCM_Device_Tokens").child("API_26_Devices");
                HashMap<String, Object> api26 = new HashMap<>();
                api26.put(randomstring, token);
                api26devices.updateChildren(api26);
                return true;

            case 27:
                DatabaseReference api27devices = ref.child("FCM_Device_Tokens").child("API_27_Devices");
                HashMap<String, Object> api27 = new HashMap<>();
                api27.put(randomstring, token);
                api27devices.updateChildren(api27);
                return true;

            case 28:
                DatabaseReference api28devices = ref.child("FCM_Device_Tokens").child("API_28_Devices");
                HashMap<String, Object> api28 = new HashMap<>();
                api28.put(randomstring, token);
                api28devices.updateChildren(api28);
                return true;

            default:
                DatabaseReference unknown = ref.child("FCM_Device_Tokens").child("Unknown_Devices");
                HashMap<String, Object> unknownapi = new HashMap<>();
                unknownapi.put(randomstring, token);
                unknown.updateChildren(unknownapi);
                return true;
        }
    }
}

