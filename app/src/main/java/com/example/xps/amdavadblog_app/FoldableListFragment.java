package com.example.xps.amdavadblog_app;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.alexvasilkov.foldablelayout.FoldableListLayout;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;
import java.util.List;
import Adapter.PostContentAdapter;
import Core.WordPressService;
import Model.Author;
import Model.Category;
import Model.Media;
import Model.Post;
import Model.SynchronousCallAdapterFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
/**
 * A simple {@link Fragment} subclass.
 */
public class FoldableListFragment extends Fragment {
    List<Post> postList = null;
    AdView adView;

    public int CategoryId;
    public FoldableListLayout foldableListLayout;
    public PostContentAdapter postContentAdapter;
    public int getCategoryId() {
        return CategoryId;
    }
    public List<Post> AllPost;
    public View view;
    public Activity activity;

    public void setCategoryId(int categoryId) {
        CategoryId = categoryId;
    }

    public FoldableListFragment() {

        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public FoldableListFragment(int id)
    {
        CategoryId = id;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_foldable_list, container, false);
        activity = (Activity) view.getContext();
        InitializeAds(view);
        postContentAdapter = new PostContentAdapter(postList,activity);
        foldableListLayout = view.findViewById(R.id.foldable_list);
        foldableListLayout.setAdapter(postContentAdapter);
        foldableListLayout.setOnFoldRotationListener(new FoldableListLayout.OnFoldRotationListener() {
            @Override
            public void onFoldRotation(float rotation, boolean isFromUser) {
                final int firstVisiblePosition = (int) (rotation / 180f);
                if (firstVisiblePosition != 0 &&  firstVisiblePosition % 5 == 0) {

                    String hello = "I am 5th post";
                    LetCall(2);
                }
            }
        });
//        LetCall(1);
        Retrofit retrofitallpost=new Retrofit.Builder()
                .baseUrl("https://amdavadblogs.apps-1and1.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(SynchronousCallAdapterFactory.create())
                .build();

        final WordPressService wordPressService = retrofitallpost.create(WordPressService.class);
        Call<List<Post>> call = null;
        if (CategoryId == 100) {
             call = wordPressService.getAllPostPerPage(1);
        }
        else
        {
            call = wordPressService.getAllPostByCategoryId(CategoryId);
        }

        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                Log.d("myResponse:", "Total Post:" + response.body().size());
                Media resp2 = null;
                Category cat = null;
                Author Auth = null;
                AllPost = response.body();
                for (Post post : AllPost) {
                    if (response.isSuccessful()) {
                        Integer catId = post.categories.get(0);

                        Auth = wordPressService.getPostAuthorById(post.author);
                        post.authorname = Auth.name;
                        cat = wordPressService.getPostCategoryById(catId);
                        post.categoryname = cat.name;
                        resp2 = wordPressService.getFeaturedImageById(post.featured_media);
                        if(resp2 == null)
                        {
                            post.imagePath = String.valueOf(R.drawable.demo);
                        }
                        else
                            post.imagePath = resp2.media_details.sizes.medium_large.source_url.toString();
                    }
                    else
                        {

                        }
                }
                 postContentAdapter.setData(AllPost);
                 postContentAdapter.notifyDataSetChanged();
            }
            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                Log.d("myResponse:", "MSG" + t.toString());
            }
        });
        return view;
    }

    private void LetCall(final Integer page){
        Retrofit retrofitallpost=new Retrofit.Builder()
                .baseUrl("https://amdavadblogs.apps-1and1.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(SynchronousCallAdapterFactory.create())
                .build();
        final WordPressService wordPressService = retrofitallpost.create(WordPressService.class);
        Call<List<Post>> call;
        if (CategoryId == 100) {
            call = wordPressService.getAllPostPerPage(page);
        }
        else
        {
            call = wordPressService.getAllPostByCategoryId(CategoryId);
        }

        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                Log.d("myResponse:", "Total Post:" + response.body().size());
                Media resp2 = null;
                Category cat = null;
                Author Auth = null;
                List<Post> currentPost = response.body();
                for (Post post : currentPost) {
                    if (response.isSuccessful()) {
                        Integer catId = post.categories.get(0);
                        Auth = wordPressService.getPostAuthorById(post.author);
                        post.authorname = Auth.name;
                        cat = wordPressService.getPostCategoryById(catId);
                        post.categoryname = cat.name;
//                        resp2 = wordPressService.getFeaturedImageById(post.featured_media);

                        if(true)
                        {
                            post.imagePath = String.valueOf(R.drawable.demo);
                        }
                        else
                            post.imagePath = resp2.media_details.sizes.medium_large.source_url.toString();
                    }
                    else
                    {

                    }
                }

                AllPost.addAll(currentPost); postContentAdapter.notify(AllPost);
                postContentAdapter = new PostContentAdapter(AllPost,activity);
                foldableListLayout.setAdapter(postContentAdapter);
                if (postContentAdapter != null)
                {
                    postContentAdapter.notifyDataSetChanged();
                }
//
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        postContentAdapter.notify(AllPost);
//                        postContentAdapter = new PostContentAdapter(AllPost,activity);
//                        foldableListLayout.setAdapter(postContentAdapter);
//                        if (postContentAdapter != null)
//                        {
//                            postContentAdapter.notifyDataSetChanged();
//                        }
//
//                        // postContentAdapter.notifyDataSetChanged();
//                    }
                //



//                if(page == 1) {
//                    //postContentAdapter.setData(AllPost);
//                    //postContentAdapter.notify();
//                    //postContentAdapter.notifyDataSetChanged();
//                }
//                else {
//                    //postContentAdapter.notify(AllPost);
//                   // postContentAdapter.notify();
//                    postContentAdapter.notifyDataSetChanged();
//
//                }
                }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {

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
