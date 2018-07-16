package com.example.xps.amdavadblog_app;

import android.app.usage.UsageEvents;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.EventLog;
import android.util.Log;
import android.view.View;
import android.webkit.WebBackForwardList;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toolbar;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbarbrowser);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       // getSupportActionBar().setHomeAsUpIndicator(R.drawable.close);

        url = getIntent().getStringExtra("url");
        //SupportActionBar.Title = url;
        txt = findViewById(R.id.toolbartitle);
        txt.setBackgroundColor(Color.MAGENTA);
        //txt.Text = SupportActionBar.Title;

        if (android.text.TextUtils.isEmpty(url))
        {
            finish();
        }
        imageView = findViewById(R.id.browserloading);

        animation = (android.graphics.drawable.AnimationDrawable)imageView.getDrawable();


        webView = findViewById(R.id.webviewbrowser);

        //  progressBar = FindViewById<ProgressBar>(Resource.Id.progressBar);
        InitializeAds();
        initWebView();

        webView.loadUrl(url);
        getSupportActionBar().setTitle(url);
        txt.setText(getSupportActionBar().getTitle());
        txt.setBackgroundResource(R.drawable.browsertoolbartitile);
    }

    private void InitializeAds() {
        try
        {
            MobileAds.initialize(this, "ca-app-pub-1870433400625480~7602115204");

            adView = (AdView)findViewById(R.id.adView3);
            //adView.AdSize = AdSize.SmartBanner;
            //adView.AdUnitId = "ca-app-pub-1870433400625480/7574195045";
            // AdRequest adRequest = new AdRequest.Builder().Build();

            AdRequest adRequest = new AdRequest.Builder().addTestDevice("EB38215ED85EFA82E937126940E5C31F").build();
            adView.loadAd(adRequest);
        }
        catch (Exception ex)
        {
            //Log.ERROR("Couldnt initialize ads", ex.getMessage());
        }
    }

    private void initWebView()
    {
        MyWebChromeClient client = new MyWebChromeClient(this);
        webView.setWebViewClient(client);
        webView.clearCache(true);
        webView.clearHistory();
        webView.getSettings().setJavaScriptEnabled(true);
//        webView.setWebChromeClient(new MyWebChromeClient(this));
//        webView.setWebViewClient(new WebViewClient() {
//                                     @Override
//                                     public void onPageStarted(WebView view, String url, Bitmap favicon) {
//                                         super.onPageStarted(view, url, favicon);
//                                        // progressBar.setVisibility(View.VISIBLE);
//                                         invalidateOptionsMenu();
//                                     }

  //                                   @Override
//                                     public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                                         if (somethingHappened == true)
//                                             //somethingHappened(url);
//
//                                         view.loadUrl(url);
//
//                                         return true;
//                                     }

//            private void somethingHappened(String url) {
//                getSupportActionBar().setTitle(url);
//                txt.setText(getSupportActionBar().getTitle());
//                txt.setBackgroundResource(R.drawable.browsertoolbartitile);
//            }

//            @Override
//            public void onPageFinished(WebView view, String url) {
//                super.onPageFinished(view, url);
//               // progressBar.setVisibility(View.GONE);
//                invalidateOptionsMenu();
//            }
//
//            @Override
//            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
//                super.onReceivedError(view, request, error);
//                //progressBar.setVisibility(View.GONE);
//                invalidateOptionsMenu();
//            }
//        });
        //MyWebChromeClient.

        //webView.SetHorizontalScrollbarOverlay(false);
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack())
        {
            WebBackForwardList mWebBackForwardList = webView.copyBackForwardList();
            String historyUrl = mWebBackForwardList.getItemAtIndex(mWebBackForwardList.getCurrentIndex()- 1).getUrl();
            getSupportActionBar().setTitle(historyUrl);
            getSupportActionBar().getTitle();
            txt.setText(getSupportActionBar().getTitle());
            txt.setBackgroundResource(R.drawable.browsertoolbartitile);
            webView.goBack();

        }
        else
            super.onBackPressed();
    }

    private class MyWebChromeClient extends WebViewClient {
        Context context;
        private InterestingEvent ie;
        private boolean somethingHappened;

        public MyWebChromeClient(CallMe callMe) {
            super();
           // this.context = context;
            // Save the event object for later use.
            ie = callMe;
            // Nothing to report yet.
            somethingHappened = false;
        }


        public MyWebChromeClient(BrowserActivity browserActivity) {
            super();
            this.context = browserActivity;
        }
        @Override
        public boolean shouldOverrideUrlLoading(WebView view,String url)
        {
            // Check the predicate, which is set elsewhere.
            if (somethingHappened)
            {
                // Signal the even by invoking the interface's method.
                ie.interestingEvent ();

            }
            view.loadUrl(url);
            getSupportActionBar().setTitle(url);
            txt.setText(getSupportActionBar().getTitle());
            txt.setBackgroundResource(R.drawable.browsertoolbartitile);
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
            // Create the event notifier and pass ourself to it.
            en = new MyWebChromeClient (this);
        }
        // Define the actual handler for the event.
        public void interestingEvent ()
        {
            // Wow!  Something really interesting must have occurred!
            // Do something...
        }
        //...
    }
}
