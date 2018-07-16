package com.example.xps.amdavadblog_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.ads.AdView;

import Helper.SocialMethod;

public class MainNavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView txt;
    String appTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.newtoolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        InitializeFirstScreenUI();
    }
    private void InitializeFirstScreenUI() {
        try
        {
             txt = findViewById(R.id.toolbartxt);
             this.appTitle = (String) this.getTitle();
            txt.setText(appTitle);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getStatusBarHeight();
            android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, new FoldableListFragment(), "Fragment1");
            ft.addToBackStack("AddFragment1");
            ft.commit();
        }
        catch (Exception ex)
        {
            android.util.Log.e("Initialize UI failed", ex.getMessage());
        }
    }
    private int getStatusBarHeight() {
        int result = 0;
        int resourceId = MainNavigationActivity.this.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0)
        {
            result = MainNavigationActivity.this.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout)findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
//        if (this.drawerToggle.OnOptionsItemSelected(item))
//            return true;
        int id = item.getItemId();

        switch (id) {
            case R.id.searchitem:
                Intent intent = new Intent(this,SearchActivity.class);
                startActivity(intent);
                return true;

            case R.id.subscribepost:
                //android.content.Context mContext = android.app.Application.Context;
                Services.PrefService ap = new Services.PrefService(getApplicationContext());
                //String subscribed = ap.getAccessKey(AppConstants.EmailKey);
               // if (subscribed == "") {
                    SocialMethod.showSubscription(this);
               // } else {
                  //  SocialMethod.alreadySubscribed(this);
               // }
                return true;
            case R.id.feedbackpost:
                SocialMethod.showFeedback(this);
                return true;
            case R.id.rateus:
                SocialMethod.showRateApp(this);
                return true;
            case R.id.shareApp:
                SocialMethod.shareIt(this);
                return true;
            case R.id.settings:
                Intent i = new Intent(this,SettingsActivity.class);
                startActivity(i);
                return true;

            //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
          }

            return super.onOptionsItemSelected(item);
        }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
