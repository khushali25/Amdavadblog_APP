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
    public int getCategoryId() {
        return CategoryId;
    }

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
        Call<List<Post>> call = null;
        if (CategoryId == 100) {
             call = wordPressService.getAllPost();
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
                List<Integer> cat = null;
                Author Auth = null;
                for (Post post : response.body()) {
                    if (response.isSuccessful()) {
                        // handle
                       cat = wordPressService.getPostCategoryById(post.categories);
                     // post.categoryname = cat.get();
                        Auth = wordPressService.getPostAuthorById(post.author);
                        post.authorname = Auth.name;
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
            AdRequest adRequest = new AdRequest.Builder().addTestDevice("EB38215ED85EFA82E937126940E5C31F").build();
            adView.loadAd(adRequest);
        }
        catch (Exception ex)
        {
            android.util.Log.e("Couldnt initialize ads", ex.getMessage());
        }
    }

}
