package com.example.xps.amdavadblog_app;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.alexvasilkov.foldablelayout.FoldableListLayout;
import com.crashlytics.android.Crashlytics;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import Adapter.PostContentAdapter;
import Core.Helper.SynchronousCallAdapterFactory;
import Helper.GifView;
import Model.Post;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import services.CacheService;


/**
 * A simple {@link Fragment} subclass.
 */
public class FoldableListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    List<Post> currentPost;
    List<Post> postList = null;
    AdView adView;
    public int CategoryId;
    public FoldableListLayout foldableListLayout;
    public PostContentAdapter postContentAdapter;
    public List AllPost;
    public View view;
    public Activity activity;
    String content,title,date,author,category;
    public int getCategoryId() {
        return CategoryId;
    }
    public void setCategoryId(int categoryId) {
        CategoryId = categoryId;
    }
    int page = 1,postcount;
    SwipeRefreshLayout mSwipeRefreshLayout;
    boolean loading = false;
    boolean reachedMax = false;
    GifView gifView;
    FrameLayout frmlayout;
    Snackbar snackbar;
    Retrofit retrofitallpost=new Retrofit.Builder()
            .baseUrl("http://api.amdavadblog.com/amdblog/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(SynchronousCallAdapterFactory.create())
            .build();
    public FoldableListFragment() {

    }
    @SuppressLint("ValidFragment")
    public FoldableListFragment(int id)
    {
        CategoryId = id;
    }

    @SuppressLint("ResourceAsColor")
    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        try {

            view = inflater.inflate(R.layout.fragment_foldable_list, container, false);

            activity = (Activity) view.getContext();
            frmlayout = (FrameLayout) view.findViewById(R.id.framelayout);
            gifView = (GifView) view.findViewById(R.id.loadinggif);
            postContentAdapter = new PostContentAdapter(postList, activity);
            foldableListLayout = view.findViewById(R.id.foldable_list);
            foldableListLayout.setAdapter(postContentAdapter);

            foldableListLayout.setOnFoldRotationListener(new FoldableListLayout.OnFoldRotationListener() {
                @Override
                public void onFoldRotation(float rotation, boolean isFromUser) {
                    int firstVisiblePosition = (int) (rotation / 180f);

                    postcount = AllPost.size();
                    int count = postcount - 5;
                    if (firstVisiblePosition != 0 && firstVisiblePosition == count) {
                        if (loading || reachedMax) {

                        } else {
                            Thread thread = new Thread(null, loadMoreListItems);
                            thread.start();

                        }
                    }
                }
            });
        }
        catch (Exception ex)
        {
            Crashlytics.logException(ex);
        }


        return view;
    }
    @Override
    public void onRefresh() {
      // mSwipeRefreshLayout.setRefreshing(true);
        if(isNetworkConnected()) {
            if (CategoryId == 100) {
                try {
                    AllPost = CacheService.GetAllPostnew(true, 1);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else {
                try {
                    AllPost = CacheService.GetPostByCategoryId(1, CategoryId, true);
                } catch (IOException e) {
                    Crashlytics.logException(e);
                    e.printStackTrace();
                }
            }
            try {
                if (postContentAdapter != null)
                    postContentAdapter.notifyDataSetChanged();
                // mSwipeRefreshLayout.setRefreshing(false);
            } catch (Exception ex) {
                Crashlytics.logException(ex);
            }
        }
        else
        {snackbarerror();}
    }

    @Override
    public void onPause() {
        try {
            super.onPause();
//            if (mSwipeRefreshLayout != null) {
//                mSwipeRefreshLayout.setRefreshing(false);
//                mSwipeRefreshLayout.destroyDrawingCache();
//                mSwipeRefreshLayout.clearAnimation();
//            }
        }
        catch (Exception ex)
        {
            Crashlytics.logException(ex);
        }
    }

    private Runnable loadMoreListItems = new Runnable() {

        @Override
        synchronized public void run() {
            if (isNetworkConnected()) {
                // Set flag so we cant load new items 2 at the same time
                final int temp = page + 1;
                loading = true;
                if (CategoryId == 100) try {
                    AllPost = CacheService.GetAllPostnew(true, temp);
                } catch (FileNotFoundException e) {
                    Crashlytics.logException(e);
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                else {
                    try {
                        AllPost = CacheService.GetPostByCategoryId(temp, CategoryId, true);
                    } catch (Exception ex) {
                        Crashlytics.logException(ex);
                    }
                }
                activity.runOnUiThread(returnRes);

            }
            else
            {snackbarerror();}
        }

    };

    private Runnable returnRes = new Runnable() {
        @Override
        public void run() {
            // Add the new items to the adapter
            if(isNetworkConnected()) {
                if (postContentAdapter != null)
                    postContentAdapter.notifyDataSetChanged();

                loading = false;
                page++;
            }
            else
            {snackbarerror();}
        }
    };

    @Override
    public void onResume() {
        try {
            super.onResume();
            if(isNetworkConnected())
            new loadingview().execute();
            else
                snackbarerror();
            //adView.resume();
        }
        catch (Exception ex)
        {
            Crashlytics.logException(ex);
        }
    }

    private void InitializeAds(View view) {
        try
        {
                MobileAds.initialize(getActivity(), "ca-app-pub-1870433400625480~7602115204");
                adView = view.findViewById(R.id.adView1);
                AdRequest adRequest = new AdRequest.Builder().addTestDevice("EB38215ED85EFA82E937126940E5C31F").build();
                adView.loadAd(adRequest);
        }
        catch (Exception ex)
        {
            Crashlytics.logException(ex);
            android.util.Log.e("Couldnt initialize ads", ex.getMessage());
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

    private class loadingview extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPostExecute(Void result) {
            try {
                if(isNetworkConnected()) {
                    postContentAdapter.setData(AllPost);
                    postContentAdapter.notifyDataSetChanged();
                    foldableListLayout.setVisibility(View.VISIBLE);
                    gifView.setVisibility(View.GONE);

                    InitializeAds(view);
                }
                else
                {snackbarerror();}
            }
            catch (Exception e) {
                Crashlytics.logException(e);
                e.printStackTrace();
            }
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if(isNetworkConnected()) {
                if (CategoryId == 100) {
                    try {
                        AllPost = CacheService.GetAllPostnew(false, 1);
                    } catch (FileNotFoundException e) {
                        Crashlytics.logException(e);
                        e.printStackTrace();
                    } catch (IOException e) {
                        Crashlytics.logException(e);
                        e.printStackTrace();
                    }
                } else {
                    try {
                        AllPost = CacheService.GetPostByCategoryId(1, CategoryId, false);
                    } catch (IOException e) {
                        Crashlytics.logException(e);
                        e.printStackTrace();
                    }
                }
            }
            else
            {snackbarerror();}
            return null;
        }

        @Override
        protected void onPreExecute()
        {
            try {
                if(isNetworkConnected()) {
                    foldableListLayout.setVisibility(View.GONE);
                    gifView.setVisibility(View.VISIBLE);
                }
                else
                {snackbarerror();}
                //adView.resume();
            }
            catch (Exception e) {
                Crashlytics.logException(e);
                e.printStackTrace();
            }
        }
    }
    private boolean isNetworkConnected() {
        //return true;
        ConnectivityManager cm = null;
        try {
            cm = (ConnectivityManager)activity.getSystemService(Context.CONNECTIVITY_SERVICE);
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
                    .make(frmlayout, "No internet connection!", Snackbar.LENGTH_INDEFINITE)
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