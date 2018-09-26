package com.example.xps.amdavadblog_app;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.alexvasilkov.foldablelayout.FoldableListLayout;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import Adapter.PostContentAdapter;
import Core.Helper.WordPressService;
import Model.Post;
import Model.StartJsonDataClass;
import Core.Helper.SynchronousCallAdapterFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
/**
 * A simple {@link Fragment} subclass.
 */
public class FoldableListFragment extends Fragment {

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
    int page = 1,postcount,postlessthan10s;
    Retrofit retrofitallpost=new Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/amdblog/")
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
    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_foldable_list, container, false);

        activity = (Activity) view.getContext();
        postContentAdapter = new PostContentAdapter(postList,activity);
        foldableListLayout = view.findViewById(R.id.foldable_list);
        foldableListLayout.setAdapter(postContentAdapter);

        foldableListLayout.setOnFoldRotationListener(new FoldableListLayout.OnFoldRotationListener() {
            @Override
          public void onFoldRotation(float rotation, boolean isFromUser) {

          int firstVisiblePosition = (int) (rotation / 180f);
          postcount = AllPost.size();
          int count = postcount - 5;
         if (firstVisiblePosition != 0 && firstVisiblePosition == count) {
             int temp = page;
             LetCall(temp + 1);
              page++;
             }
            }
        });
        final WordPressService wordPressService = retrofitallpost.create(WordPressService.class);
        Call<StartJsonDataClass> call = null;
        if (CategoryId == 100) {
            call = wordPressService.getAllPostPerPage(1);
        }
        else
        {
            call = wordPressService.getAllPostByCategoryId(1,CategoryId);
        }
        call.enqueue(new Callback<StartJsonDataClass>() {
            @Override
            public void onResponse(Call<StartJsonDataClass> call, Response<StartJsonDataClass> response) {
                    if (response.isSuccessful()) {
                        AllPost = response.body().getData();
                    } else {

                    }

                postContentAdapter.setData(AllPost);
                postContentAdapter.notifyDataSetChanged();
            }
            @Override
            public void onFailure(Call<StartJsonDataClass> call, Throwable t) {
                Log.d("myResponse:", "MSG" + t.toString());
            }
        });
        InitializeAds(view);
        return view;
    }
    private void LetCall(int i) {

        final WordPressService wordPressService = retrofitallpost.create(WordPressService.class);
        Call<StartJsonDataClass> call;
        if (CategoryId == 100) {
            call = wordPressService.getAllPostPerPage(i);
        }
        else
        {
            call = wordPressService.getAllPostByCategoryId(i,CategoryId);
        }

        call.enqueue(new Callback<StartJsonDataClass>() {
            @Override
            public void onResponse(Call<StartJsonDataClass> call, Response<StartJsonDataClass> response) {
                currentPost = response.body().getData();

                for (Post post : currentPost) {
                    if (response.isSuccessful()) {
                    } else {

                    }
                }
                postlessthan10s = currentPost.size();
                if(postlessthan10s == 10){
                    AllPost.addAll(currentPost);
                }
                else if(postlessthan10s < 10 && postlessthan10s != 0)
                {
                    AllPost.addAll(currentPost);
                }
                else
                {

                }
         postContentAdapter =new PostContentAdapter(AllPost, activity);
         foldableListLayout.setAdapter(postContentAdapter);
         if(postContentAdapter !=null)
                    postContentAdapter.notifyDataSetChanged();
        }
    @Override
    public void onFailure(Call<StartJsonDataClass> call, Throwable t) {

    }
        });

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
