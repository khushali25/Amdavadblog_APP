package com.example.xps.amdavadblog_app;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.JsonObject;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import Core.Helper.SynchronousCallAdapterFactory;
import Core.Helper.ApiService;
import Helper.PrefService;
import Helper.SocialMethod;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import static android.provider.ContactsContract.Intents.Insert.EMAIL;

public class MainNavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static String FACEBOOK_URL = "https://www.facebook.com/amdavadblog";
    public static String FACEBOOK_PAGE_ID = "2012829695602790";
    TextView txt,fbname,fbemail;
    ImageView fbicon,twittericon,instaicon,googleplusicon,websiteicon;
    Button fblogout;
    CircleImageView fbphoto;
    LoginButton fbloginbutton;
    TextView info;
    CallbackManager callbackManager;
    String firstName,lastName,emailtostore,nametostore,link;
    URL profilePicture;
    String userId,add;
    View headerView;
    Bitmap img;
    NavigationView navigationView;
    Fragment fragmentCurrent;
    DrawerLayout drawer;
    private int checkedItem = 0;
    private boolean navItemSelected = false;
    private Fragment fragment;
    ActionBarDrawerToggle toggle;
    Snackbar snackbar;
    private boolean isDrawerOpen = false;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);

                FacebookSdk.sdkInitialize(getApplicationContext());

                setContentView(R.layout.activity_main_navigation);

                callbackManager = CallbackManager.Factory.create();

                Toolbar toolbar = (Toolbar) findViewById(R.id.newtoolbar);
                setSupportActionBar(toolbar);
                getSupportActionBar().setDisplayShowTitleEnabled(false);
                InitiaalizeGoogleAppConfig();
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);

                drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

                toggle = new ActionBarDrawerToggle(
                        this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

                drawer.addDrawerListener(toggle);
                toggle.syncState();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                            WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                }

                navigationView = (NavigationView) findViewById(R.id.nav_view);
                navigationView.setNavigationItemSelectedListener(this);

                headerView = navigationView.getHeaderView(0);
                fbname = (TextView) headerView.findViewById(R.id.fbname);
                fbemail = (TextView) headerView.findViewById(R.id.fbemail);
                fbphoto = (CircleImageView) headerView.findViewById(R.id.circleView);
                fbloginbutton = (LoginButton) headerView.findViewById(R.id.login_button);
                fblogout = (Button) headerView.findViewById(R.id.fblogout);

                getloginstatus();

                fbicon = (ImageView) findViewById(R.id.fbicon);
                twittericon = (ImageView) findViewById(R.id.twittericon);
                instaicon = (ImageView) findViewById(R.id.instaicon);
                googleplusicon = (ImageView) findViewById(R.id.googleplusicon);
                websiteicon = (ImageView) findViewById(R.id.websiteicon);
                fbicon.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                final Animation myAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bounce);
                                fbicon.startAnimation(myAnim);
                                if(isNetworkConnected()) {
                                    Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
                                    String facebookUrl = getFacebookPageURL(this);
                                    facebookIntent.setData(Uri.parse(facebookUrl));
                                    startActivity(facebookIntent);
                                }
                                else {snackbarerror();}
                            }
                        });
                twittericon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = null;
                        final Animation myAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bounce);
                        twittericon.startAnimation(myAnim);
                        try {
                            if(isNetworkConnected()) {
                                // get the Twitter app if possible
                                getPackageManager().getPackageInfo("com.twitter.android", 0);
                                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/amdavadblog"));
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                            else
                            {snackbarerror();}
                        } catch (Exception e) {
                            // no Twitter app, revert to browser
                            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/amdavadblog"));
                            startActivity(intent);
                        }
                    }
                });
                instaicon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final Animation myAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bounce);
                        instaicon.startAnimation(myAnim);
                        if(isNetworkConnected()){
                        Uri uri = Uri.parse("http://instagram.com/_u/amdavadblog");
                        Intent i = new Intent(Intent.ACTION_VIEW, uri);
                        i.setPackage("com.instagram.android");
                        try {
                            startActivity(i);
                        } catch (ActivityNotFoundException e) {
                            startActivity(new Intent(Intent.ACTION_VIEW,
                                    Uri.parse("http://instagram.com/amdavadblog")));
                        }}
                        else
                        {snackbarerror();}
                    }
                });
                googleplusicon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final Animation myAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bounce);
                        googleplusicon.startAnimation(myAnim);
                        if(isNetworkConnected())
                        newGooglePlusIntent("116682005972414924881");
                        else
                        {snackbarerror();}
                    }
                });
                websiteicon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final Animation myAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bounce);
                        websiteicon.startAnimation(myAnim);
                        if(isNetworkConnected())
                        startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("https://amdavadblog.com/")));
                        else
                            snackbarerror();
                    }
                });
                fblogout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final Animation myAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bounce);
                        fblogout.startAnimation(myAnim);
                        LoginManager.getInstance().logOut();
                        Intent intent1 = new Intent(MainNavigationActivity.this, MainNavigationActivity.class);
                        startActivity(intent1);
                        fbname.setVisibility(View.GONE);
                        fbemail.setVisibility(View.GONE);
                        fbphoto.setVisibility(View.GONE);
                        fbloginbutton.setVisibility(View.VISIBLE);
                        fblogout.setVisibility(View.GONE);
                    }
                });
                AppEventsLogger.activateApp(this);

                InitializeFirstScreenUI();

                checkedItem = R.id.home;
                fragment = new FoldableListFragment(100);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, fragment)
                        .commit();
                navigationView.setCheckedItem(R.id.home);

        }
        catch(Exception ex)
        {
            Crashlytics.logException(ex);
        }

    }

    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        try
        {
            toggle.syncState();
        }
        catch (Exception ex)
        {
           Crashlytics.logException(ex);
        }
    }

    @Override
    protected void onPause() {
        try {
            super.onPause();
        }
        catch (Exception ex)
        {
            Crashlytics.logException(ex);
        }
    }

    @Override
    protected void onStop() {
        try {
            super.onStop();
        }
        catch (Exception ex)
        {
            Crashlytics.logException(ex);
        }
    }

    @Override
    protected void onDestroy() {
        try {
            super.onDestroy();
        }
        catch (Exception ex)
        {
            Crashlytics.logException(ex);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        this.toggle.onConfigurationChanged(newConfig);
    }

    private void getloginstatus() {
        try {
            if(isNetworkConnected()) {
                PrefService ap = new PrefService(this);
                String name = ap.getAccessKey("Username");
                String emailid = ap.getAccessKey("Password");
                String photo = ap.getAccessKey("loginkey");
                String tetxtofloginbtn = ap.getAccessKey("textofbtn");

                AccessToken accessToken = AccessToken.getCurrentAccessToken();
                if (accessToken == null) {
                    fbname.setVisibility(View.GONE);
                    fbemail.setVisibility(View.GONE);
                    fbphoto.setVisibility(View.GONE);
                    fbloginbutton.setVisibility(View.VISIBLE);
                    fblogout.setVisibility(View.GONE);

                } else {
                    fbname.setVisibility(View.VISIBLE);
                    fbemail.setVisibility(View.VISIBLE);
                    fbphoto.setVisibility(View.VISIBLE);
                    fbname.setText(name);
                    fbemail.setText(emailid);
                    getBitmapFromURL(photo);
                    fbphoto.setImageURI(null);
                    fbphoto.setImageBitmap(img);
                    fbloginbutton.setVisibility(View.GONE);
                    fblogout.setVisibility(View.VISIBLE);
                }
            }
            else
            {snackbarerror();}
        } catch(Exception ex)
        {
            Crashlytics.logException(ex);
        }
    }

    private String getFacebookPageURL(View.OnClickListener onClickListener) {
        PackageManager packageManager = this.getPackageManager();
        try {
            int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850) { //newer versions of fb app
//                return "fb://facewebmodal/f?href=" + FACEBOOK_URL;
                return "fb://page/" + FACEBOOK_PAGE_ID;
            } else { //older versions of fb app
                return "fb://page/" + FACEBOOK_PAGE_ID;
            }
        } catch (PackageManager.NameNotFoundException e) {
            Crashlytics.logException(e);
            return FACEBOOK_URL; //normal web url
        }
    }
    private void newGooglePlusIntent(String s) {
        try {
            if(isNetworkConnected()) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setClassName("com.google.android.apps.plus",
                        "com.google.android.apps.plus.phone.UrlGatewayActivity");
                intent.putExtra("customAppUri", s);
                startActivity(intent);
            }
            else
            {snackbarerror();}
        } catch(ActivityNotFoundException e) {
            Crashlytics.logException(e);
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://plus.google.com/"+s+"/posts")));
        }
    }
    private void InitiaalizeGoogleAppConfig()
    {
      Runnable Task = new Runnable() {
          @Override
          public void run() {
              FirebaseInstanceId instanceId = FirebaseInstanceId.getInstance();
              try {
                  instanceId.deleteInstanceId();
              } catch (IOException e) {
                  Crashlytics.logException(e);
                  e.printStackTrace();
              }
          }
      };
    }
    private int GetRandomIndex() {
        int min = 0;
        int max = 15000;
        Random rand = new Random();
        return min + rand.nextInt((max - min) + 1);
    }
    private void initfacebooklogin() {
        if(isNetworkConnected()) {
            fbloginbutton = (LoginButton) headerView.findViewById(R.id.login_button);
            List<String> permissionNeeds = Arrays.asList("user_photos", "user_birthday", "user_gender", "user_link", "user_location", "user_posts", "public_profile", EMAIL);
            fbloginbutton.setReadPermissions(permissionNeeds);
            fbloginbutton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    String accessToken = loginResult.getAccessToken().getToken();
                    GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(final JSONObject object, GraphResponse response) {
                            String fbuserdata = object.toString();
                            try {
                                userId = object.getString("id");

                                firstName = object.getString("first_name");
                                lastName = object.getString("last_name");
                                profilePicture = new URL("https://graph.facebook.com/" + userId + "/picture?width=500&height=500");
                                String imgfb = profilePicture.toString();

                                nametostore = firstName + lastName;
                                fbname.setText(firstName + " " + lastName);

                                if (object.has("email")) {
                                    emailtostore = object.getString("email");
                                    fbemail.setText(emailtostore);
                                } else
                                    fbemail.setVisibility(View.GONE);

                                if (imgfb == null) {
                                    img = BitmapFactory.decodeResource(getResources(),
                                            R.drawable.ic_home_black_24dp);
                                    fbphoto.setImageBitmap(img);
                                } else {
                                    getBitmapFromURL(imgfb);
                                    fbphoto.setImageURI(null);
                                    fbphoto.setImageBitmap(img);
                                }
                                PrefService ap = new PrefService(getApplicationContext());
                                ap.saveAccessKey("Username", nametostore);
                                ap.saveAccessKey("Password", emailtostore);
                                ap.saveAccessKey("loginkey", imgfb);
                                ap.saveAccessKey("textofbtn", "Logout");

                            } catch (JSONException e) {
                                Crashlytics.logException(e);
                                e.printStackTrace();
                            } catch (MalformedURLException e) {
                                Crashlytics.logException(e);
                                e.printStackTrace();
                            }
                            try {
                                Retrofit retrofitallpost = new Retrofit.Builder()
                                        .baseUrl("http://api.amdavadblog.com/amdblog/")
                                        .addConverterFactory(GsonConverterFactory.create())
                                        .addCallAdapterFactory(SynchronousCallAdapterFactory.create())
                                        .build();
                                final ApiService apiService = retrofitallpost.create(ApiService.class);
                                Call<JsonObject> call = apiService.saveFbUserData(fbuserdata);
                                call.enqueue(new Callback<JsonObject>() {
                                    @Override
                                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                        JsonObject object = response.body();
                                    }

                                    @Override
                                    public void onFailure(Call<JsonObject> call, Throwable t) {
                                        Crashlytics.logException(t);

                                    }
                                });
                            } catch (Exception ex) {
                                Crashlytics.logException(ex);
                            }
                        }
                    });
                    Bundle bundle = new Bundle();
                    bundle.putString("fields", "id,first_name,last_name,birthday,gender,email,about,work,website,location,link,education,age_range,languages,name,hometown,picture{url},installed,interested_in");
                    request.setParameters(bundle);
                    request.executeAsync();
                    fbloginbutton.setVisibility(View.GONE);
                    fblogout.setVisibility(View.VISIBLE);
                }

                @Override
                public void onCancel() {
                    System.out.println("onCancel");
                }

                @Override
                public void onError(FacebookException error) {
                    System.out.println("onError");
                    Crashlytics.logException(error);
                    Log.v("LoginActivity", error.getCause().toString());
                }
            });
        }
        else
        {snackbarerror();}
        AccessTokenTracker tracker1 = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                if(isNetworkConnected()) {
                    if (currentAccessToken == null) {
                        Log.d("FB", "User Logged Out.");
                        fbname.setVisibility(View.GONE);
                        fbemail.setVisibility(View.GONE);
                        fbphoto.setVisibility(View.GONE);
                        fbloginbutton.setVisibility(View.VISIBLE);
                        fblogout.setVisibility(View.GONE);
                    } else {
                        fbname.setVisibility(View.VISIBLE);
                        fbemail.setVisibility(View.VISIBLE);
                        fbphoto.setVisibility(View.VISIBLE);
                        fbloginbutton.setVisibility(View.GONE);
                    }
                }
                else
                {snackbarerror();}
            }
        };
        tracker1.startTracking();
    }
    public void onClick(View v) {
       initfacebooklogin();
    }
    public Bitmap getBitmapFromURL(String src)
    {
        try {
            if(isNetworkConnected()) {
                URL url = new URL(src);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                BufferedInputStream buf = new BufferedInputStream(input, 1024);
                img = BitmapFactory.decodeStream(buf);
            }
            else
            {snackbarerror();}
                return img;

        } catch (IOException e) {
            Crashlytics.logException(e);
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
            if(isNetworkConnected()) {
                txt = findViewById(R.id.toolbartxt);
                getSupportActionBar().setTitle("Home");
                txt.setText("Home");
                getSupportActionBar().setDisplayShowTitleEnabled(false);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
                getStatusBarHeight();
            }
            else
            {snackbarerror();}
        }
        catch (Exception ex)
        {
            Crashlytics.logException(ex);
            android.util.Log.e("Initialize UI failed", ex.getMessage());
        }
    }
    private int getStatusBarHeight() {
        int result = 0;
        try {
            if(isNetworkConnected()) {
                int resourceId = MainNavigationActivity.this.getResources().getIdentifier("status_bar_height", "dimen", "android");
                if (resourceId > 0) {
                    result = MainNavigationActivity.this.getResources().getDimensionPixelSize(resourceId);
                }
            }
            else
            {snackbarerror();}
        }
        catch (Exception ex)
        {
            Crashlytics.logException(ex);
        }
        return result;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
        catch (Exception ex)
        {
            Crashlytics.logException(ex);
        }
    }
    @Override
    public void onBackPressed() {
        try {
            if(isNetworkConnected()) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else if (checkedItem == R.id.home) {
                    finish();
                } else {
                    checkedItem = R.id.home;
                    fragment = new FoldableListFragment(100);
                    getSupportFragmentManager().beginTransaction().setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                            .replace(R.id.content_frame, fragment)
                            .commit();
                    getSupportActionBar().setTitle("Home");
                    txt.setText("Home");
                    navigationView.setCheckedItem(R.id.home);

                }
            }
            else
            {snackbarerror();}
        }
        catch (Exception ex)
        {
            Crashlytics.logException(ex);
        }
        }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        try {
            if(isNetworkConnected())
            getMenuInflater().inflate(R.menu.main_navigation, menu);
            else
                snackbarerror();
        }
        catch (Exception ex)
        {
            Crashlytics.logException(ex);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        try {
            if (isNetworkConnected()) {
                int id = item.getItemId();

                switch (id) {
                    case R.id.searchitem:
                        Intent intent = new Intent(this, SearchActivity.class);
                        startActivity(intent);
                        return true;

                    case R.id.subscribepost:
                        PrefService ap = new PrefService(this);
                        String subscribed = ap.getAccessKey("subscribe");
                        if (subscribed == "") {
                            SocialMethod.showSubscription(this);
                        } else {
                            SocialMethod.alreadySubscribed(this);
                        }
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
                }
            }
            else {snackbarerror();}
        }

        catch (Exception ex)
        {
            Crashlytics.logException(ex);
        }
            return super.onOptionsItemSelected(item);
        }

    @Override
    public boolean onNavigationItemSelected(final MenuItem item) {
        // Handle navigation view item clicks here.
        try {
            if(isNetworkConnected()) {
                navItemSelected = true;
                drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);

                int id = item.getItemId();
                txt = findViewById(R.id.toolbartxt);

                if (id == R.id.home) {
                    checkedItem = R.id.home;
                    fragment = new FoldableListFragment(100);
                    getSupportActionBar().setTitle("Home");
                    txt.setText("Home");
                } else if (id == R.id.explore) {

                    checkedItem = R.id.explore;
                    fragment = new FoldableListFragment(35);
                    getSupportActionBar().setTitle("Explore Amdavad");
                    txt.setText("Explore Amdavad");

                } else if (id == R.id.flavor) {
                    checkedItem = R.id.flavor;
                    fragment = new FoldableListFragment(36);

                    getSupportActionBar().setTitle("Flavors of Amdavad");
                    txt.setText("Flavors of Amdavad");
                } else if (id == R.id.news) {
                    checkedItem = R.id.news;
                    fragment = new FoldableListFragment(5);
                    getSupportActionBar().setTitle("News");
                    txt.setText("News");

                } else if (id == R.id.thingstodo) {
                    checkedItem = R.id.thingstodo;
                    fragment = new FoldableListFragment(37);
                    getSupportActionBar().setTitle("Things to do");
                    txt.setText("Things to do");
                }
                if (id == R.id.privacypolicy) {

                    checkedItem = R.id.privacypolicy;
                    fragment = new PrivacyPolicyFragment();
                    getSupportActionBar().setTitle("Privacy Policy");
                    txt.setText("Privacy Policy");
                    navigationView.setCheckedItem(R.id.privacypolicy);

                } else if (id == R.id.contactus) {
                    checkedItem = R.id.contactus;
                    fragment = new CommunicationFragment();
                    getSupportActionBar().setTitle("Contact");
                    txt.setText("Contact");
                    navigationView.setCheckedItem(R.id.contactus);
                }
            }
            else
            {snackbarerror();}
            drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
                @Override
                public void onDrawerSlide(@NonNull View view, float slideOffset) {
                    if(slideOffset > .55 && !isDrawerOpen){
                        onDrawerOpened(view);
                        isDrawerOpen = true;
                    } else if(slideOffset < .45 && isDrawerOpen) {
                        onDrawerClosed(view);
                        isDrawerOpen = false;
                    }
                }
                @Override
                public void onDrawerOpened(@NonNull View view) {

                }
                @Override
                public void onDrawerClosed(@NonNull View view) {
                    if(isNetworkConnected()) {
                        if (navItemSelected) {
                            navItemSelected = !navItemSelected;
                            /**
                             * Change fragment for all items excluding nav_five
                             * as it opens up an Activity
                             */
                            getSupportFragmentManager().beginTransaction().setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                                    .replace(R.id.content_frame, fragment)
                                    .commit();
                            getSupportFragmentManager().executePendingTransactions();
                            navigationView.setCheckedItem(checkedItem);


                        }
                    }
                    else
                    {snackbarerror();}
                }
                @Override
                public void onDrawerStateChanged(int i) {

                }
            });
        }
        catch (Exception ex)
        {
            Crashlytics.logException(ex);
        }
        return true;
    }
    private boolean isNetworkConnected() {
        ConnectivityManager cm = null;
        try {
            cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
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
            snackbar = Snackbar
                    .make(drawer, "No internet connection!", Snackbar.LENGTH_INDEFINITE)
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


