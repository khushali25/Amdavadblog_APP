package com.example.xps.amdavadblog_app;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
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
import Helper.PrefService;
import Helper.SocialMethod;

public class UnfoldableDetailsActivity extends AppCompatActivity {

    private static final String TAG = "UnfoldabelActivity" ;
    private ImageView postfeaturedimage;
    CollapsingToolbarLayout ctl;
    private AdView adView;
    public ImageLoader AppImageLoader;
    FloatingActionButton floatingActionButton1,floatingActionButton2,floatingActionButton3,floatingActionButton4;
    FloatingActionMenu materialDesignFAM;
    String img1;
    public ImageLoader getAppImageLoader() {
        return AppImageLoader;
    }
    public void setAppImageLoader(ImageLoader appImageLoader) {
        AppImageLoader = appImageLoader;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_unfoldable_details);

        Bundle param = new Bundle();
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
    }
    private void GetBlogDetails() {

            String refreshedToken = FirebaseInstanceId.getInstance().getToken();
            Log.d(TAG, "Refreshed token: " + refreshedToken);
            int id = this.getIntent().getIntExtra("BlogId", 0);
            AppImageLoader = ImageLoader.getInstance();
            DisplayImageOptions opts = new DisplayImageOptions.Builder().imageScaleType(ImageScaleType.NONE).build();
            img1 = this.getIntent().getStringExtra("Image");
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
                            }
                            else {
                                materialDesignFAM.setMenuButtonColorNormal(darkMutedSwatch.getRgb());
                            }
                        }
                    });
                }
                @Override
                public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                }
                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            };
            Picasso.get()
                    .load(img1).into(target);
            postfeaturedimage.setTag(target);

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

    private void InitializeAds() {
        try
        {
            MobileAds.initialize(this, "ca-app-pub-1870433400625480~7602115204");

            adView = (AdView)findViewById(R.id.adView2);
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
