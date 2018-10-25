package services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;
import com.crashlytics.android.Crashlytics;

import static com.facebook.FacebookSdk.getApplicationContext;

public class UninstallIntentReceiver extends BroadcastReceiver {
    Snackbar snackbar;
    @Override
    public void onReceive(Context context, Intent intent) {
        // fetching package names from extras
        try {
            if(isNetworkConnected()) {
                String[] packageNames = intent.getStringArrayExtra("android.intent.extra.PACKAGES");
                if (packageNames != null) {
                    for (String packageName : packageNames) {
                        if (packageName != null && packageName.equals("YOUR_APPLICATION_PACKAGE_NAME")) {
                            // User has selected our application under the Manage Apps settings
                            // now initiating background thread to watch for activity
                            new ListenActivities(context).start();

                        }
                    }
                }
            }
            else
            {snackbarerror();}
        } catch (Exception ex) {
            Crashlytics.logException(ex);
        }

    }
    private boolean isNetworkConnected() {
        ConnectivityManager cm = null;
        try {
            cm = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        }
        catch(Exception ex)
        {
            Crashlytics.logException(ex);
        }
        return cm.getActiveNetworkInfo() != null;
    }
    public void snackbarerror()
    {
        try {
            View snackbarView = snackbar.getView();
            // Context context = SocialMethod.getAppContext();
            snackbar = Snackbar
                    .make(snackbarView, "No internet connection!", Snackbar.LENGTH_INDEFINITE)
                    .setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (isNetworkConnected())
                                snackbar.dismiss();
                            else
                                snackbarerror();
                        }
                    });
            snackbar.setActionTextColor(Color.RED);
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.YELLOW);
            snackbar.show();
        }
        catch(Exception ex)
        {
            Crashlytics.logException(ex);
        }
    }
}