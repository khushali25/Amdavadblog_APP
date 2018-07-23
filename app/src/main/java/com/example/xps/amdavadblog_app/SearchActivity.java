package com.example.xps.amdavadblog_app;

import android.content.ClipData;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alexvasilkov.foldablelayout.FoldableListLayout;

import java.util.List;

import Adapter.PostContentAdapter;
import Model.Post;

public class SearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    android.support.v7.widget.SearchView searchView;
    private PostContentAdapter postContentAdapter;
    ImageView imageView;
    private TextView txtnotfound;
    FoldableListLayout foldableListLayout;
    String searchText;
    android.graphics.drawable.AnimationDrawable animation;
    private RecyclerView.LayoutManager layoutManager;
    private List<Post> posts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarSearch);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);
       // imageView = findViewById(R.id.searchloading);
       // txtnotfound = findViewById(R.id.txtnodatafound);
        //recyclerView = findViewById(R.id.searchpostrecycler);
       // recyclerView.hasFixedSize();
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        foldableListLayout = findViewById(R.id.foldable_list_search);
       // postContentAdapter = new PostContentAdapter(this);
       // foldableListLayout.setAdapter(postContentAdapter);
        UpdateView();
    }

    private void UpdateView() {
        if (postContentAdapter != null)
        {
            postContentAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.searchitem, menu);

        try
        {
            searchView = (SearchView) menu.findItem(R.id.action_search1).getActionView();
            foldableListLayout.setVisibility(View.GONE);
        }
        catch (Exception exx)
        {
           // Log.ERROR("Initialize UI failed", exx.getMessage());
        }
        searchView.setQueryHint("Type something...");
        View searchPlate = searchView.findViewById(R.id.search_plate);
        if (searchPlate != null)
        {
            TextView searchText = searchPlate.findViewById(R.id.search_src_text);
            searchText.setTextColor(Color.WHITE);
            searchText.setHintTextColor(Color.WHITE);
        }
        searchView.setIconifiedByDefault(false);
        onSearchRequested();
        searchView.requestFocus();
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        searchText = s;
//        imageView.setVisibility(View.VISIBLE);
      //  animation = (android.graphics.drawable.AnimationDrawable)imageView.getDrawable();

      //  animation.start();
        // CacheService.ClearAllCache();

      //  InitializeBackgroundWorker();
      //  backgroundworker1.RunWorkerAsync();

        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
       // imageView.setVisibility(View.GONE);
        foldableListLayout.setVisibility(View.GONE);
      //  txtnotfound.setVisibility(View.GONE);
        return false;
    }
}
