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

import Model.Post;


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
    //  private ImageView postfeaturedimage;
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
        View view =  inflater.inflate(R.layout.fragment_unfoldable_details, container, false);
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

        int id = this.getActivity().getIntent().getIntExtra("BlogId", 0);
        String title = this.getActivity().getIntent().getStringExtra("Title");
        String replacedtitle = title.replace(" ", "-");
        posturl = "http://amdavadblogs.apps-1and1.com/en/" + replacedtitle.toLowerCase();
        if (!android.text.TextUtils.isEmpty(getActivity().getIntent().getStringExtra("posturl")))
        {
            posturl = getActivity().getIntent().getStringExtra("posturl");
        }
       // String deatilblog = AsyncHelpers.RunSync(async () => await ApiHelper.Instance.GetPostDetailById(id, lang));
        String stylestr = "<html><head><style type=\"text/css\" link rel=\"stylesheet\" href=\"style.css\" />img{display: inline; height: auto; max-width: 100%;}@font-face {font-family: MyFont;src: url(\"file:///android_asset/fonts/PT_Serif-Web-Regular.ttf\")}body {font-family: MyFont;color: #6d6c6c;line-height: 30px;font-size: 18px;text-align: justify;} iframe {display: block;max-width:100%;margin-top:10px;margin-bottom:10px;}</style></head><body>";
        String pas = "</body></html>";
        detail = stylestr + "https://amdavadblog.com" + pas;
        Bundle param = new Bundle();
       // param.putString("id", id.);
        //EventServices.Instance.GenericEvent(EventType.PostDetailsReceived);

//        animation.stop();
        imageView.setVisibility(View.GONE);
        WebSettings webSetting = webviewLayout.getSettings();
        webSetting.setTextSize(WebSettings.TextSize.SMALLER);
        webSetting.getJavaScriptEnabled();
        webSetting.getLoadWithOverviewMode();
        webSetting.getLoadsImagesAutomatically();
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webviewLayout.setWebViewClient(new MyWebChromeClient(getActivity()));
        webviewLayout.setInitialScale(335);
        //webviewLayout.loadDataWithBaseURL(null, "https://amdavadblog.com", "text/html", "UTF-8", null);
        webviewLayout.loadUrl("https://amdavadblog.com");
    }

    private class MyWebChromeClient extends WebViewClient
    {
        public Context con;

            public MyWebChromeClient(Context con)
        {
            this.con = con;
        }
        public void displayToast(String message)
        {
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
        public boolean shouldOverrideUrlLoading(WebView view, String url)
        {
            //string s = "http://amdavadblogs.apps-1and1.com/wp-content/uploads/";
            //if (url.Contains(s))
            //{
            //    LayoutInflater layoutinflater = LayoutInflater.From(con);
            //    ViewGroup vg = (ViewGroup)layoutinflater.Inflate(Resource.Layout.PopupImageWebview, null);
            //    var alert1 = new Android.Support.V7.App.AlertDialog.Builder(con);

            //    ImageView image = (ImageView)vg.FindViewById(Resource.Id.imgpopup);
            //    Java.Net.URL url2 = new Java.Net.URL(url);
            //    Bitmap bmp = BitmapFactory.DecodeStream(url2.OpenConnection().InputStream);
            //    image.SetImageBitmap(bmp);
            //    alert1.SetView(vg);

            //    var _dialog = alert1.Create();
            //    _dialog.Show();
            //}
            //else
            openInAppBrowser(url);
            return true;
        }
        private void openInAppBrowser(String url)
        {
//            if (isOnline())
//            {
                Intent intent = new Intent(con, BrowserActivity.class);
                intent.putExtra("url", url);
                con.startActivity(intent);
//            }
//            else {
//                displayToast("Please check your internet connection");
//            }
        }
    }

}
//    @Override
//    public void onBackPressed() {
//        if (unfoldableView != null
//                && (unfoldableView.isUnfolded() || unfoldableView.isUnfolding())) {
//            unfoldableView.foldBack();
//        } else {
//            super.onBackPressed();
//        }
//    }
//
//    public void openDetails(View coverView, Post post) {
//
//        final ImageView image = Views.find(detailsLayout, R.id.details_image);
//        final TextView title = Views.find(detailsLayout, R.id.details_title);
//        final TextView description = Views.find(detailsLayout, R.id.details_text);
//        detailsLayout = Views.find(coverView,R.id.details_layout);
//        detailsLayout.setVisibility(View.INVISIBLE);
//        final TextView newtxt = Views.find(detailsLayout, R.id.details_title);
//        String imageUri = "drawable://" + R.drawable.demo;
//        // DisplayImageOptions options = new DisplayImageOptions.Builder()
//        // .cacheOnDisk(true)
//        //.build();
//        //imgloader.init(ImageLoaderConfiguration
//        //.createDefault(coverView.getContext()));
//        //imgloader.displayImage(image);
//        //  image.setImageURI(Uri.parse(post.imagePath));
//        //ImageLoader.loadPaintingImage(image, post);
//        title.setText(post.getTitle());
//
//        SpannableBuilder builder = new SpannableBuilder(coverView.getContext());
//        builder
//                .createStyle().setFont(Typeface.DEFAULT_BOLD).apply()
//                .append(R.string.year).append(": ")
//                .clearStyle()
//                .append(post.getAuthor()).append("\n")
//                .createStyle().setFont(Typeface.DEFAULT_BOLD).apply()
//                .append(R.string.location).append(": ")
//                .clearStyle()
//                .append(post.getLocation());
//        description.setText(builder.build());
//
//        unfoldableView.unfold(coverView, detailsLayout);
//    }




