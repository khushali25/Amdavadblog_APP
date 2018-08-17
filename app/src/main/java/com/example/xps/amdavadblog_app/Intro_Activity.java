package com.example.xps.amdavadblog_app;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import Adapter.IntroScreenAdapter;
import Helper.IntroPageTransformer;
import Helper.PrefService;

public class Intro_Activity extends AppCompatActivity  {
    private ViewPager mViewPager;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private int[] layouts;
    private Button btnSkip, btnNext;
    String gender,add,firstName,lastName,userId;
    Bundle bundle = new Bundle();
    TextView info;
    CallbackManager callbackManager;
    RelativeLayout relativeLayout;
    Snackbar snackbar;
    URL profilePicture;
    Bitmap img;
    PrefService prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PreferenceManager.setDefaultValues(this, R.xml.pref_general, false);
        Boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getBoolean("isFirstRun", true);

        if (isFirstRun) {
            //show sign up activity
            startActivity(new Intent(Intro_Activity.this, MainNavigationActivity.class));
            Toast.makeText(Intro_Activity.this, "Run only once", Toast.LENGTH_LONG)
                    .show();
        }

        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
                .putBoolean("isFirstRun", false).commit();

//        prefManager = new PrefService(this);
//        if (!prefManager.isFirstTimeLaunch()) {
//            launchHomeScreen();
//            finish();
//        }
        facebookSDKInitialize();
        setContentView(R.layout.activity_intro);
        relativeLayout = (RelativeLayout) findViewById(R.id
                .relativelayout);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);
        btnSkip = (Button) findViewById(R.id.btn_skip);
        btnNext = (Button) findViewById(R.id.btn_next);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        // Set an Adapter on the ViewPager
        mViewPager.setAdapter(new IntroScreenAdapter(getSupportFragmentManager()));

        // Set a PageTransformer
        mViewPager.setPageTransformer(false, new IntroPageTransformer());
      //  initfacebooklogin();
        layouts = new int[]{
                R.layout.introscreenfragmentlayout_1,
                R.layout.introscreenfragmentlayout_2,
                R.layout.introscreenfragmentlayout_3,
                };

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isNetworkConnected())
                {
                    snackbarerror();
                }
                else
                launchHomeScreen();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // checking for last page
                // if last page home screen will be launched
                if (!isNetworkConnected())
                {
                    snackbarerror();
                }
                else {
                    int current = getItem(+1);
                    if (current < layouts.length) {
                        // move to next screen
                        mViewPager.setCurrentItem(current);
                    } else {
                        launchHomeScreen();
                    }
                }
            }
        });

        if (isNetworkConnected()) {

            LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
            loginButton.setTextSize(16);
            loginButton.setReadPermissions("email");
            getLoginDetails(loginButton);

        } else {
            snackbarerror();
        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    private void launchHomeScreen() {
       //  prefManager.setFirstTimeLaunch(false);
        startActivity(new Intent(this, MainNavigationActivity.class));
        finish();
    }
    private int getItem(int i) {
        return mViewPager.getCurrentItem() + i;
    }

    protected void facebookSDKInitialize() {
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
    }
/*
Register a callback function with LoginButton to respond to the login result.
*/
    protected void getLoginDetails(final LoginButton login_button){

        // Callback registration
        login_button.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult login_result) {

                getUserInfo(login_result);
                login_button.setVisibility(View.GONE);
            }

            @Override
            public void onCancel() {
                System.out.println("onCancel");
            }

            @Override
            public void onError(FacebookException exception) {
                System.out.println("onError");
                Log.v("LoginActivity", exception.getCause().toString());
            }
        });
    }
    protected void getUserInfo(LoginResult login_result){

        GraphRequest data_request = GraphRequest.newMeRequest(
                login_result.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback(){
                    @Override
                    public void onCompleted(
                            JSONObject json_object,
                            GraphResponse response) {
                        try {
                            userId = json_object.getString("id");
                            profilePicture = new URL("https://graph.facebook.com/" + userId + "/picture?width=500&height=500");
                            String imgfb = profilePicture.toString();

                           String name = json_object.getString("name");
                           // lastName = json_object.getString("last_name");
                           // String name = firstName + lastName;

                            final String email =json_object.getString("email");

                            if(imgfb == null)
                            {
                                img = BitmapFactory.decodeResource(getResources(),
                                        R.drawable.unfold_glance);
                            }
                            else {
                                getBitmapFromURL(imgfb);
                            }

//                            bundle.putString("userName",name);
//                            bundle.putString("userEmail",email);
//                            bundle.putString("userimg",imgfb);

                            PrefService ap = new PrefService(getApplicationContext());
                            ap.saveAccessKey("Username", name);
                            ap.saveAccessKey("Password", email);
                            ap.saveAccessKey("loginkey", imgfb);
                            ap.saveAccessKey("textofbtn", "Logout");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                        Intent intent = new Intent(Intro_Activity.this,MainNavigationActivity.class);
                        intent.putExtras(bundle);
                        intent.putExtra("jsondata",json_object.toString());
                        startActivity(intent);
                        finish();
                    }
                });
        Bundle permission_param = new Bundle();
        permission_param.putString("fields", "id,name,email,picture.width(120).height(120)");
        data_request.setParameters(permission_param);
        data_request.executeAsync();
    }

    private Bitmap getBitmapFromURL(String imgfb) {
        try {
            URL url = new URL(imgfb);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            BufferedInputStream buf = new BufferedInputStream(input, 1024);
            img = BitmapFactory.decodeStream(buf);
            return img;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void snackbarerror()
    {
       snackbar = Snackbar
                .make(relativeLayout,"No internet connection!", Snackbar.LENGTH_INDEFINITE)
                .setAction("RETRY", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(isNetworkConnected())
                        snackbar.dismiss();
                        else
                            snackbarerror();
                    }
                });
        snackbar.setActionTextColor(Color.RED);

        // Changing action button text color
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);
        snackbar.show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        Log.e("data", data.toString());
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }

}