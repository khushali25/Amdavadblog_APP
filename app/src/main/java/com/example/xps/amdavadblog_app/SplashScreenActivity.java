//package com.example.xps.amdavadblog_app;
//
//import android.Manifest;
//import android.content.Context;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.os.AsyncTask;
//import android.os.Build;
//import android.support.annotation.NonNull;
//import android.support.design.widget.Snackbar;
//import android.support.v4.app.ActivityCompat;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.Window;
//import android.view.WindowManager;
//import android.widget.Toast;
//
//import com.crashlytics.android.Crashlytics;
//
//import java.io.IOException;
//import java.util.List;
//
//import services.CacheService;
//
//public class SplashScreenActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {
//    private Boolean firstTime = null;
//    private static final int REQUEST = 112;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        try {
//            if (isOnline()) {
//                if (Build.VERSION.SDK_INT >= 23) {
//                    String[] PERMISSIONS = {android.Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
//
//                    if (!hasPermissions(this, PERMISSIONS)) {
//
//                        ActivityCompat.requestPermissions(this, PERMISSIONS, REQUEST);
//                    } else {
//                        //do here
//                        requestWindowFeature(Window.FEATURE_NO_TITLE);
//                        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                                WindowManager.LayoutParams.FLAG_FULLSCREEN);
//                        setContentView(R.layout.activity_splash_screen);
//                        new FetchStats().execute();
//                        CacheService.ClearAllCache();
//
//                        //   InitializeBackgroundWorker();
//                        //   backgroundworker1.RunWorkerAsync();
//                    }
//                }
//            }
//        }
////                    else
////                    {
////                        Toast.MakeText(this, "No internet connection found", ToastLength.Long).Show();
////
////                    }
//
////                    Thread background = new Thread() {
////                        public void run() {
////                            try {
////                                // Thread will sleep for 5 seconds
////                                sleep(2 * 1000);
////                                // After 5 seconds redirect to another intent
////                                Intent i = new Intent(getBaseContext(), Intro_Activity.class);
////                                //Remove activity
////                                finish();
////                                startActivity(i);
////                            } catch (Exception e) {
////                                Crashlytics.logException(e);
////                            }
////                        }
////                    };
////
////                    background.start();
//
////                }
////            }
////        }
//        catch (Exception ex) {
//            Crashlytics.logException(ex);
//        }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int i, @NonNull String[] strings, @NonNull int[] ints) {
//        // super.onRequestPermissionsResult(i, strings, ints);
//        try {
//            switch (i) {
//                case REQUEST: {
//                    if (ints.length > 0 && ints[0] == PackageManager.PERMISSION_GRANTED) {
//                        //do here
//                        requestWindowFeature(Window.FEATURE_NO_TITLE);
//                        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                                WindowManager.LayoutParams.FLAG_FULLSCREEN);
//                        setContentView(R.layout.activity_splash_screen);
//                        Thread background = new Thread() {
//                            public void run() {
//                                try {
//                                    // Thread will sleep for 5 seconds
//                                    sleep(2 * 1000);
//                                    // After 5 seconds redirect to another intent
//                                    Intent i = new Intent(getBaseContext(), Intro_Activity.class);
//                                    //Remove activity
//                                    finish();
//                                    startActivity(i);
//                                } catch (Exception e) {
//                                    Crashlytics.logException(e);
//                                }
//                            }
//                        };
//
//                        background.start();
//                        // Toast.makeText(this,"Permission Granted, Now you can access SMS.",Snackbar.LENGTH_LONG).show();
//                    } else {
//                        Toast.makeText(this, "The app was not allowed to write in your storage", Toast.LENGTH_LONG).show();
//                    }
//                }
//            }
//        } catch (Exception ex) {
//            Crashlytics.logException(ex);
//        }
//
//    }
//
//    private boolean hasPermissions(SplashScreenActivity splashScreenActivity, String[] permissions) {
//        try {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && splashScreenActivity != null && permissions != null) {
//                for (String permission : permissions) {
//                    //   Activity activity = (Activity);
//                    if (ActivityCompat.checkSelfPermission(splashScreenActivity, permission) != PackageManager.PERMISSION_GRANTED) {
//                        return false;
//                    }
//                }
//            }
//        } catch (Exception ex) {
//            Crashlytics.logException(ex);
//        }
//        return true;
//    }
//
//
////    private class FetchStats extends AsyncTask<String,String,String> {
////        @Override
////        protected String doInBackground(String... strings) {
////            try {
////                List AllPost = CacheService.GetAllPostnew(true,1);
////            } catch (IOException e) {
////                Crashlytics.logException(e);
////                e.printStackTrace();
////            }
////            return null;
////        }
////
////        @Override
////        protected void onPostExecute(String s) {
////            super.onPostExecute(s);
////            //Toast.MakeText(this, "Welcome to Amdavad Blogs", ToastLength.Long).Show();
////            try
////            {
////                //Moving to next activity
////                StartActivity(typeof(MainActvity));
////            }
////            catch (Exception exx)
////            {
////                Android.Util.Log.Error("Initialize UI failed", exx.Message);
////            }
////        }
////
////        @Override
////        protected void onPreExecute() {
////            super.onPreExecute();
////        }
////    }
//}
//
package com.example.xps.amdavadblog_app;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import services.CacheService;

public class SplashScreenActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {
    private Boolean firstTime = null;
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
//                    GetPosts();
//                    Thread background = new Thread() {
//                        public void run() {
//                            try {
//                                // Thread will sleep for 5 seconds
//                                sleep(2 * 1000);
//                                // After 5 seconds redirect to another intent
//                                Intent i = new Intent(getBaseContext(), Intro_Activity.class);
//                                //Remove activity
//                                finish();
//                                startActivity(i);
//                            } catch (Exception e) {
//                                Crashlytics.logException(e);
//                            }
//                        }
//                    };
//
//                    background.start();

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
        // super.onRequestPermissionsResult(i, strings, ints);
        try {
            switch (i) {
                case REQUEST: {
                    if (ints.length > 0 && ints[0] == PackageManager.PERMISSION_GRANTED) {
                        //do here
                        //requestWindowFeature(Window.FEATURE_NO_TITLE);
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
                                    //GetPosts();
                                } catch (Exception e) {
                                    Crashlytics.logException(e);
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

    private  void GetPosts()
    {
        try {
           List AllPost = CacheService.GetAllPostnew(false, 1);
        } catch (FileNotFoundException e) {
            Crashlytics.logException(e);
            e.printStackTrace();
        } catch (IOException e) {
            Crashlytics.logException(e);
            e.printStackTrace();
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
            //GetPosts();
            setContentView(R.layout.activity_splash_screen);
            Thread background = new Thread() {
                public void run() {
                    try {
                        // Thread will sleep for 5 seconds
                        sleep(2 * 1000);
                        Intent intent = new Intent(getBaseContext(), MainNavigationActivity.class);
                        startActivity(intent);
                        finish();
                        // After 5 seconds redirect to another intent
                    } catch (Exception e) {
                        Crashlytics.logException(e);
                    }
                }
            };
            background.start();

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
}
