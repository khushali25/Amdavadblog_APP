package com.example.xps.amdavadblog_app;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.iid.FirebaseInstanceId;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.apache.commons.lang3.StringEscapeUtils;

import java.io.IOException;

import Core.Helper.ApiService;
import Core.Helper.SynchronousCallAdapterFactory;
import Helper.PrefService;
import Helper.SocialMethod;
import Model.NotificationDataClass;
import Model.Post;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NotificationClickActivity extends AppCompatActivity {

    private static final String TAG = "UnfoldabelActivity" ;
    Retrofit retrofitallpost;
    private ImageView postfeaturedimage;
    CollapsingToolbarLayout ctl;
    private AdView adView;
    public ImageLoader AppImageLoader;
    FloatingActionButton floatingActionButton1,floatingActionButton2,floatingActionButton3,floatingActionButton4;
    FloatingActionMenu materialDesignFAM;
    String img1;
    RelativeLayout relative;
    Snackbar snackbar;
    String title,link,date,author,content,category;
    ImageView imgview;
    public ImageLoader getAppImageLoader() {
        return AppImageLoader;
    }
    public void setAppImageLoader(ImageLoader appImageLoader) {
        AppImageLoader = appImageLoader;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);

            setContentView(R.layout.activity_notification_click);

            Bundle param = new Bundle();
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_notification);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);

            retrofitallpost = new Retrofit.Builder()
                    .baseUrl("http://api.amdavadblog.com/amdblog/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(SynchronousCallAdapterFactory.create())
                    .build();

            relative = (RelativeLayout) findViewById(R.id.relativenotification);
            postfeaturedimage = (ImageView) findViewById(R.id.imgpostnotification);
            AppImageLoader = ImageLoader.getInstance();
            if (!AppImageLoader.isInited()) {
                AppImageLoader.init(ImageLoaderConfiguration
                        .createDefault(NotificationClickActivity.this));
            }
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
            getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                            + WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                            +WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
            );
            materialDesignFAM = (FloatingActionMenu) findViewById(R.id.material_design_android_floating_action_menu_noti);
            floatingActionButton1 = (FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_item1_noti);
            floatingActionButton2 = (FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_item2_noti);
            floatingActionButton3 = (FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_item3_noti);
            floatingActionButton4 = (FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_item4_noti);
            materialDesignFAM.setClosedOnTouchOutside(true);
            floatingActionButton1.setColorNormal(Color.WHITE);
            floatingActionButton1.setColorPressed(Color.DKGRAY);
            floatingActionButton2.setColorNormal(Color.WHITE);
            floatingActionButton2.setColorPressed(Color.DKGRAY);
            floatingActionButton3.setColorNormal(Color.WHITE);
            floatingActionButton3.setColorPressed(Color.DKGRAY);
            floatingActionButton4.setColorNormal(Color.WHITE);
            floatingActionButton4.setColorPressed(Color.DKGRAY);
            floatingActionButton1.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    //TODO something when floating action menu first item clicked
                    PrefService ap = new PrefService(getApplication());
                    String subscribed = ap.getAccessKey("subscribe");
                    if (subscribed == "") {
                        SocialMethod.showSubscription(NotificationClickActivity.this);
                    } else {
                        SocialMethod.alreadySubscribed(NotificationClickActivity.this);
                    }
                }
            });
            floatingActionButton2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    //TODO something when floating action menu second item clicked
                    SocialMethod.showFeedback(NotificationClickActivity.this);

                }
            });
            floatingActionButton3.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    //TODO something when floating action menu third item clicked
                    SocialMethod.showRateApp(NotificationClickActivity.this);

                }
            });
            floatingActionButton4.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    //TODO something when floating action menu third item clicked
                    shareBlog();
                }
            });
            int id = this.getIntent().getIntExtra("BlogId", 0);

                GetBlogDetailsById(id);




            ctl = findViewById(R.id.collapsetoolbarnotification);
