package services;

import android.annotation.SuppressLint;
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
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import com.crashlytics.android.Crashlytics;
import com.example.xps.amdavadblog_app.NotificationClickActivity;
import com.example.xps.amdavadblog_app.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    String TAG = "MyFirebaseIDService";
    String getVALUE;
    private static final int WRITE_PERMISSION_REQUEST = 5000;
    Snackbar snackbar;
    static Context context;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        try {
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
        }catch (Exception ex) {
            Crashlytics.logException(ex);
        }
    }
    public static Context getContext() {
        return context;
    }

    @SuppressLint("NewApi")
    public void SendNotification(String id, String img, String title) {
        try {
                context = getApplicationContext();
                int blogId = Integer.parseInt(id);
                Intent intent = new Intent(this, NotificationClickActivity.class);
                intent.putExtra("BlogId", blogId);
                intent.putExtra("Title", title);
                intent.putExtra("Image", img);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

                    NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                            .setSmallIcon(R.drawable.ic_launcher)
                            .setContentTitle(title)
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
                            .setSmallIcon(R.drawable.ic_stat_name)
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
        }catch (Exception ex) {
            Crashlytics.logException(ex);
        }
    }
}