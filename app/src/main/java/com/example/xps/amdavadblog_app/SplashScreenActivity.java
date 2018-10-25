package com.example.xps.amdavadblog_app;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;
import com.crashlytics.android.Crashlytics;

public class SplashScreenActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {
    private static final int REQUEST = 112;
    final String PREFS_NAME = "MyPrefsFile";
    final String PREFS_FIRST_RUN = "first_run";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        try {
            if (Build.VERSION.SDK_INT >= 23) {
                String[] PERMISSIONS = {android.Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};

                if (!hasPermissions(this, PERMISSIONS)) {

                    ActivityCompat.requestPermissions(this, PERMISSIONS, REQUEST);
                } else {
                    //do here
                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                            WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    setContentView(R.layout.activity_splash_screen);
                    checkFirstRun();
                }
            }
        }
        catch(Exception ex)
        {
            Crashlytics.logException(ex);
        }
    }
    @Override
    public void onRequestPermissionsResult(int i, @NonNull String[] strings, @NonNull int[] ints) {
        super.onRequestPermissionsResult(i, strings, ints);
        try {
            switch (i) {
                case REQUEST: {
                    if (ints.length > 0 && ints[0] == PackageManager.PERMISSION_GRANTED) {
                        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);
                        setContentView(R.layout.activity_splash_screen);

                        Thread background = new Thread() {
                            public void run() {
                                try {
                                    // Thread will sleep for 2 seconds
                                    sleep(2 * 1000);
                                    // After 2 seconds redirect to another intent
                                    Intent i = new Intent(getBaseContext(), Intro_Activity.class);
                                    //Remove activity
                                    finish();
                                    startActivity(i);

                                } catch (Exception e) {
                                    Crashlytics.logException(e);
                                }
                            }
                        };

                        background.start();
                    } else {
                        Toast.makeText(this, "The app was not allowed to write in your storage", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
        catch(Exception ex)
        {
            Crashlytics.logException(ex);
        }

    }
    private void checkFirstRun() {
        try
        {
            SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
            // If the app is launched for first time, view splash screen and setup 'Next >>' link.
            if (sharedPreferences.getBoolean(PREFS_FIRST_RUN, true)) {
                // Record that user have done first run.
                sharedPreferences.edit().putBoolean(PREFS_FIRST_RUN, false).apply();
                gotoIntroActivity();
            } else {
                goToMainActivity();
            }
        }
        catch(Exception ex)
        {
            Crashlytics.logException(ex);
        }
    }
    private void gotoIntroActivity() {
        try {
            Intent intent = new Intent(this, Intro_Activity.class);
            startActivity(intent);
            finish();
        }
        catch(Exception ex)
        {
            Crashlytics.logException(ex);
        }
    }

    private void goToMainActivity() {
        try {
            new LoadViewTask().execute("");
        }
        catch(Exception ex)
        {
            Crashlytics.logException(ex);
        }
    }
    private boolean hasPermissions(SplashScreenActivity splashScreenActivity, String[] permissions) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && splashScreenActivity != null && permissions != null) {
                for (String permission : permissions) {
                    //   Activity activity = (Activity);
                    if (ActivityCompat.checkSelfPermission(splashScreenActivity, permission) != PackageManager.PERMISSION_GRANTED) {
                        return false;
                    }
                }
            }
        }
        catch(Exception ex)
        {
            Crashlytics.logException(ex);
        }
        return true;
    }
    private class LoadViewTask extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... strings) {
            for (int i = 0; i < 2; i++) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.interrupted();
                }
            }
            return "whatever result you have";
        }
        @Override
        protected void onPostExecute(String result) {
            Intent i = new Intent(SplashScreenActivity.this, MainNavigationActivity.class);
           // i.putExtra("data", result);
            startActivity(i);
            finish();
        }
        @Override
        protected void onPreExecute()
        {

        }
    }
}
