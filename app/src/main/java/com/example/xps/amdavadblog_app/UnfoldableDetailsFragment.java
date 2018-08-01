package com.example.xps.amdavadblog_app;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alexvasilkov.android.commons.texts.SpannableBuilder;
import com.alexvasilkov.android.commons.ui.Views;
import com.alexvasilkov.foldablelayout.UnfoldableView;
import com.alexvasilkov.foldablelayout.shading.GlanceFoldShading;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import Core.WordPressService;
//import Helper.IFrameParser;
import Model.Post;
import Model.SynchronousCallAdapterFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 */
public class UnfoldableDetailsFragment extends Fragment {
    public static String posturl;

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String detail;
    ImageView imageView;
    private WebView webviewLayout;
    TextView titleTextView;
    Retrofit retrofitallpost;
    private int fontSize;
    static Toast toast;
    android.graphics.drawable.AnimationDrawable animation;

    public ImageLoader getAppImageLoader() {
        return AppImageLoader;
    }

    public void setAppImageLoader(ImageLoader appImageLoader) {
        AppImageLoader = appImageLoader;
    }

    public ImageLoader AppImageLoader;

    public UnfoldableDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_unfoldable_details, container, false);
        AssetManager am = getActivity().getApplicationContext().getAssets();
        webviewLayout = view.findViewById(R.id.webView1);
        titleTextView = view.findViewById(R.id.posttitletext);
//        Typeface tf = Typeface.createFromAsset(am, "fonts/Lora-Bold.ttf");
        //titleTextView.setTypeface(tf);
        String title = this.getActivity().getIntent().getStringExtra("Title");
        titleTextView.setText(title);

        imageView = view.findViewById(R.id.webviewloading);
        imageView.setVisibility(View.VISIBLE);
        // animation = (android.graphics.drawable.AnimationDrawable)imageView.getDrawable();
//        animation.start();
        //CacheService.ClearAllCache();
        retrofitallpost = new Retrofit.Builder()
                .baseUrl("https://amdavadblogs.apps-1and1.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(SynchronousCallAdapterFactory.create())
                .build();

        WebViewInitialize();
        //  backgroundworker1.RunWorkerAsync();
        return view;
//        listTouchInterceptor = Views.find(this, R.id.touch_interceptor_view);
//        listTouchInterceptor.setClickable(false);
//        detailsLayout = Views.find(this, R.id.details_layout);
//        detailsLayout.setVisibility(View.INVISIBLE);
//
//        unfoldableView = Views.find(this, R.id.unfoldable_view);
//
//        Bitmap glance = BitmapFactory.decodeResource(getResources(), R.drawable.unfold_glance);
//        unfoldableView.setFoldShading(new GlanceFoldShading(glance));
//
//        //  detailsLayout = Views.find(this,R.id.details_layout);
//        //  detailsLayout.setVisibility(View.INVISIBLE);
//        image = Views.find(detailsLayout, R.id.details_image);
//
//        description = Views.find(detailsLayout, R.id.details_text);
//
//        unfoldableView.setOnFoldingListener(new UnfoldableView.SimpleFoldingListener() {
//            @Override
//            public void onUnfolding(UnfoldableView unfoldableView) {
//                listTouchInterceptor.setClickable(true);
//                detailsLayout.setVisibility(View.VISIBLE);
//            }
//
//            @Override
//            public void onUnfolded(UnfoldableView unfoldableView) {
//                listTouchInterceptor.setClickable(false);
//            }
//
//            @Override
//            public void onFoldingBack(UnfoldableView unfoldableView) {
//                listTouchInterceptor.setClickable(true);
//            }
//
//            @Override
//            public void onFoldedBack(UnfoldableView unfoldableView) {
//                listTouchInterceptor.setClickable(false);
//                detailsLayout.setVisibility(View.INVISIBLE);
//            }
//        });
    }

    private void WebViewInitialize() {

        final int id1 = this.getActivity().getIntent().getIntExtra("BlogId", 0);
        String title = this.getActivity().getIntent().getStringExtra("Title");
        final String Content = this.getActivity().getIntent().getStringExtra("Content");
        String replacedtitle = title.replace(" ", "-");
        posturl = "http://amdavadblogs.apps-1and1.com/en/" + replacedtitle.toLowerCase();
        if (!android.text.TextUtils.isEmpty(getActivity().getIntent().getStringExtra("posturl"))) {
            posturl = getActivity().getIntent().getStringExtra("posturl");
        }
        final String stylestr = "<html><head><style type=\"text/css\" link rel=\"stylesheet\" href=\"style.css\" />img{display: inline; height: auto; max-width: 100%;}@font-face {font-family: MyFont;src: url(\"file:///android_asset/fonts/PT_Serif-Web-Regular.ttf\")}body {font-family: MyFont;color: #6d6c6c;line-height: 30px;font-size: 18px;text-align: justify;} iframe {display: block;max-width:100%;margin-top:10px;margin-bottom:10px;}</style></head><body>";
        final String pas = "</body></html>";
        final WordPressService wordPressService = retrofitallpost.create(WordPressService.class);
        Call<Post.PostDetail> call = null;
        call = wordPressService.getPostDetailById(id1);
        call.enqueue(new Callback<Post.PostDetail>() {
            @Override
            public void onResponse(Call<Post.PostDetail> call, Response<Post.PostDetail> response) {
                if(id1 == 0)
                {
                    detail = stylestr + Content + pas;
                }
                else {
                    String tobeParsed = response.body().content.rendered;
                    //String afterParsed = IFrameParser.urlUpdate(tobeParsed);
                    //return afterParsed;
                    // String BlogContent = Html.fromHtml(call.getClass().);
                    detail = stylestr + tobeParsed + pas;
                }
                Bundle param = new Bundle();
                param.putInt("id", id1);
                WebSettings webSetting = webviewLayout.getSettings();
                webSetting.setTextSize(WebSettings.TextSize.SMALLER);
                webSetting.getJavaScriptEnabled();
                webSetting.getLoadWithOverviewMode();
                webSetting.getLoadsImagesAutomatically();
                webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
                webviewLayout.setWebViewClient(new MyWebChromeClient(getActivity()));
                webviewLayout.setInitialScale(335);
                webviewLayout.loadDataWithBaseURL(null, detail, "text/html", "UTF-8", null);
            }

            @Override
            public void onFailure(Call<Post.PostDetail> call, Throwable t) {

            }
        });
        imageView.setVisibility(View.GONE);

    }

    private class MyWebChromeClient extends WebViewClient {
        public Context con;

        public MyWebChromeClient(Context con) {
            this.con = con;
        }

        public void displayToast(String message) {
            if (toast != null)
                toast.cancel();
            // toast = Toast.makeText(con, message, ToastLength.Long);
            toast.show();
        }

        //   public boolean isOnline()
        //  {
//            ConnectivityManager cm = (ConnectivityManager)con.getSystemService(Context.CONNECTIVITY_SERVICE);
//            //NetworkInfo netInfo = cm.getActiveNetworkInfo();
//            if (netInfo != null && netInfo.isConnectedOrConnecting())
//            {
//                return true;
//            }
//            else
//            {
//                return false;
//            }
        //}
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            openInAppBrowser(url);
            return true;
        }

        private void openInAppBrowser(String url) {
//            if (isOnline())
//            {
            Intent intent = new Intent(con, BrowserActivity.class);
            intent.putExtra("url", url);
            con.startActivity(intent);
        }
    }
//            }
//            else {
//                displayToast("Please check your internet connection");
//            }
}

//
//

