package com.example.xps.amdavadblog_app;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
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
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import com.google.firebase.FirebaseApp;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import Helper.PrefService;
import Helper.SocialMethod;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.provider.ContactsContract.Intents.Insert.EMAIL;


public class MainNavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static String FACEBOOK_URL = "https://www.facebook.com/amdavadblog";
    public static String FACEBOOK_PAGE_ID = "2012829695602790";
    TextView txt,fbname,fbemail;
    ImageView fbicon,twittericon,instaicon,googleplusicon,websiteicon;
    Button fbbtn,fblogout;
    CircleImageView fbphoto;
    String appTitle;
    LoginButton fbloginbutton;
    TextView info;
    CallbackManager callbackManager;
    String firstName,lastName,email,birthday,gender,emailtostore,nametostore,textofloginbtn;
    private FoldableListFragment catInstance;
    URL profilePicture;
    String userId,email1,personname,gen,add,dob;
    private FirebaseAnalytics mFirebaseAnalytics;
    Intent main;
    View headerView;
    Bitmap img;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.newtoolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        InitiaalizeGoogleAppConfig();
        FirebaseApp app = FirebaseApp.initializeApp(this);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setAnalyticsCollectionEnabled(true);
        mFirebaseAnalytics.setMinimumSessionDuration(20000);


        //Sets the user ID property.
        mFirebaseAnalytics.setUserId(String.valueOf(GetRandomIndex()));
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // Set paddingTop of toolbar to height of status bar.
            // Fixes statusbar covers toolbar issue
            //toolbar.setPadding(0, getStatusBarHeight(), 0, 0);
        }
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        headerView = navigationView.getHeaderView(0);
        fbname = (TextView)headerView.findViewById(R.id.fbname);
        fbemail = (TextView)headerView.findViewById(R.id.fbemail);
        fbphoto = (CircleImageView) headerView.findViewById(R.id.circleView);
        //fbbtn = (Button)headerView.findViewById(R.id.fblogin);
        fbloginbutton = (LoginButton)headerView.findViewById(R.id.login_button);
        fblogout = (Button)headerView.findViewById(R.id.fblogout);

        fbicon = (ImageView)findViewById(R.id.fbicon);
        twittericon = (ImageView)findViewById(R.id.twittericon);
        instaicon = (ImageView)findViewById(R.id.instaicon);
        googleplusicon = (ImageView)findViewById(R.id.googleplusicon);
        websiteicon = (ImageView)findViewById(R.id.websiteicon);
        fbicon.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View view) {
                                          Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
                                          String facebookUrl = getFacebookPageURL(this);
                                          facebookIntent.setData(Uri.parse(facebookUrl));
                                          startActivity(facebookIntent);
                                      }
                                      });
        twittericon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("twitter://user?screen_name=[amdavadblog]"));
                    startActivity(intent);
                } catch (Exception e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://twitter.com/amdavadblog")));
                }
            }
        });
        instaicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("http://instagram.com/_u/amdavadblog");
                Intent i= new Intent(Intent.ACTION_VIEW,uri);
                i.setPackage("com.instagram.android");
                try {
                    startActivity(i);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://instagram.com/amdavadblog")));
                }
            }
        });
        googleplusicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newGooglePlusIntent("116682005972414924881");
            }
        });
        websiteicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://amdavadblog.com/")));
            }
        });
        fblogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logOut();
                Intent intent1 = new Intent(MainNavigationActivity.this,MainNavigationActivity.class);
                startActivity(intent1);
            }
        });
        InitializeFirstScreenUI();

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

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
            fblogout.setVisibility(View.VISIBLE);
        }
        initfacebooklogin();
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
            return FACEBOOK_URL; //normal web url
        }

    }
    private void newGooglePlusIntent(String s) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setClassName("com.google.android.apps.plus",
                    "com.google.android.apps.plus.phone.UrlGatewayActivity");
            intent.putExtra("customAppUri", s);
            startActivity(intent);
        } catch(ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://plus.google.com/"+s+"/posts")));
        }

    }
    private void InitiaalizeGoogleAppConfig()
    {
        ////0.Initialize Firebase token.
        //if (!GetString(Resource.String.google_app_id).Equals(AppConstants.GOOGLE_APP_ID))
        //{
        //    FirebaseCrash.Report(new System.Exception("Invalid Json file"));
        //}

      Runnable Task = new Runnable() {
          @Override
          public void run() {
              FirebaseInstanceId instanceId = FirebaseInstanceId.getInstance();
              try {
                  instanceId.deleteInstanceId();
              } catch (IOException e) {
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
        callbackManager = CallbackManager.Factory.create();

        fbloginbutton = (LoginButton)headerView.findViewById(R.id.login_button);
        fbloginbutton.setReadPermissions(Arrays.asList(EMAIL));
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));
//        fbbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (view == fbbtn) {
//                    fbloginbutton.performClick();
//                   // updatefbbtn();
//                }
//            }
//        });

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
//
//                               fbname.setVisibility(View.VISIBLE);
//                               fbemail.setVisibility(View.VISIBLE);
//                               fbphoto.setVisibility(View.VISIBLE);

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
                                        R.drawable.ic_home_black_24dp);
                                fbphoto.setImageBitmap(img);
                            }
                            else {
                                getBitmapFromURL(imgfb);
                                fbphoto.setImageURI(null);
                                fbphoto.setImageBitmap(img);
                            }
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
                System.out.println("onCancel");
            }
            @Override
            public void onError(FacebookException error) {
                System.out.println("onError");
                Log.v("LoginActivity", error.getCause().toString());
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
                    fbloginbutton.setVisibility(View.VISIBLE);
                    fblogout.setVisibility(View.GONE);
                } else {
                    fbname.setVisibility(View.VISIBLE);
                    fbemail.setVisibility(View.VISIBLE);
                    fbphoto.setVisibility(View.VISIBLE);
                    fbloginbutton.setVisibility(View.GONE);
                    fblogout.setVisibility(View.VISIBLE);
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
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//             SupportActionBar.Title = appTitle = Categories[v].name;
//             txt.Text = SupportActionBar.Title;
            txt = findViewById(R.id.toolbartxt);
            getSupportActionBar().setTitle("Home");
            txt.setText("Home");
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
            getStatusBarHeight();
            catInstance = new FoldableListFragment(100);
            ft.replace(R.id.content_frame, new FoldableListFragment(100), "Fragment1");
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
//    private void disableNavigationViewScrolling(NavigationView navigationView) {
//        if (navigationView != null) {
//            NavigationMenuView navigationMenuView = (NavigationMenuView) navigationView.getChildAt(0);
//            if (navigationMenuView != null) {
//                navigationMenuView.setNestedScrollingEnabled(false);
//            }
//        }
//    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout)findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else if(catInstance.CategoryId == 100)
        {
            finish();
        }
        else {
            onNavigationItemSelected(navigationView.getMenu().getItem(0));
            getSupportActionBar().setTitle("Home");
            txt.setText("Home");
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
                PrefService ap = new PrefService(this);
              //  String subscribed = ap.getAccessKey("subscribe");
               // if (subscribed == "")
                {
                    SocialMethod.showSubscription(this);
                }
               // else
                {
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
            case R.id.settings:
                Intent i = new Intent(this,SettingsActivity.class);
                startActivity(i);
                return true;
          }

            return super.onOptionsItemSelected(item);
        }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        txt = findViewById(R.id.toolbartxt);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (id == R.id.home)
        {
            catInstance = new FoldableListFragment(100);
            ft.replace(R.id.content_frame, new FoldableListFragment(100), "Fragment1");
            ft.commit();
            getSupportActionBar().setTitle("Home");
            txt.setText("Home");

        }
        else if (id == R.id.explore)
        {
            catInstance = new FoldableListFragment(35);
            ft.replace(R.id.content_frame, new FoldableListFragment(35), "Fragment1");
            ft.commit();
            getSupportActionBar().setTitle("Explore Amdavad");
            txt.setText("Explore Amdavad");
        }
        else if (id == R.id.flavor)
        {
            catInstance = new FoldableListFragment(36);
            ft.replace(R.id.content_frame, new FoldableListFragment(36), "Fragment1");
            ft.commit();
            getSupportActionBar().setTitle("Flavors of Amdavad");
            txt.setText("Flavors of Amdavad");
        }
        else if (id == R.id.news)
        {
            catInstance = new FoldableListFragment(5);
            ft.replace(R.id.content_frame, new FoldableListFragment(5), "Fragment1");
            ft.commit();
            getSupportActionBar().setTitle("News");
            txt.setText("News");
        }
        else if (id == R.id.thingstodo)
        {
            catInstance = new FoldableListFragment(37);
            ft.replace(R.id.content_frame, new FoldableListFragment(37), "Fragment1");
            ft.commit();
            getSupportActionBar().setTitle("Things to Do");
            txt.setText("Things to Do");
        }
        else if (id == R.id.privacypolicy) {

        }
        else if (id == R.id.contactus) {

        }
        Bundle param = new Bundle();
        param.putString("name", String.valueOf(getSupportActionBar().getTitle()));
      //  param.putString("id", v.ToString());
        //EventServices.Instance.GenericEvent(EventType.CategorySelect, param);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
