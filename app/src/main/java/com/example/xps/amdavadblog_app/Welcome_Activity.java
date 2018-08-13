package com.example.xps.amdavadblog_app;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import Adapter.IntroScreenAdapter;
import Helper.IntroPageTransformer;
import services.PrefService;

public class Welcome_Activity extends AppCompatActivity  {
    private ViewPager mViewPager;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private int[] layouts;
    private Button btnSkip, btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_welcome);

        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);
        btnSkip = (Button) findViewById(R.id.btn_skip);
        btnNext = (Button) findViewById(R.id.btn_next);


        // Set an Adapter on the ViewPager
        mViewPager.setAdapter(new IntroScreenAdapter(getSupportFragmentManager()));

        // Set a PageTransformer
        mViewPager.setPageTransformer(false, new IntroPageTransformer());

        layouts = new int[]{
                R.layout.introscreenfragmentlayout_1,
                R.layout.introscreenfragmentlayout_2,
                R.layout.introscreenfragmentlayout_3,
                };

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchHomeScreen();
            }


        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // checking for last page
                // if last page home screen will be launched
                int current = getItem(+1);
                if (current < layouts.length) {
                    // move to next screen
                    mViewPager.setCurrentItem(current);
                } else {
                    launchHomeScreen();
                }
            }
        });
    }
    private void launchHomeScreen() {
        // prefManager.setFirstTimeLaunch(false);
        startActivity(new Intent(this, MainNavigationActivity.class));
        finish();
    }
    private int getItem(int i) {
        return mViewPager.getCurrentItem() + i;
    }
}