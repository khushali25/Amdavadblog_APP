package com.example.xps.amdavadblog_app;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.alexvasilkov.android.commons.adapters.ItemsAdapter;
import com.alexvasilkov.android.commons.ui.Views;
import com.alexvasilkov.foldablelayout.FoldableListLayout;
import com.alexvasilkov.foldablelayout.UnfoldableView;

import java.util.ArrayList;
import java.util.List;

import Adapter.PostContentAdapter;

public class MainActivity extends AppCompatActivity {
    private String appTitle;
    private TextView txt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InitializeFirstScreenUI();
    }

    private void InitializeFirstScreenUI() {
        try
        {
            txt = findViewById(R.id.toolbartxt);
            this.appTitle = (String) this.getTitle();
            txt.setText(appTitle);

            android.support.v7.widget.Toolbar tool1 = findViewById(R.id.newtoolbar);
            setSupportActionBar(tool1); // for set actionbar title
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getStatusBarHeight();
//            getSupportActionBar().setHomeButtonEnabled(true);
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


    private int getStatusBarHeight()
    {
        int result = 0;
        int resourceId = MainActivity.this.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0)
        {
            result = MainActivity.this.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

}
