package services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
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
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            for (Map.Entry<String, String> entry : remoteMessage.getData().entrySet()) {
                String getKEY = entry.getKey();
                getVALUE = entry.getValue();

                Log.d(TAG, "key, " + getKEY + " value " + getVALUE);
            }
            String postid = remoteMessage.getData().get("id");
            String postimage = remoteMessage.getData().get("image");
            String posttitle = remoteMessage.getData().get("title");

            SendNotification(postid, postimage, posttitle);
        }
    }
    public void SendNotification(String id, String img, String title) {
        int blogId = Integer.parseInt(id);

        Intent intent = new Intent(this, UnfoldableDetailsActivity.class);
        intent.putExtra("BlogId", blogId);
        intent.putExtra("Title", title);
        intent.putExtra("Image", img);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.O) {
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    //.setSmallIcon(android.R.drawable)
                    .setContentTitle("Test Notification")
                    .setContentText(title)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                    .setContentIntent(pendingIntent)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setPriority(Notification.PRIORITY_HIGH);

            PowerManager powerManager = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
            boolean isScreenOn;
            if (Build.VERSION.SDK_INT >= 20) {
                isScreenOn = powerManager.isInteractive();
            } else {
                isScreenOn = powerManager.isScreenOn();
            }
            if (!isScreenOn) {

                PowerManager.WakeLock wl = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, "MH24_SCREENLOCK");
                wl.acquire(2000);
                Settings.System.putString(this.getContentResolver(), Settings.System.NEXT_ALARM_FORMATTED, "Hello");
                PowerManager.WakeLock wl_cpu = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MH24_SCREENLOCK");
                wl_cpu.acquire(2000);
            }
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.notify(0, notificationBuilder.build());
        }
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationManager notificationManager1 =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            int notificationId = 1;
            String channelId = "channel-01";
            String channelName = "Channel Name";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, channelName, importance);
            mChannel.setShowBadge(true);
            mChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            assert notificationManager1 != null;
            notificationManager1.createNotificationChannel(mChannel);
            PendingIntent pendingIntent1 = PendingIntent.getActivity(this, 0, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, channelId)
                   // .setSmallIcon(R.drawable.icon)
                    .setContentTitle("New Blog")
                    .setContentText("Post Published")
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                    .setContentIntent(pendingIntent)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setPriority(Notification.PRIORITY_HIGH);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            stackBuilder.addNextIntent(intent);
            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
                    0,
                    PendingIntent.FLAG_UPDATE_CURRENT
            );
            mBuilder.setContentIntent(resultPendingIntent);
            notificationManager1.notify((int) System.currentTimeMillis(), mBuilder.build());
        }
    }
}



