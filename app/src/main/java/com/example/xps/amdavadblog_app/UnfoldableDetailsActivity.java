package com.example.xps.amdavadblog_app;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import static android.view.View.VISIBLE;

public class UnfoldableDetailsActivity extends AppCompatActivity {

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
    FloatingActionButton fabcontainer;
    FloatingActionButton fabcontainer1;
    FloatingActionButton fabcontainer2;
    FloatingActionButton fabcontainer3;
    FloatingActionButton fabcontainer4;

    TextView subscribetxt;
    TextView feedbacktxt;
    TextView rateapptxt;
    TextView shareblogtxt;
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
        //getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);
        postfeaturedimage = (ImageView)findViewById(R.id.imgpost);
         
        bgFabMenu = (View)findViewById(R.id.bgfabmenu);
        subscribetxt = findViewById(R.id.txtsubscribe);
        feedbacktxt = findViewById(R.id.txtfeedback);
        rateapptxt = findViewById(R.id.txtrateapp);
        shareblogtxt = findViewById(R.id.txtxshareblog);
        fabcontainer = findViewById(R.id.floatingbtn);
        fabcontainer1 = findViewById(R.id.fabbtn1);
        fabcontainer2 = findViewById(R.id.fabbtn2);
        fabcontainer3 = findViewById(R.id.fabbtn3);
        fabcontainer4 = findViewById(R.id.fabbtn4);
        fabcontainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isFabOpen)
                    ShowFabMenu();

                else
                    CloseFabMenu();
            }
        });
        fabcontainer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CloseFabMenu();
