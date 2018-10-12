package com.example.xps.amdavadblog_app;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.alexvasilkov.foldablelayout.FoldableListLayout;
import com.crashlytics.android.Crashlytics;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import Adapter.PostContentAdapter;
import Core.Helper.ApiService;
import Core.Helper.SynchronousCallAdapterFactory;
import Model.Post;
import Model.StartJsonDataClass;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
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
            mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
            mSwipeRefreshLayout.setOnRefreshListener(this);
            postContentAdapter = new PostContentAdapter(postList, activity);
            foldableListLayout = view.findViewById(R.id.foldable_list);
            foldableListLayout.setAdapter(postContentAdapter);

            foldableListLayout.setOnFoldRotationListener(new FoldableListLayout.OnFoldRotationListener() {
                @Override
                public void onFoldRotation(float rotation, boolean isFromUser) {
                    int firstVisiblePosition = (int) (rotation / 180f);
                    mSwipeRefreshLayout.setEnabled(firstVisiblePosition == 0);
                    postcount = AllPost.size();
                    int count = postcount - 5;
                    if (firstVisiblePosition != 0 && firstVisiblePosition == count) {
                        if (loading || reachedMax) {

                        } else {
                            int temp = page + 1;
                            try {
                                LetCall(temp);
                            } catch (IOException e) {
                                Crashlytics.logException(e);
                                e.printStackTrace();
                            }
                            page++;
                        }
                    }
                }
            });
        }
        catch (Exception ex)
        {
            Crashlytics.logException(ex);
        }

        InitializeAds(view);
        return view;
    }
    @Override
    public void onRefresh() {
       mSwipeRefreshLayout.setRefreshing(true);
        if (CategoryId == 100) {
            try {
                AllPost = CacheService.GetAllPostnew(true,1);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        else
        {
            try {
                AllPost = CacheService.GetPostByCategoryId(1,CategoryId,true);
            } catch (IOException e) {
                Crashlytics.logException(e);
                e.printStackTrace();
            }
        }
        try {
            if (postContentAdapter != null)
                postContentAdapter.notifyDataSetChanged();
            mSwipeRefreshLayout.setRefreshing(false);
        }
        catch (Exception ex)
        {
            Crashlytics.logException(ex);
        }
    }

    @Override
    public void onPause() {
        try {
            super.onPause();
            if (mSwipeRefreshLayout != null) {
                mSwipeRefreshLayout.setRefreshing(false);
                mSwipeRefreshLayout.destroyDrawingCache();
                mSwipeRefreshLayout.clearAnimation();
            }
        }
        catch (Exception ex)
        {
            Crashlytics.logException(ex);
        }
    }
    private void LetCall(int i) throws IOException {
        loading = true;
        if (CategoryId == 100) {
            try {
                AllPost = CacheService.GetAllPostnew(true,i);
            } catch (FileNotFoundException e) {
                Crashlytics.logException(e);
                e.printStackTrace();
            }

        }
        else
        {
            try {
                AllPost = CacheService.GetPostByCategoryId(i, CategoryId, true);
            }
            catch (Exception ex)
            {
                Crashlytics.logException(ex);
            }
        }
        try {
            postContentAdapter = new PostContentAdapter(AllPost, activity);
            foldableListLayout.setAdapter(postContentAdapter);
            if (postContentAdapter != null)
                postContentAdapter.notifyDataSetChanged();
            loading = false;
        }
        catch (Exception ex)
        {
            Crashlytics.logException(ex);
        }
    }

    @Override
    public void onResume() {
        try {
            super.onResume();
            adView.resume();

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
            postContentAdapter.setData(AllPost);
            postContentAdapter.notifyDataSetChanged();
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


}