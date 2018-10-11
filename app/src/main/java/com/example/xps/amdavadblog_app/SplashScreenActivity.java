package com.example.xps.amdavadblog_app;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

public class SplashScreenActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {
    private Boolean firstTime = null;
    private static final int REQUEST = 112;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 23) {
            String[] PERMISSIONS = {android.Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};

            if (!hasPermissions(this, PERMISSIONS)) {

                ActivityCompat.requestPermissions(this, PERMISSIONS, REQUEST);
            } else {
                //do here
                requestWindowFeature(Window.FEATURE_NO_TITLE);
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                        WindowManager.LayoutParams.FLAG_FULLSCREEN);
                setContentView(R.layout.activity_splash_screen);
                Thread background = new Thread() {
                    public void run() {
                        try {
                            // Thread will sleep for 5 seconds
                            sleep(2 * 1000);
                            // After 5 seconds redirect to another intent
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

            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int i, @NonNull String[] strings, @NonNull int[] ints) {
        // super.onRequestPermissionsResult(i, strings, ints);
        switch (i) {
            case REQUEST: {
                if (ints.length > 0 && ints[0] == PackageManager.PERMISSION_GRANTED) {
                    //do here
                    requestWindowFeature(Window.FEATURE_NO_TITLE);
                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                            WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    setContentView(R.layout.activity_splash_screen);
                    Thread background = new Thread() {
                        public void run() {
                            try {
                                // Thread will sleep for 5 seconds
                                sleep(2 * 1000);
                                // After 5 seconds redirect to another intent
                                Intent i = new Intent(getBaseContext(), Intro_Activity.class);
                                //Remove activity
                                finish();
                                startActivity(i);
                            } catch (Exception e) {

                            }
                        }
                    };

                    background.start();
                   // Toast.makeText(this,"Permission Granted, Now you can access SMS.",Snackbar.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "The app was not allowed to write in your storage", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
    private boolean hasPermissions(SplashScreenActivity splashScreenActivity, String[] permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && splashScreenActivity != null && permissions != null) {
            for (String permission : permissions) {
                //   Activity activity = (Activity);
                if (ActivityCompat.checkSelfPermission(splashScreenActivity, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
        }
    }

