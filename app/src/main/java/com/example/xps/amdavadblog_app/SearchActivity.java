package com.example.xps.amdavadblog_app;

import android.app.Activity;
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
import Adapter.PostContentAdapterSearch;
import Core.WordPressService;
import Model.Author;
import Model.Category;
import Model.Media;
import Model.PoseSearch;
import Model.Post;
import Model.SynchronousCallAdapterFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchActivity extends AppCompatActivity {

    android.support.v7.widget.SearchView searchView;
    private PostContentAdapterSearch postContentAdapter;
    ImageView imageView;
    private TextView txtnotfound;
    FoldableListLayout foldableListLayout;
    String searchText;
    android.graphics.drawable.AnimationDrawable animation;
    private RecyclerView.LayoutManager layoutManager;
    private List<PoseSearch> posts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarSearch);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
        // imageView = findViewById(R.id.searchloading);
        txtnotfound = findViewById(R.id.txtnodatafound);
        //recyclerView = findViewById(R.id.searchpostrecycler);
        // recyclerView.hasFixedSize();
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        foldableListLayout = findViewById(R.id.foldable_list_search);
        postContentAdapter = new PostContentAdapterSearch(posts, this);
        foldableListLayout.setAdapter(postContentAdapter);
        UpdateView();
    }

    private void UpdateView() {
        if (postContentAdapter != null) {
            postContentAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.searchitem, menu);
        try {
            searchView = (SearchView) menu.findItem(R.id.action_search1).getActionView();
            ImageView searchClose = searchView.findViewById(android.support.v7.appcompat.R.id.search_close_btn);
            searchClose.setImageResource(R.drawable.ic_close_white_24dp);
            ImageView searchIcon = searchView.findViewById(android.support.v7.appcompat.R.id.search_mag_icon);
            searchIcon.setImageResource(R.drawable.ic_search_black_24dp);

            foldableListLayout.setVisibility(View.GONE);
        } catch (Exception exx) {
            // Log.ERROR("Initialize UI failed", exx.getMessage());
        }
        searchView.setQueryHint("Type something...");
        View searchPlate = searchView.findViewById(R.id.search_plate);
        if (searchPlate != null) {
            TextView searchText = searchPlate.findViewById(R.id.search_src_text);
            searchText.setTextColor(Color.WHITE);
            searchText.setHintTextColor(Color.WHITE);
        }
        searchView.setIconifiedByDefault(false);
        //searchView.setSubmitButtonEnabled(true);
        onSearchRequested();
        searchView.requestFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
       @Override
       public boolean onQueryTextSubmit(String s) {
                searchText = s;
        //imageView.Visibility = ViewStates.Visible;
        //  animation = (android.graphics.Drawables.AnimationDrawable)imageView.Drawable;
        //  animation.Start();
        Retrofit retrofitallpost = new Retrofit.Builder()
                .baseUrl("https://amdavadblog.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(SynchronousCallAdapterFactory.create())
                .build();

           final WordPressService wordPressService = retrofitallpost.create(WordPressService.class);
           Call<List<PoseSearch>> call = null;
           call = wordPressService.getPostsByQuerySearch(searchText);
           call.enqueue(new Callback<List<PoseSearch>>() {
               @Override
               public void onResponse(Call<List<PoseSearch>> call, Response<List<PoseSearch>> response) {
                   Media resp2 = null;
                   Author Auth = null;
                   Category category = null;
                   if (response.isSuccessful()) {
                       int postsize = response.body().size();
                       if (postsize != 0) {
                   for (final PoseSearch post1 : response.body()) {
                           Integer catId = post1.categories.get(0);
                           category = wordPressService.getPostCategoryById(catId);
                           post1.categoryname = category.getName();
                           foldableListLayout.setVisibility(View.VISIBLE);
                           Auth = wordPressService.getPostAuthorById(post1.author);
                           post1.authorname = Auth.getName();
                           resp2 = wordPressService.getFeaturedImageById(post1.featured_media);
                           if (resp2 == null) {
                               post1.imagePath = String.valueOf(R.drawable.ic_home_black_24dp);
                           } else
                               post1.imagePath = resp2.media_details.sizes.medium_large.source_url.toString();
                       }
                   } else {
                       foldableListLayout.setVisibility(View.GONE);
                       txtnotfound.setVisibility(View.VISIBLE);
                   }
               }
                postContentAdapter.setData1(response.body());
                postContentAdapter.notifyDataSetChanged();
           }
               @Override
               public void onFailure(Call<List<PoseSearch>> call, Throwable t) {
               }
           });
        return true;
        }

        @Override
        public boolean onQueryTextChange(String s)
        {
            foldableListLayout.setVisibility(View.GONE);
            txtnotfound.setVisibility(View.GONE);
            return false;
        }
        });
        //searchView.setOnQueryTextListener(this);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        //android.support.v4.app.NavUtils.navigateUpFromSameTask(this);
    }

//    @Override
//    public boolean onQueryTextSubmit(String s) {
//        return false;
//    }
//
//    @Override
//    public boolean onQueryTextChange(String s) {
//        return false;
//    }


    // @Override
//    public boolean onQueryTextSubmit(String s) {
//        searchText = s;
//        //imageView.Visibility = ViewStates.Visible;
//        //  animation = (android.graphics.Drawables.AnimationDrawable)imageView.Drawable;
//        //  animation.Start();
//        Retrofit retrofitallpost = new Retrofit.Builder()
//                .baseUrl("http://192.168.1.5:3000/amdblog/")
//                .addConverterFactory(GsonConverterFactory.create())
//                .addCallAdapterFactory(SynchronousCallAdapterFactory.create())
//                .build();
//
//        final WordPressService wordPressService = retrofitallpost.create(WordPressService.class);
//        Call<List<Post>> call = null;
//
//        call = wordPressService.getPostsByQuerySearch(searchText);
//        call.enqueue(new Callback<List<Post>>() {
//            @Override
//            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
//                Media resp2 = null;
//                Author Auth = null;
//                Category category = null;
//                if (response.isSuccessful()) {
//                    int postsize = response.body().size();
//                    if (postsize != 0) {
//                        for (final Post post : response.body()) {
//                            String date = post.getDate();
//                            String excerpt = post.getExcerpt();
//
//                            //Integer catId = post.categories.get(0);
//                            // category = wordPressService.getPostCategoryById(catId);
//                            // post.categoryname = category.name;
//                            foldableListLayout.setVisibility(View.VISIBLE);
//                            // Auth = wordPressService.getPostAuthorById(post.author);
//                            // post.authorname = Auth.name;
//                            //resp2 = wordPressService.getFeaturedImageById(post.featured_media);
//                            //if (resp2 == null) {
//                            //    post.imagePath = String.valueOf(R.drawable.ic_home_black_24dp);
//                            // } else
//                            //     post.imagePath = resp2.media_details.sizes.medium_large.source_url.toString();
//                        }
//                    } else {
//                        foldableListLayout.setVisibility(View.GONE);
//                        txtnotfound.setVisibility(View.VISIBLE);
//                    }
//                }
//                postContentAdapter.setData(response.body());
//                postContentAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onFailure(Call<List<Post>> call, Throwable t) {
//
//            }
//        });
//        return true;
//    }

 //   @Override
//    public boolean onQueryTextChange(String s) {
//       // imageView.setVisibility(View.GONE);
//        foldableListLayout.setVisibility(View.GONE);
//        txtnotfound.setVisibility(View.GONE);
//        return false;
//    }
}
