package com.example.xps.amdavadblog_app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
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
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

import Helper.PrefService;
import Helper.SocialMethod;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainNavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView txt,fbname,fbemail;
    CircleImageView fbphoto;
    String appTitle;
    LoginButton fbloginbutton;
    TextView info;
    CallbackManager callbackManager;
    String firstName,lastName,email,birthday,gender,emailtostore,nametostore;
    private String fbname1,fbsurname1,fbemail1;
    String imguri1;
    URL profilePicture;
    String userId,email1,personname,gen,add,dob;
    ProgressDialog mProgressDialog;
    Intent main;
     View headerView;
    Bitmap img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.newtoolbar);

        setSupportActionBar(toolbar);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        headerView = navigationView.getHeaderView(0);
        fbname = (TextView)headerView.findViewById(R.id.fbname);
        fbemail = (TextView)headerView.findViewById(R.id.fbemail);
        fbphoto = (CircleImageView) headerView.findViewById(R.id.circleView);
        InitializeFirstScreenUI();
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        PrefService ap = new PrefService(getApplicationContext());
        String name = ap.getAccessKey("Username");
        String emailid = ap.getAccessKey("Password");
        String photo = ap.getAccessKey("loginkey");

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken == null) {
            fbname.setVisibility(View.GONE);
            fbemail.setVisibility(View.GONE);
            fbphoto.setVisibility(View.GONE);
        //Log.d(TAG, ">>>" + "Signed Out");
        } else {
            fbname.setVisibility(View.VISIBLE);
            fbemail.setVisibility(View.VISIBLE);
            fbphoto.setVisibility(View.VISIBLE);
            fbname.setText(name);
            fbemail.setText(emailid);
            getBitmapFromURL(photo);
            fbphoto.setImageURI(null);
            fbphoto.setImageBitmap(img);
        }

        initfacebooklogin();
    }

    private void initfacebooklogin() {
        callbackManager = CallbackManager.Factory.create();
        fbloginbutton = (LoginButton)headerView.findViewById(R.id.login_button);
        fbloginbutton.setReadPermissions("email","user_birthday","user_posts");

        fbloginbutton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                String accessToken = loginResult.getAccessToken().getToken();
               // fbloginbutton.setVisibility(View.GONE);
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(final JSONObject object, GraphResponse response) {
                        try {
                            userId = object.getString("id");
                            profilePicture = new URL("https://graph.facebook.com/" + userId + "/picture?width=500&height=500");
                            String imgfb = profilePicture.toString();

                               fbname.setVisibility(View.VISIBLE);
                               fbemail.setVisibility(View.VISIBLE);
                               fbphoto.setVisibility(View.VISIBLE);

                            firstName = object.getString("first_name");
                            lastName = object.getString( "last_name");
                            nametostore = firstName + lastName;
                            fbname.setText(firstName + " " + lastName);
                            if (object.has("email")) {
                                emailtostore = object.getString("email");
                                fbemail.setText(object.getString("email"));
                            }
                            else
                                fbemail.setVisibility(View.GONE);
                            if(imgfb == null)
                            {
                                img = BitmapFactory.decodeResource(getResources(),
                                        R.drawable.unfold_glance);
                                fbphoto.setImageBitmap(img);
                            }
                            else {
                                getBitmapFromURL(imgfb);
                                fbphoto.setImageURI(null);
                                fbphoto.setImageBitmap(img);
                            }
                            PrefService ap = new PrefService(getApplicationContext());
                            ap.saveAccessKey("name",nametostore);
                            ap.saveAccessKey("email",emailtostore);
                            ap.saveAccessKey("photo",imgfb);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                    }
                });
                Bundle bundle = new Bundle();
                bundle.putString("fields","id,picture,first_name,last_name,email,birthday,gender");
                request.setParameters(bundle);
                request.executeAsync();
            }
            @Override
            public void onCancel() {
             //   info.setText("Login attemp canceled");
            }
            @Override
            public void onError(FacebookException error) {

              //  info.setText("Login attempt falied");
            }
        });
        AccessTokenTracker tracker1 = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                if (currentAccessToken == null) {
                    Log.d("FB", "User Logged Out.");
                    fbname.setVisibility(View.GONE);
                    fbemail.setVisibility(View.GONE);
                    fbphoto.setVisibility(View.GONE);
                }
            }
        };
        tracker1.startTracking();
    }


    public Bitmap getBitmapFromURL(String src)
    {
        try {
            URL url = new URL(src);
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
    public void onResume() {
        super.onResume();
    }
    private void InitializeFirstScreenUI() {
        try
        {
             txt = findViewById(R.id.toolbartxt);
             this.appTitle = (String) this.getTitle();
            txt.setText(appTitle);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getStatusBarHeight();
            android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, new FoldableListFragment(), "Fragment1");
            ft.addToBackStack("AddFragment1");
            ft.commit();
        }
        catch (Exception ex)
        {
            android.util.Log.e("Initialize UI failed", ex.getMessage());
        }
    }
    private int getStatusBarHeight() {
        int result = 0;
        int resourceId = MainNavigationActivity.this.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0)
        {
            result = MainNavigationActivity.this.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout)findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
//        if (this.drawerToggle.OnOptionsItemSelected(item))
//            return true;
        int id = item.getItemId();

        switch (id) {
            case R.id.searchitem:
                Intent intent = new Intent(this,SearchActivity.class);
                startActivity(intent);
                return true;

            case R.id.subscribepost:
                //android.content.Context mContext = android.app.Application.Context;
                Services.PrefService ap = new Services.PrefService(getApplicationContext());
                //String subscribed = ap.getAccessKey(AppConstants.EmailKey);
               // if (subscribed == "") {
                    SocialMethod.showSubscription(this);
               // } else {
                  //  SocialMethod.alreadySubscribed(this);
               // }
                return true;
            case R.id.feedbackpost:
                SocialMethod.showFeedback(this);
                return true;
            case R.id.rateus:
                SocialMethod.showRateApp(this);
                return true;
            case R.id.shareApp:
                SocialMethod.shareIt(this);
                return true;
            case R.id.settings:
                Intent i = new Intent(this,SettingsActivity.class);
                startActivity(i);
                return true;

            //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
          }

            return super.onOptionsItemSelected(item);
        }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.home) {
            // Handle the camera action
        } else if (id == R.id.flavor) {

        } else if (id == R.id.explore) {

        } else if (id == R.id.news) {

        } else if (id == R.id.thingstodo) {

        } else if (id == R.id.nav_send) {
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



}
