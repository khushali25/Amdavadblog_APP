package com.example.xps.amdavadblog_app;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.alexvasilkov.android.commons.ui.Views;
import com.alexvasilkov.foldablelayout.FoldableListLayout;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import Adapter.PostContentAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class FoldableListFragment extends Fragment {

    AdView adView;
    public FoldableListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_foldable_list, container, false);
        Activity activity =(Activity)view.getContext();
        InitializeAds(view);
        FoldableListLayout foldableListLayout = view.findViewById(R.id.foldable_list);
        foldableListLayout.setAdapter(new PostContentAdapter(activity));
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