//              //  Android.Content.Context mContext = Android.App.Application.Context;
//                //Services.PrefService ap = new Services.PrefService(mContext);
//                //String subscribed = ap.getAccessKey(AppConstants.EmailKey);
//              //  if (subscribed == "")
//                {
//                    //SocialMethod.showSubscription(this);
//                }
//                else
//                {
//                   // SocialMethod.alreadySubscribed(this);
//                }
            }
        });
        fabcontainer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CloseFabMenu();
               // SocialMethod.showFeedback(this);
            }
        });
        fabcontainer3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CloseFabMenu();
                //SocialMethod.showRateApp(this);
            }
        });
        fabcontainer4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CloseFabMenu();
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
    @SuppressLint("RestrictedApi")
    private void ShowFabMenu()
    {
        isFabOpen = true;
        fabcontainer1.setVisibility(VISIBLE);
        //fabcontainer1.requestLayout();
        fabcontainer2.setVisibility(VISIBLE);
        fabcontainer3.setVisibility(VISIBLE);
        fabcontainer4.setVisibility(VISIBLE);

        subscribetxt.setVisibility(VISIBLE);
        feedbacktxt.setVisibility(VISIBLE);
        rateapptxt.setVisibility(VISIBLE);
        shareblogtxt.setVisibility(VISIBLE);
        bgFabMenu.setVisibility(VISIBLE);

        fabcontainer.animate().rotation(135f);
        bgFabMenu.animate().alpha(0.5f);

        fabcontainer1.animate()
                .translationY(-getResources().getDimension(R.dimen.standard_190))
                .rotation(0f);
        fabcontainer2.animate()
                .translationY(-getResources().getDimension(R.dimen.standard_145))
                .rotation(0f);
        fabcontainer3.animate()
                .translationY(-getResources().getDimension(R.dimen.standard_100))
                .rotation(0f);
        fabcontainer4.animate()
                .translationY(-getResources().getDimension(R.dimen.standard_55))
                .rotation(0f);
    }

    private void CloseFabMenu()
    {
        isFabOpen = false;
        fabcontainer.animate().rotation(0f);
        bgFabMenu.animate().alpha(0f);
        subscribetxt.setVisibility(View.GONE);
        feedbacktxt.setVisibility(View.GONE);
        rateapptxt.setVisibility(View.GONE);
        shareblogtxt.setVisibility(View.GONE);

        fabcontainer1.animate().translationY(0f)
                .rotation(90f);
        fabcontainer2.animate()
                .translationY(0f)
                .rotation(90f);
        fabcontainer3.animate()
                .translationY(0f)
                .rotation(90f);
        fabcontainer4.animate()
                .translationY(0f)
                .rotation(90f)
                .setListener(new FabAnimatorListener(bgFabMenu, fabcontainer1, fabcontainer2, fabcontainer3, fabcontainer4, subscribetxt,feedbacktxt, rateapptxt, shareblogtxt));
    }
    private void GetBlogDetails() {
        int id = this.getIntent().getIntExtra("BlogId", 0);
        //titleTextView.Text = title;
        AppImageLoader = ImageLoader.getInstance();
        DisplayImageOptions opts = new DisplayImageOptions.Builder().imageScaleType(ImageScaleType.NONE).build();
        img1 = this.getIntent().getStringExtra("Image");
       // int productImageId = getResources().getIdentifier(img1,"string", getPackageName());
      //  img1.getDrawable();
        loadBitmap(R.drawable.demo);
        //postfeaturedimage.setTag(loadtarget);
        AppImageLoader.displayImage(img1, postfeaturedimage, opts);

    }

    private void loadBitmap(int demo) {
//        if (loadtarget == null)
       loadtarget = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                    @Override
                    public void onGenerated(@Nullable Palette palette) {
                        if (palette == null)
                            return;
                        if (palette.getLightVibrantSwatch() != null) {
                            int lightVibrant = palette.getLightVibrantSwatch().getRgb();
                            //palette lightVibrant = new android.graphics.Color(palette.getLightVibrantSwatch().getRgb());
                            //  Palette.Swatch lightVibrant = palette.getLightVibrantSwatch();
                            // int color = new android.graphics.Color(lightVibrant);

                            if ((int) Build.VERSION.SDK_INT >= 21)
                                getWindow().setStatusBarColor(lightVibrant);
                            fabcontainer1.setBackgroundTintList(android.content.res.ColorStateList.valueOf(lightVibrant));
                            fabcontainer2.setBackgroundTintList(android.content.res.ColorStateList.valueOf(lightVibrant));
                            fabcontainer3.setBackgroundTintList(android.content.res.ColorStateList.valueOf(lightVibrant));
                            fabcontainer4.setBackgroundTintList(android.content.res.ColorStateList.valueOf(lightVibrant));
                            fabcontainer.setBackgroundTintList(android.content.res.ColorStateList.valueOf(lightVibrant));
                        }
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(30, 30);
                        //Loop through each of the palettes available
                        //and put them as a small square
                        for (Palette.Swatch p : palette.getSwatches()) {
                            if (p == null)
                                continue;
                            View view = new View(getApplicationContext());
                            view.setBackgroundColor(p.getRgb());
                            view.getLayoutParams();
                            //Android.Graphics.Color.AddView(view, 0);
                        }
                    }
                });
            }
            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                android.util.Log.e("Initialize UI failed", e.getMessage());
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
        Picasso.get().load(demo).into(loadtarget);
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

    private void GetFragment()
    {
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frcontent, new UnfoldableDetailsFragment());
        ft.commit();
    }
    public void onBackPressed()
    {
        android.support.v4.app.NavUtils.navigateUpFromSameTask(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Bundle param = new Bundle();
        param.putString("name", String.valueOf(item.getItemId()));
        //EventServices.Instance.GenericEvent(EventType.MenuItemSelect, param);

        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        switch (item.getItemId())
        {
           // case R.id.shareApp:
              //  SocialMethod.shareIt(this);
              //  return true;

            case android.R.id.home:
              //  android.support.v4.app.NavUtils.navigateUpFromSameTask(this);
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

    public void shareBlog()
    {
        int id = this.getIntent().getIntExtra("BlogId", 0);
        String title = this.getIntent().getStringExtra("Title");
        String replacedtitle = title.replace(" ", "-");
        String finaltitle = replacedtitle.toLowerCase();
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_COMPONENT_NAME, "http://amdavadblogs.apps-1and1.com/en/" + finaltitle);
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
    }
}
