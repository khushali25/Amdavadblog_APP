package com.example.xps.amdavadblog_app;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;

/**
 * A simple {@link Fragment} subclass.
 */
public class IntroScreenFragment extends Fragment {

    private static final String BACKGROUND_COLOR = "backgroundColor";
    private static final String PAGE = "page";
    Snackbar snackbar;
    View background;

    private int mBackgroundColor, mPage;

    public static IntroScreenFragment newInstance(int backgroundColor, int page) {
        IntroScreenFragment frag = new IntroScreenFragment();
        try {
                    Bundle b = new Bundle();
                    b.putInt(BACKGROUND_COLOR, backgroundColor);
                    b.putInt(PAGE, page);
                    frag.setArguments(b);


        }
        catch (Exception ex)
        {
            Crashlytics.logException(ex);
        }
        return frag;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);

                if (!getArguments().containsKey(BACKGROUND_COLOR))
                    throw new RuntimeException("Fragment must contain a \"" + BACKGROUND_COLOR + "\" argument!");
                mBackgroundColor = getArguments().getInt(BACKGROUND_COLOR);

                if (!getArguments().containsKey(PAGE))
                    throw new RuntimeException("Fragment must contain a \"" + PAGE + "\" argument!");
                mPage = getArguments().getInt(PAGE);

        }
        catch (Exception ex)
        {
            Crashlytics.logException(ex);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Select a layout based on the current page
        int layoutResId;
        View view = null;
        try {
            if(isNetworkConnected()) {
                switch (mPage) {
                    case 0:
                        layoutResId = R.layout.introscreenfragmentlayout_1;
                        break;
                    case 1:
                        layoutResId = R.layout.introscreenfragmentlayout_2;
                        break;
                    case 2:
                        layoutResId = R.layout.introscreenfragmentlayout_3;
                        break;
                    default:
                        layoutResId = R.layout.introscreenfragmentlayout_4;
                }
                view = getActivity().getLayoutInflater().inflate(layoutResId, container, false);
                AssetManager am = getActivity().getApplicationContext().getAssets();
                Typeface custom_font = Typeface.createFromAsset(am, "font/DenkOne-Regular.ttf");
                TextView description = view.findViewById(R.id.description);
                description.setTypeface(custom_font);
                view.setTag(mPage);
            }
            else
            {
                snackbarerror();
            }
        }
        catch (Exception ex)
        {
            Crashlytics.logException(ex);
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        try {
            super.onViewCreated(view, savedInstanceState);
            if(isNetworkConnected()) {
                // Set the background color of the root view to the color specified in newInstance()
                background = view.findViewById(R.id.intro_background);
                background.setBackgroundColor(mBackgroundColor);
            }
            else {
                snackbarerror();
            }
        }
        catch (Exception ex)
        {
            Crashlytics.logException(ex);
        }
    }
    private boolean isNetworkConnected() {
        ConnectivityManager cm = null;
        try {
            cm = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
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
                    .make(background, "No internet connection!", Snackbar.LENGTH_INDEFINITE)
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
