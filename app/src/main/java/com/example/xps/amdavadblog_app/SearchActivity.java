package com.example.xps.amdavadblog_app;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alexvasilkov.android.commons.nav.builders.EmailIntentBuilder;
import com.alexvasilkov.foldablelayout.FoldableListLayout;
import com.crashlytics.android.Crashlytics;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.List;
import Adapter.PostContentAdapterSearch;
import Core.Helper.ApiService;
import Helper.GifView;
import Model.Author;
import Model.Category;
import Model.Media;
import Model.PostSearch;
import Core.Helper.SynchronousCallAdapterFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchActivity extends AppCompatActivity {

    android.support.v7.widget.SearchView searchView;
    private PostContentAdapterSearch postContentAdapter;
    private TextView txtnotfound;
    FoldableListLayout foldableListLayout;
    String searchText;
    LinearLayoutManager layoutManager;
    List<PostSearch> posts;
    GifView gifView;
    Snackbar snackbar;
    LinearLayout linearsearch;
    AdView adview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);

                setContentView(R.layout.activity_search);
                Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarSearch);
                setSupportActionBar(toolbar);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
                linearsearch = findViewById(R.id.linearsearch);
                txtnotfound = findViewById(R.id.txtnodatafound);
                layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
                gifView = (GifView) findViewById(R.id.loading);
                foldableListLayout = findViewById(R.id.foldable_list_search);
                InitializeAds(this);
                postContentAdapter = new PostContentAdapterSearch(posts, this);
                foldableListLayout.setAdapter(postContentAdapter);
                UpdateView();


        } catch (Exception ex) {
            Crashlytics.logException(ex);
        }
    }

    private void InitializeAds(SearchActivity searchActivity) {
        try
        {
            MobileAds.initialize(searchActivity, "ca-app-pub-1870433400625480~7602115204");
            adview = findViewById(R.id.adViewsearch);
            AdRequest adRequest = new AdRequest.Builder().addTestDevice("EB38215ED85EFA82E937126940E5C31F").build();
            adview.loadAd(adRequest);
        }
        catch (Exception ex)
        {
            Crashlytics.logException(ex);
            android.util.Log.e("Couldnt initialize ads", ex.getMessage());
        }
        adview.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                adview.setTag(true); // Set tag true if adView is loaded
            }
            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                adview.setTag(false); // Set tag false if loading failed
            }
        });
    }

    private void UpdateView() {
        try {

                if (postContentAdapter != null) {
                    postContentAdapter.notifyDataSetChanged();
                }

        } catch (Exception ex) {
            Crashlytics.logException(ex);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.searchitem, menu);
        try {
            if (isNetworkConnected()) {
                searchView = (SearchView) menu.findItem(R.id.action_search1).getActionView();
                searchView.setQueryHint("Type something...");
                ImageView searchClose = searchView.findViewById(android.support.v7.appcompat.R.id.search_close_btn);
                searchClose.setImageResource(R.drawable.ic_close_white_24dp);
                ImageView searchIcon = searchView.findViewById(android.support.v7.appcompat.R.id.search_mag_icon);
                searchIcon.setImageResource(R.drawable.ic_search_black_24dp);
                gifView.setVisibility(View.GONE);
                foldableListLayout.setVisibility(View.GONE);
            }
            else
            {snackbarerror();}
        }catch (Exception exx) {
            Crashlytics.logException(exx);
            // Log.ERROR("Initialize UI failed", exx.getMessage());
        }
        try {
            if (isNetworkConnected()) {
                View searchPlate = searchView.findViewById(R.id.search_plate);
                if (searchPlate != null) {
                    TextView searchText = searchPlate.findViewById(R.id.search_src_text);
                    searchText.setTextColor(Color.WHITE);
                    searchText.setHintTextColor(Color.WHITE);
                }
                searchView.setIconifiedByDefault(false);
                onSearchRequested();
                searchView.requestFocus();
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String s) {
                        try {
                            searchText = s;

                            gifView.setVisibility(View.VISIBLE);
                            new loadingsearch().execute().get();


                        } catch (Exception ex) {
                            Crashlytics.logException(ex);
                        }
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String s) {
                        try {
                            foldableListLayout.setVisibility(View.GONE);
                            txtnotfound.setVisibility(View.GONE);
                            gifView.setVisibility(View.GONE);
                        } catch (Exception e) {
                            Crashlytics.logException(e);
                        }
                        return false;
                    }
                });
            }
            else
            {snackbarerror();}
        }catch (Exception ex) {
            Crashlytics.logException(ex);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            if(isNetworkConnected()) {
                switch (item.getItemId()) {
                    case android.R.id.home:
                        onBackPressed();
                        return true;
                }
            }
            else
            {snackbarerror();}
        } catch (Exception ex) {
            Crashlytics.logException(ex);
        }
        return super.onOptionsItemSelected(item);
    }


    private class loadingsearch extends AsyncTask<Void, Void, Void> {
        Call<List<PostSearch>> call = null;
        ApiService apiService;
//        @Override
//        protected void onPreExecute() {
//            try {
//                foldableListLayout.setVisibility(View.GONE);
//
//                //adView.resume();
//            } catch (Exception e) {
//                Crashlytics.logException(e);
//                e.printStackTrace();
//            }
//        }

        @Override
        protected Void doInBackground(Void... voids) {
            if(isNetworkConnected()) {
                Retrofit retrofitallpost = new Retrofit.Builder()
                        .baseUrl("https://amdavadblog.com/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(SynchronousCallAdapterFactory.create())
                        .build();
                apiService = retrofitallpost.create(ApiService.class);
                call = apiService.getPostsByQuerySearch(searchText);
            }
            else {snackbarerror();}

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            try {
                if (isNetworkConnected()) {
                    call.enqueue(new Callback<List<PostSearch>>() {
                        @Override
                        public void onResponse(Call<List<PostSearch>> call, Response<List<PostSearch>> response) {
                            Media resp2 = null;
                            Author Auth = null;
                            Category category = null;
                            if (response.isSuccessful()) {
                                int postsize = response.body().size();
                                if (postsize != 0) {
                                    for (final PostSearch post1 : response.body()) {
                                        Integer catId = post1.categories.get(0);
                                        category = apiService.getPostCategoryById(catId);
                                        post1.categoryname = category.getName();

                                        Auth = apiService.getPostAuthorById(post1.author);
                                        post1.authorname = Auth.getName();

                                        resp2 = apiService.getFeaturedImageById(post1.featured_media);
                                        if (resp2 == null) {
                                            post1.imagePath = String.valueOf(R.drawable.ic_home_black_24dp);
                                        } else
                                            post1.imagePath = resp2.media_details.sizes.medium_large.source_url.toString();
                                        postContentAdapter.setData1(response.body());
                                        postContentAdapter.notifyDataSetChanged();
                                        foldableListLayout.setVisibility(View.VISIBLE);

                                        txtnotfound.setVisibility(View.GONE);
                                        gifView.setVisibility(View.GONE);
                                    }


                                } else {
                                    foldableListLayout.setVisibility(View.GONE);
                                    txtnotfound.setVisibility(View.VISIBLE);
                                    gifView.setVisibility(View.GONE);
                                }
                                //  postContentAdapter.setData1(response.body());
                            }
                            // return null;
                        }

                        @Override
                        public void onFailure(Call<List<PostSearch>> call, Throwable t) {
                            Crashlytics.logException(t);
                        }
                    });
                    // foldableListLayout.setVisibility(View.VISIBLE);
                    // postContentAdapter.setData1(response.body());
                    // postContentAdapter.notifyDataSetChanged();
                    // gifView.setVisibility(View.GONE);

                    //adView.resume();
                }
                else
                {snackbarerror();}
            }catch (Exception e) {
                Crashlytics.logException(e);
                e.printStackTrace();
            }

        }
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
                    .make(linearsearch, "No internet connection!", Snackbar.LENGTH_INDEFINITE)
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