//            final String title = this.getIntent().getStringExtra("Title");
//            final String decodedtitle = StringEscapeUtils.unescapeHtml3(title);
            ctl.setTitle(" ");

            AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.applayoutnotification);
            appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
                boolean isShow = false;
                int scrollRange = -1;

                @Override
                public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
                    if (scrollRange == -1) {
                        scrollRange = appBarLayout.getTotalScrollRange();
                    }
                    if (scrollRange + i == 0) {
                        ctl.setTitle(title);

                        ctl.setExpandedTitleTextAppearance(R.style.CollapsedAppBar);
                        ctl.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
                        isShow = true;
                    } else if (isShow) {
                        ctl.setTitle(" ");
                        isShow = false;
                    }
                }
            });
            InitializeAds();
            GetFragment();

        }
        catch (Exception ex)
        {
            Crashlytics.logException(ex);
        }
    }

    private void GetBlogDetailsById(int id) {
        Call<NotificationDataClass> call = null;
        final ApiService apiService = retrofitallpost.create(ApiService.class);

        call = apiService.getPostDetailById(id);
        Response<NotificationDataClass> response = null;
        try {
            response = call.execute();
        } catch (IOException e) {
            Crashlytics.logException(e);
            e.printStackTrace();
        }
        if (response.isSuccessful()) {

                    Post Postdata = response.body().getData();
                    title = Postdata.getTitle();
                    author =  Postdata.getAuthor().getName();
                    img1 = Postdata.getFeaturedMedia().getURL();
                    category = Postdata.getCategory().getName();
                    date = Postdata.getDate();
                    content = Postdata.getContent();
                    link = Postdata.getLink();
                    GetImage();
                }
    }

    private void GetImage() {
        try {
            if (isNetworkConnected()) {

                AppImageLoader = ImageLoader.getInstance();
                DisplayImageOptions opts = new DisplayImageOptions.Builder().imageScaleType(ImageScaleType.NONE).build();
                AppImageLoader.displayImage(img1, postfeaturedimage, opts);
                Target target;
                target = new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                            @Override
                            public void onGenerated(@Nullable Palette palette) {
                                Palette.Swatch darkMutedSwatch = palette.getDarkMutedSwatch();
                                if (darkMutedSwatch == null) {
                                    Palette.Swatch mutedSwatch = palette.getLightMutedSwatch();
                                    materialDesignFAM.setMenuButtonColorNormal(mutedSwatch.getRgb());
                                } else {
                                    materialDesignFAM.setMenuButtonColorNormal(darkMutedSwatch.getRgb());
                                }
                            }
                        });
                    }

                    @Override
                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                        Crashlytics.logException(e);

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                };
                Picasso.get()
                        .load(img1).into(target);
                postfeaturedimage.setTag(target);
            }
            else {snackbarerror();}
        }
        catch (Exception ex)
        {
            Crashlytics.logException(ex);
        }

    }
    @Override
    protected void onPause() {
        super.onPause();
        //adView.pause();
    }
    @Override
    protected void onResume() {
        super.onResume();
      //  adView.resume();
    }
    private void GetFragment() {
        try {
            if (isNetworkConnected()) {
                android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                Bundle bundle = new Bundle();

                bundle.putString("Title", title );
                bundle.putString("Date", date );
                bundle.putString("Content", content );
                bundle.putString("Link", link );
                bundle.putString("Category",category);
                bundle.putString("Author",author);
                bundle.putString("Image",img1);

                NotificationClickFragment fragnotifi = new NotificationClickFragment();
                fragnotifi.setArguments(bundle);
                ft.replace(R.id.frcontentnoti,fragnotifi);
                ft.commit();

            } else {
                snackbarerror();
            }
        }
        catch (Exception ex)
        {
            Crashlytics.logException(ex);
        }
    }
    public void onBackPressed() {
        try {
            if(isNetworkConnected()) {
//                String flag = this.getIntent().getStringExtra("I_CAME_FROM");
//                if (flag.equals("searchactivity")) {
//                    //you came from main activity
//                    finish();
                     startActivity(new Intent(this, MainNavigationActivity.class));
//                } else {
//                    finish();
//                    //android.support.v4.app.NavUtils.navigateUpFromSameTask(this);
//                    //startActivity(new Intent(this, MainNavigationActivity.class));
//                }
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
        try {
            if(isNetworkConnected()) {
                Bundle param = new Bundle();
                param.putString("name", String.valueOf(item.getItemId()));
                android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                switch (item.getItemId()) {

                    case android.R.id.home:
                        finish();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    public void shareBlog() {
        try {
            if(isNetworkConnected()) {
               // int id = this.getIntent().getIntExtra("BlogId", 0);
               // String link = this.getIntent().getStringExtra("link");
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, link);
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, "Share Blog via"));
            }
            else {snackbarerror();}
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

            adView = (AdView)findViewById(R.id.adViewnoti);
            AdRequest adRequest = new AdRequest.Builder().addTestDevice("EB38215ED85EFA82E937126940E5C31F").build();
            adView.loadAd(adRequest);
        }
        catch (Exception ex)
        {
            Crashlytics.logException(ex);
            android.util.Log.e("Initialize UI failed", ex.getMessage());
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
                    .make(relative, "No internet connection!", Snackbar.LENGTH_INDEFINITE)
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
