package com.example.xps.amdavadblog_app;


import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.commons.lang3.StringEscapeUtils;

import Core.Helper.SynchronousCallAdapterFactory;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationClickFragment extends Fragment {

    public static String posturl;
    public String detail;
    ImageView imageView;
    private WebView webviewLayout;
    TextView titleTextView, authortextview, datetextview;
    Retrofit retrofitallpost;
    static Toast toast;
    android.graphics.drawable.AnimationDrawable animation;
    public ImageLoader AppImageLoader;
    Snackbar snackbar;
    RelativeLayout relativedetaillayout;
    public NotificationClickFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = null;
        try {

            view = inflater.inflate(R.layout.fragment_notification_click, container, false);
            AssetManager am = getActivity().getApplicationContext().getAssets();
            relativedetaillayout = view.findViewById(R.id.relativedetaillayoutnotifi);
            webviewLayout = view.findViewById(R.id.webViewnotifi);
            titleTextView = view.findViewById(R.id.posttitletextnotifi);
            authortextview = view.findViewById(R.id.postauthornotifi);
            datetextview = view.findViewById(R.id.postdatenotifi);
            imageView = view.findViewById(R.id.webviewloadingnotifi);
            imageView.setVisibility(View.VISIBLE);
            retrofitallpost = new Retrofit.Builder()
                    .baseUrl("http://api.amdavadblog.com/amdblog/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(SynchronousCallAdapterFactory.create())
                    .build();

            WebViewInitialize();

        } catch (Exception ex) {
            Crashlytics.logException(ex);
        }
        return view;
    }

    private void WebViewInitialize() {
        try {
            if (isNetworkConnected()) {
                AssetManager am = getContext().getAssets();
                Typeface custom_font = Typeface.createFromAsset(am, "font/Lora-Bold.ttf");
                Typeface custom_font1 = Typeface.createFromAsset(am, "font/Martel-Bold.ttf");
                int id1 = this.getActivity().getIntent().getIntExtra("BlogId",0);

                //final String id1 = getActivity().getIntent().getStringExtra("BlogId",0);
                String title = this.getArguments().getString("Title");
                String decodedtitle = StringEscapeUtils.unescapeHtml3(title);
                String author = this.getArguments().getString("Author");
                String date = this.getArguments().getString("Date");
                String category = this.getArguments().getString("Category");
                String image = this.getArguments().getString("Image");

                titleTextView.setText(decodedtitle);
                authortextview.setText(author);
                datetextview.setText(date);

                titleTextView.setTypeface(custom_font);
                authortextview.setTypeface(custom_font1);
                datetextview.setTypeface(custom_font1);

                final String Content = this.getArguments().getString("Content");
                ;
                final String replacedtitle = title.replace(" ", "-");
                posturl = "http://amdavadblog.com/" + replacedtitle.toLowerCase();
                if (!android.text.TextUtils.isEmpty(getActivity().getIntent().getStringExtra("posturl"))) {
                    posturl = getActivity().getIntent().getStringExtra("posturl");
                }
                final String stylestr = "<html><head><style type=\"text/css\" link rel=\"stylesheet\" href=\"style.css\" />img{display: inline; height: auto; max-width: 100%;}@font-face {font-family: MyFont;src: url(\"file:///android_asset/fonts/PT_Serif-Web-Regular.ttf\")}body {font-family: MyFont;color: #6d6c6c;line-height: 30px;font-size: 18px;text-align: justify;} iframe {display: block;max-width:100%;margin-top:10px;margin-bottom:10px;}</style></head><body>";
                final String pas = "</body></html>";
                detail = stylestr + Content + pas;

                Bundle param = new Bundle();
                param.putInt("id", id1);
                WebSettings webSetting = webviewLayout.getSettings();
                webSetting.setTextSize(WebSettings.TextSize.NORMAL);
                webSetting.setJavaScriptEnabled(true);
                webSetting.setMediaPlaybackRequiresUserGesture(false);
                webSetting.setLoadsImagesAutomatically(true);
                webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
                webviewLayout.setWebViewClient(new MyWebChromeClient(getActivity()));
                webviewLayout.loadDataWithBaseURL(null, detail, "text/html", "UTF-8", null);
                webviewLayout.addJavascriptInterface(this, "MyApp");
                imageView.setVisibility(View.GONE);
            }
            else
            {snackbarerror();}
        }catch (Exception ex) {
            Crashlytics.logException(ex);
        }
    }



    @JavascriptInterface
    public void resize(final float height) {
        try {

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    webviewLayout.setLayoutParams(new LinearLayout.LayoutParams(getResources().getDisplayMetrics().widthPixels, (int) (height * getResources().getDisplayMetrics().density)));
                }
            });

        } catch (Exception ex) {
            Crashlytics.logException(ex);
        }
    }

    private class MyWebChromeClient extends WebViewClient {
        public Context con;

        public MyWebChromeClient(Context con) {
            try {
                this.con = con;
            } catch (Exception ex) {
                Crashlytics.logException(ex);
            }
        }

        public void displayToast(String message) {
            if (toast != null)
                toast.cancel();
            toast.show();
        }

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            try {
                if(isNetworkConnected())
                    openInAppBrowser(url);
                else
                    snackbarerror();
            } catch (Exception ex) {
                Crashlytics.logException(ex);
            }
            return true;
        }

        private void openInAppBrowser(String url) {
            try {
                if(isNetworkConnected()) {
                    Intent intent = new Intent(con, BrowserActivity.class);
                    intent.putExtra("url", url);
                    con.startActivity(intent);
                }
                else
                    snackbarerror();
            } catch (Exception ex) {
                Crashlytics.logException(ex);
            }
        }
    }
    private boolean isNetworkConnected() {
        ConnectivityManager cm = null;
        try {
            cm = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
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
                    .make(relativedetaillayout, "No internet connection!", Snackbar.LENGTH_INDEFINITE)
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
