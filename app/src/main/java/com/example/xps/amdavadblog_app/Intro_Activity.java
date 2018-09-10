package com.example.xps.amdavadblog_app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
import com.facebook.login.LoginManager;
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
import java.util.Arrays;
import java.util.List;

import Adapter.IntroScreenAdapter;
import Helper.IntroPageTransformer;
import Helper.PrefService;

import static android.provider.ContactsContract.Intents.Insert.EMAIL;
import static com.facebook.FacebookSdk.getApplicationContext;

public class Intro_Activity extends AppCompatActivity {
    private ViewPager mViewPager;
    private int[] layouts;
    private Button btnSkip, btnNext, fblogincustombtn;
    String gender, add, userId;
    Bundle bundle = new Bundle();
    TextView info;
    LoginButton loginButton;
    CallbackManager callbackManager;
    RelativeLayout relativeLayout;
    Snackbar snackbar;
    URL profilePicture;
    Bitmap img;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    final String PREFS_NAME = "MyPrefsFile";
    final String PREFS_FIRST_RUN = "first_run";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        facebookSDKInitialize();
        setContentView(R.layout.activity_intro);
        checkFirstRun();
        callbackManager = CallbackManager.Factory.create();

        relativeLayout = (RelativeLayout) findViewById(R.id
                .relativelayout);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabDots);
        tabLayout.setupWithViewPager(mViewPager, true);
        dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);
        btnSkip = (Button) findViewById(R.id.btn_skip);
        btnNext = (Button) findViewById(R.id.btn_next);
        loginButton = (LoginButton) findViewById(R.id.login_button);
        fblogincustombtn = (Button) findViewById(R.id.fblogincustombtn);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        layouts = new int[]{
                R.layout.introscreenfragmentlayout_1,
                R.layout.introscreenfragmentlayout_2,
                R.layout.introscreenfragmentlayout_3,
                R.layout.introscreenfragmentlayout_4,
                };
        //addBottomDots(0);
        changeStatusBarColor();
        mViewPager.setAdapter(new IntroScreenAdapter(getSupportFragmentManager()));

        mViewPager.setPageTransformer(false, new IntroPageTransformer());
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
            getLoginDetails(fblogincustombtn);
        } else {
            snackbarerror();
        }
   }

    private void addBottomDots(int i) {
        dots = new TextView[layouts.length];

        int[] colorsActive = getResources().getIntArray(R.array.array_pager_active);

        int[] colorsInactive = getResources().getIntArray(R.array.array_pager_inactive);

        dotsLayout.removeAllViews();

        for (int j = 0; j < dots.length; i++) {

            dots[j] = new TextView(this);

            dots[j].setText(Html.fromHtml("•"));

            dots[j].setTextSize(35);

           // dots[j].setTextColor(colorsInactive[i]);

            dotsLayout.addView(dots[j]);

        }

       // if (dots.length > 0)

            //dots[i].setTextColor(colorsActive[i]);
    }
    //  viewpager change listener

    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float v, int i1) {
            addBottomDots(position);
            // changing the next button text 'NEXT' / 'GOT IT'

            if (position == layouts.length - 1) {

                // last page. make button text to GOT IT
                btnNext.setText("Start");
                btnSkip.setVisibility(View.GONE);
            } else {
                // still pages are left
                btnNext.setText("NEXT");
                btnSkip.setVisibility(View.VISIBLE);
            }
        }
        @Override
        public void onPageSelected(int i) {

        }
        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };
    private void changeStatusBarColor() {

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//
//            Window window = getWindow();
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(Color.TRANSPARENT);
//        }
    }
    private void checkFirstRun() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        // If the app is launched for first time, view splash screen and setup 'Next >>' link.
        if (sharedPreferences.getBoolean(PREFS_FIRST_RUN, true)) {

            // Record that user have done first run.
            sharedPreferences.edit().putBoolean(PREFS_FIRST_RUN, false).apply();
        }
        // Else, directly go to Registration page.
        else {

            goToMainActivity();
        }
    }

    private void goToMainActivity() {

        Intent intent = new Intent(this, MainNavigationActivity.class);
        startActivity(intent);
        finish();
    }

    private void getLoginDetails(Button fblogincustombtn) {
        List< String > permissionNeeds = Arrays.asList("user_photos", "email",
                "user_birthday", "public_profile", "AccessToken");

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult login_result) {
                System.out.println("onSuccess");
                String accessToken = login_result.getAccessToken()
                        .getToken();
                Log.i("accessToken", accessToken);
                getUserInfo(login_result);
                loginButton.setVisibility(View.GONE);
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

    public void onClick(View v) {
        if (v == fblogincustombtn) {
            loginButton.performClick();
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
                            final String email =json_object.getString("email");
                            if(imgfb == null)
                            {
                                img = BitmapFactory.decodeResource(getResources(),
                                        R.drawable.ic_home_black_24dp);
                            }
                            else {
                                getBitmapFromURL(imgfb);
                            }
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

        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);
        snackbar.show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            SharedPreferences settings = getSharedPreferences("prefs", 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("firstRun", false);
            editor.commit();
        }
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