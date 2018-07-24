package com.example.xps.amdavadblog_app;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.alexvasilkov.foldablelayout.FoldableListLayout;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.ResponseBody;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import Adapter.PostContentAdapter;
import Core.WordPressService;
import Model.Media;
import Model.Post;
import Model.SynchronousCallAdapterFactory;
import okhttp3.HttpUrl;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
/**
 * A simple {@link Fragment} subclass.
 */
public class FoldableListFragment extends Fragment {
    List<Post> postList = null;
    AdView adView;
    public FoldableListFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_foldable_list, container, false);
        Activity activity = (Activity) view.getContext();
        InitializeAds(view);
        final PostContentAdapter postContentAdapter = new PostContentAdapter(postList,activity);
        FoldableListLayout foldableListLayout = view.findViewById(R.id.foldable_list);
        foldableListLayout.setAdapter(postContentAdapter);

        Retrofit retrofitallpost=new Retrofit.Builder()
                .baseUrl("https://amdavadblogs.apps-1and1.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(SynchronousCallAdapterFactory.create())
                .build();

        final WordPressService wordPressService = retrofitallpost.create(WordPressService.class);
        Call<List<Post>> call = wordPressService.getAllPost();
        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                Log.d("myResponse:", "Total Post:" + response.body().size());
                Media resp2 = null;
                for (final Post post : response.body()) {
                    if (response.isSuccessful()) {
                        // handle
                        resp2 = wordPressService.getFeaturedImageById(post.featured_media);
                        post.imagePath = resp2.media_details.sizes.medium_large.source_url.toString();
                    }
                    else
                        {

                    }
                }
                 postContentAdapter.setData(response.body());
                 postContentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                Log.d("myResponse:", "MSG" + t.toString());
            }
        });
        return view;
    }

    private void InitializeAds(View view) {
        try
        {
            MobileAds.initialize(getActivity(), "ca-app-pub-1870433400625480~7602115204");

            adView = view.findViewById(R.id.adView1);
            //adView.AdSize = AdSize.SmartBanner;
            //adView.AdUnitId = "ca-app-pub-1870433400625480/7574195045";
            // AdRequest adRequest = new AdRequest.Builder().Build();

            AdRequest adRequest = new AdRequest.Builder().addTestDevice("EB38215ED85EFA82E937126940E5C31F").build();
            adView.loadAd(adRequest);
        }
        catch (Exception ex)
        {
            android.util.Log.e("Couldnt initialize ads", ex.getMessage());
        }
    }

}
