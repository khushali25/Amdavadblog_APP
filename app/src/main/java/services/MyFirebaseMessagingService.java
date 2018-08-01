package services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.xps.amdavadblog_app.R;
import com.example.xps.amdavadblog_app.UnfoldableDetailsActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    String TAG = "MyFirebaseIDService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

    //SendNotification(message.GetNotification().Body);

    String id = "";
        if(remoteMessage.getData().size() > 0)

    {
        Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        id = remoteMessage.getMessageId();
    }

      String clickAction = remoteMessage.getNotification().getClickAction();

        if(!(id==null) || id.trim().equals(""))
        {
            SendNotification(remoteMessage.getNotification().getBody(),remoteMessage.getNotification().getTitle());
        }
        else
        {
            SendNotification(remoteMessage.getNotification().getBody(), id, clickAction);

        }
  }

    public void SendNotification(String body, String id, String clickAction)
    {
        //int questionId = Integer.parseInt(body.get("questionId").toString());
        int blogId = Integer.parseInt(id);
        Intent intent = new Intent(this, UnfoldableDetailsActivity.class);
        intent.putExtra("BlogId", blogId);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

       // var defaultSoundUri = RingtoneManager.getDefaultUri(ring.Notification);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.icon256)
                .setContentTitle("Test Notification")
                .setContentText(body)
                .setAutoCancel(true)
               // .SetSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());
    }

    private void SendNotification(String body,String title)
    {
        Intent intent = new Intent(this, UnfoldableDetailsActivity.class);
        intent.putExtra("Title", title);
        intent.putExtra("Content",body);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

       // SoundPool defaultSoundUri = RingtoneManager.getDefaultUri(SoundPool.Notification);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.icon256)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
               // .SetSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());
    }
}
