package com.example.xps.amdavadblog_app;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebBackForwardList;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class BrowserActivity extends AppCompatActivity {
    private String url;
    private AdView adView;
    private EditText txt;
    ImageView imageView;
    android.graphics.drawable.AnimationDrawable animation;
    private WebView webView;
    Snackbar snackbar;
    CoordinatorLayout coordinator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_browser);
            if (isNetworkConnected()) {
                android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbarbrowser);
                setSupportActionBar(toolbar);

                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
                coordinator = (CoordinatorLayout) findViewById(R.id.coordinator);
                url = getIntent().getStringExtra("url");
                txt = findViewById(R.id.toolbartitle);
                txt.setBackgroundColor(Color.MAGENTA);

                if (android.text.TextUtils.isEmpty(url)) {
                    finish();
                }

                webView = findViewById(R.id.webviewbrowser);

                initWebView();

                webView.loadUrl(url);
                InitializeAds();
                getSupportActionBar().setTitle(url);
                txt.setText(getSupportActionBar().getTitle());
                txt.setBackgroundResource(R.drawable.browsertoolbartitile);
            }
            else
            {snackbarerror();}
        }
        catch (Exception ex)
        {
            Crashlytics.logException(ex);
        }
    }

    private void InitializeAds() {
        try
        {
            MobileAds.initialize(this, "ca-app-pub-1870433400625480~7602115204");
            adView = (AdView)findViewById(R.id.adView3);
            AdRequest adRequest = new AdRequest.Builder().addTestDevice("EB38215ED85EFA82E937126940E5C31F").build();
            adView.loadAd(adRequest);
        }
        catch (Exception ex)
        {
            Crashlytics.logException(ex);
            //Log.ERROR("Couldnt initialize ads", ex.getMessage());
        }
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                adView.setTag(true); // Set tag true if adView is loaded
            }
            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                adView.setTag(false); // Set tag false if loading failed
            }
        });
    }

    private void initWebView()
    {
        try {
            if(isNetworkConnected()) {
                MyWebChromeClient client = new MyWebChromeClient(this);
                webView.setWebViewClient(client);
                webView.clearCache(true);
                webView.clearHistory();
                webView.getSettings().setJavaScriptEnabled(true);
                webView.setHorizontalScrollBarEnabled(false);
                webView.getSettings().setLoadWithOverviewMode(true);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        Bundle param = new Bundle();
        try {
            if(isNetworkConnected()) {
                param.putString("name", String.valueOf(item.getItemId()));
                android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                switch (item.getItemId()) {
                    case android.R.id.home:
                        finish();
                        return true;
                }
            }
            else
            {snackbarerror();}
        }
        catch (Exception ex)
        {
            Crashlytics.logException(ex);
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        try {
            if(isNetworkConnected()) {
                if (webView.canGoBack()) {
                    WebBackForwardList mWebBackForwardList = webView.copyBackForwardList();
                    String historyUrl = mWebBackForwardList.getItemAtIndex(mWebBackForwardList.getCurrentIndex() - 1).getUrl();
                    getSupportActionBar().setTitle(historyUrl);
                    getSupportActionBar().getTitle();
                    txt.setText(getSupportActionBar().getTitle());
                    txt.setBackgroundResource(R.drawable.browsertoolbartitile);
                    webView.goBack();
                } else
                    super.onBackPressed();
            }
            else
            {snackbarerror();}
        }
        catch (Exception ex)
        {
            Crashlytics.logException(ex);
        }
    }

    private class MyWebChromeClient extends WebViewClient {

        Context context;
        private InterestingEvent ie;
        private boolean somethingHappened;

        public MyWebChromeClient(CallMe callMe) {
            super();
            try {

                // this.context = context;
                // Save the event object for later use.
                ie = callMe;
                // Nothing to report yet.
                somethingHappened = false;
            }
            catch (Exception ex)
            {
                Crashlytics.logException(ex);
            }
        }
        public MyWebChromeClient(BrowserActivity browserActivity) {
            super();
            try {
                this.context = browserActivity;
            }
            catch (Exception ex)
            {
                Crashlytics.logException(ex);
            }
        }
        @Override
        public boolean shouldOverrideUrlLoading(WebView view,String url)
        {
            try {
                // Check the predicate, which is set elsewhere.
                if (somethingHappened) {
                    // Signal the even by invoking the interface's method.
                    ie.interestingEvent();
                }
                view.loadUrl(url);
                getSupportActionBar().setTitle(url);
                txt.setText(getSupportActionBar().getTitle());
                txt.setBackgroundResource(R.drawable.browsertoolbartitile);
            }
            catch (Exception ex)
            {
                Crashlytics.logException(ex);
            }
            return true;
        }
    }
    public interface InterestingEvent
    {
        // This is just a regular method so it can return something or
        // take arguments if you like.
        public void interestingEvent ();
    }

    public class CallMe implements InterestingEvent
    {

        private MyWebChromeClient en;
        public CallMe ()
        {
            try {
                // Create the event notifier and pass ourself to it.
                en = new MyWebChromeClient(this);
            }
            catch (Exception ex)
            {
                Crashlytics.logException(ex);
            }
        }
        // Define the actual handler for the event.
        public void interestingEvent ()
        {
            // Wow!  Something really interesting must have occurred!
            // Do something...
        }
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
                    .make(coordinator, "No internet connection!", Snackbar.LENGTH_INDEFINITE)
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
