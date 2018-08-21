package com.example.xps.amdavadblog_app;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
//import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;

import Helper.PrefService;
import Helper.SocialMethod;

import static android.view.View.VISIBLE;

public class UnfoldableDetailsActivity extends AppCompatActivity {

    private static final String TAG = "UnfoldabelActivity" ;
    private ImageView postfeaturedimage;
    TextView showResult;

    ImageView imageView;
    CollapsingToolbarLayout ctl;
    AppBarLayout appBarLayout;
    private AdView adView;
    private static boolean isFabOpen;
    private View bgFabMenu;
    public Dialog dialog;

    public ImageLoader getAppImageLoader() {
        return AppImageLoader;
    }

    public void setAppImageLoader(ImageLoader appImageLoader) {
        AppImageLoader = appImageLoader;
    }

    public ImageLoader AppImageLoader;
    FloatingActionButton floatingActionButton,floatingActionButton1,floatingActionButton2,floatingActionButton3,floatingActionButton4;
    FloatingActionMenu materialDesignFAM;
    String img1;
    private Target loadtarget;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_unfoldable_details);
        Bundle param = new Bundle();
//        param.PutString("name", Title);
//        EventServices.Instance.GenericEvent(EventType.PostDetailActivityStart, param);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_itemdetail);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
        postfeaturedimage = (ImageView)findViewById(R.id.imgpost);
        AppImageLoader =  ImageLoader.getInstance();
        if (!AppImageLoader.isInited()) {
            AppImageLoader.init(ImageLoaderConfiguration
                    .createDefault(UnfoldableDetailsActivity.this));
        }

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                        + WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        + WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
        );

        materialDesignFAM = (FloatingActionMenu) findViewById(R.id.material_design_android_floating_action_menu);
        floatingActionButton1 = (FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_item1);
        floatingActionButton2 = (FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_item2);
        floatingActionButton3 = (FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_item3);
        floatingActionButton4 = (FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_item4);
        materialDesignFAM.setClosedOnTouchOutside(true);
        floatingActionButton1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //TODO something when floating action menu first item clicked
                PrefService ap = new PrefService(getApplication());
                String subscribed = ap.getAccessKey("subscribe");
                if (subscribed == "")
                {
                    SocialMethod.showSubscription(UnfoldableDetailsActivity.this);
                }
                else
                {
                    SocialMethod.alreadySubscribed(UnfoldableDetailsActivity.this);
                }
            }
        });
        floatingActionButton2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //TODO something when floating action menu second item clicked
                SocialMethod.showFeedback(UnfoldableDetailsActivity.this);

            }
        });
        floatingActionButton3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //TODO something when floating action menu third item clicked
                SocialMethod.showRateApp(UnfoldableDetailsActivity.this);

            }
        });
        floatingActionButton4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //TODO something when floating action menu third item clicked
                SocialMethod.showRateApp(UnfoldableDetailsActivity.this);
                shareBlog();
            }
        });

        ctl = findViewById(R.id.collapsetoolbar);
        final String title = this.getIntent().getStringExtra("Title");
        ctl.setTitle(" ");

        AppBarLayout appBarLayout = (AppBarLayout)findViewById(R.id.applayout);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
             boolean isShow = false;
             int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {

                if (scrollRange == -1)
                {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + i == 0)
                {
                    ctl.setTitle(title);

                    ctl.setExpandedTitleTextAppearance(R.style.CollapsedAppBar);
                    ctl.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
                    isShow = true;
                }
                else if (isShow)
                {
                    ctl.setTitle(" ");
                    isShow = false;
                }
             }
    });
            InitializeAds();
            GetBlogDetails();
            GetFragment();
            //EventServices.Instance.GenericEvent(EventType.PostDetailActivityComplete, param);
    }

    private void GetBlogDetails() {

            String refreshedToken = FirebaseInstanceId.getInstance().getToken();
            Log.d(TAG, "Refreshed token: " + refreshedToken);
            int id = this.getIntent().getIntExtra("BlogId", 0);
            //if (!AppImageLoader.isInited()) {
            AppImageLoader = ImageLoader.getInstance();
            // }
            DisplayImageOptions opts = new DisplayImageOptions.Builder().imageScaleType(ImageScaleType.NONE).build();
            img1 = this.getIntent().getStringExtra("Image");
            AppImageLoader.displayImage(img1, postfeaturedimage, opts);

//            Target target;
//            target = new Target() {
//                @Override
//                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
//                    Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
//                        @Override
//                        public void onGenerated(@Nullable Palette palette) {
//                            Palette.Swatch lightVibrantSwatch = palette.getLightVibrantSwatch();
//                            if (lightVibrantSwatch == null) {
//                                Palette.Swatch mutedSwatch = palette.getLightMutedSwatch();
//                                getWindow().setStatusBarColor(mutedSwatch.getRgb());
//                                floatingActionButton1.setColorNormal(mutedSwatch.getRgb());
//                                floatingActionButton2.setColorNormal(mutedSwatch.getRgb());
//                                floatingActionButton3.setColorNormal(mutedSwatch.getRgb());
//                                floatingActionButton4.setColorNormal(mutedSwatch.getRgb());
//                                //materialDesignFAM.setBackgroundColor(mutedSwatch.getRgb());
//                            }
//                            //int lightVibrant = palette.getLightVibrantSwatch().getRgb();
//                            else {
//                                getWindow().setStatusBarColor(lightVibrantSwatch.getRgb());
//                                floatingActionButton1.setColorNormal(lightVibrantSwatch.getRgb());
//                                floatingActionButton2.setColorNormal(lightVibrantSwatch.getRgb());
//                                floatingActionButton3.setColorNormal(lightVibrantSwatch.getRgb());
//                                floatingActionButton4.setColorNormal(lightVibrantSwatch.getRgb());
//                                //materialDesignFAM.setBackgroundColor(lightVibrantSwatch.getRgb());
//                            }
//                        }
//                    });
//                }
//
//                @Override
//                public void onBitmapFailed(Exception e, Drawable errorDrawable) {
//
//                }
//
//                @Override
//                public void onPrepareLoad(Drawable placeHolderDrawable) {
//
//                }
//            };
//            Picasso.get()
//                    .load(img1).into(target);
//            postfeaturedimage.setTag(target);
//
       }
            @Override
            protected void onPause() {
                super.onPause();
                adView.pause();
            }
            @Override
            protected void onResume() {
                super.onResume();
                adView.resume();
            }
            private void GetFragment() {
                android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.frcontent, new UnfoldableDetailsFragment());
                ft.commit();
            }

            public void onBackPressed() {
                android.support.v4.app.NavUtils.navigateUpFromSameTask(this);
            }

            @Override
            public boolean onOptionsItemSelected(MenuItem item) {
                Bundle param = new Bundle();
                param.putString("name", String.valueOf(item.getItemId()));
                //EventServices.Instance.GenericEvent(EventType.MenuItemSelect, param);

                android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                switch (item.getItemId()) {

                    case android.R.id.home:
                          android.support.v4.app.NavUtils.navigateUpFromSameTask(this);
                        return true;
                }
                return super.onOptionsItemSelected(item);
            }

            @Override
            public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.ActionBarMenuItemForPostDetail, menu);
                return true;
            }

            public void shareBlog() {
                int id = this.getIntent().getIntExtra("BlogId", 0);

                String title = this.getIntent().getStringExtra("Title");
                String replacedtitle = title.replace(" ", "-");
                String finaltitle = replacedtitle.toLowerCase();
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_COMPONENT_NAME, "http://amdavadblog.com" + finaltitle);
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, "Share Blog via"));
            }

      private class FabAnimatorListener implements Animator.AnimatorListener {
        View bgfabmenu;
        FloatingActionButton fab1;
        FloatingActionButton fab2;
        FloatingActionButton fab3;
        FloatingActionButton fab4;
        public FabAnimatorListener(View bgFabMenu, FloatingActionButton fabcontainer1, FloatingActionButton fabcontainer2, FloatingActionButton fabcontainer3, FloatingActionButton fabcontainer4, TextView subscribetxt, TextView feedbacktxt, TextView rateapptxt, TextView shareblogtxt) {
            this.bgfabmenu = bgFabMenu;
            fab1 = fabcontainer1;
            fab2 = fabcontainer2;
            fab3 = fabcontainer3;
            fab4 = fabcontainer4;
        }
        @Override
        public void onAnimationStart(Animator animator) {

        }

        @SuppressLint("RestrictedApi")
        @Override
        public void onAnimationEnd(Animator animator) {
            if (!isFabOpen) {
                //  for (View view : bgfabmenu)
                fab1.setVisibility(View.GONE);
                fab2.setVisibility(View.GONE);
                fab3.setVisibility(View.GONE);
                fab4.setVisibility(View.GONE);
            }
        }

        @Override
        public void onAnimationCancel(Animator animator) {

        }

        @Override
        public void onAnimationRepeat(Animator animator) {

        }
    }
    private void InitializeAds() {
        try
        {
            MobileAds.initialize(this, "ca-app-pub-1870433400625480~7602115204");

            adView = (AdView)findViewById(R.id.adView2);
            //adView.AdSize = AdSize.SmartBanner;
            //adView.AdUnitId = "ca-app-pub-1870433400625480/7574195045";
            // AdRequest adRequest = new AdRequest.Builder().Build();

            AdRequest adRequest = new AdRequest.Builder().addTestDevice("EB38215ED85EFA82E937126940E5C31F").build();
            adView.loadAd(adRequest);
        }
        catch (Exception ex)
        {
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
}
