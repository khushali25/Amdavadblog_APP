package services;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.xps.amdavadblog_app.R;
import com.example.xps.amdavadblog_app.UnfoldableDetailsActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    String TAG = "MyFirebaseIDService";
    String getVALUE;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

//        Intent intent = new Intent();
//        if (intent.hasExtra("click_action")) {
//            handleFirebaseNotificationIntent(intent);
//        }


        if(remoteMessage.getData().size() > 0)
        {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

        for (Map.Entry<String, String> entry : remoteMessage.getData().entrySet()) {
            String getKEY = entry.getKey();
            getVALUE = entry.getValue();

            Log.d(TAG, "key, " + getKEY + " value " + getVALUE);
        }
        String postid = remoteMessage.getData().get("id");
        String postimage = remoteMessage.getData().get("image");
        String posttitle = remoteMessage.getData().get("title");
       // String clickAction = remoteMessage.getData().get("click_action");

        // String clickAction = remoteMessage.getNotification().getClickAction();
            SendNotification(postid,postimage,posttitle);
       }
  }

//    private void handleFirebaseNotificationIntent(Intent intent) {
//        String className = intent.getStringExtra("click_action");
//        startSelectedActivity(className, intent.getExtras());
//    }
//
//    private void startSelectedActivity(String className, Bundle extras) {
//        Class cls = null;
//        try {
//            cls = Class.forName(className);
//        }catch(ClassNotFoundException e){
//
//        }
//        Intent i = new Intent(this, cls);
//
//        if (i != null) {
//            i.putExtras(extras);
//            this.startActivity(i);
//        }
//
//    }

    public void SendNotification(String id, String img, String title)
    {
        //int questionId = Integer.parseInt(body.get("questionId").toString());
        int blogId = Integer.parseInt(id);
        Intent intent = new Intent(this, UnfoldableDetailsActivity.class);
        intent.putExtra("BlogId", blogId);
        intent.putExtra("Title",title);
        intent.putExtra("Image",img);
       // intent.putExtra("")
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

       // var defaultSoundUri = RingtoneManager.getDefaultUri(ring.Notification);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.icon256)
                .setContentTitle("Test Notification")
                .setContentText(title)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());
    }

}
