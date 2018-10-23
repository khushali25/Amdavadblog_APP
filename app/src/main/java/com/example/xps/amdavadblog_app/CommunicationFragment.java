package com.example.xps.amdavadblog_app;


import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import Core.Helper.SynchronousCallAdapterFactory;
import Core.Helper.ApiService;
import Model.Contact;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import static android.support.constraint.Constraints.TAG;
import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 */
public class CommunicationFragment extends Fragment {

    TextInputEditText edtemail,edtphone,edtname,edtmessage;
    TextInputLayout txtemail,txtphone,txtname,txtmessage;
    String phone;
    RelativeLayout framelayoutfragment;
    Snackbar snackbar;
    AdView adviewcontact;
    public CommunicationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = null;
        try {
            view = inflater.inflate(R.layout.fragment_communication, container, false);
            if (isNetworkConnected()) {
                edtphone = view.findViewById(R.id.phone_edit_text);
                txtphone = view.findViewById(R.id.phone_text_input);

                edtname = view.findViewById(R.id.name_edit_text);
                txtname = view.findViewById(R.id.name_text_input);

                edtemail = view.findViewById(R.id.email_edit_text);
                txtemail = view.findViewById(R.id.email_text_input);

                edtmessage = view.findViewById(R.id.message_edit_text);
                txtmessage = view.findViewById(R.id.message_text_input);

                adviewcontact = view.findViewById(R.id.adViewcontact);
                framelayoutfragment = (RelativeLayout) view.findViewById(R.id.framelayoutfragment);

                final TextInputEditText[] textInputLayouts = new TextInputEditText[]{edtname, edtemail, edtmessage};

                final Button button = view.findViewById(R.id.submitbtn);

                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        boolean noErrors = true;
                        final Animation myAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bounce);
                        button.startAnimation(myAnim);
                        final String name = edtname.getText().toString();
                        final String email = edtemail.getText().toString();
                        phone = edtphone.getText().toString();
                        final String message = edtmessage.getText().toString();

                        for (TextInputEditText textInputLayout : textInputLayouts) {
                            String editTextString = textInputLayout.getText().toString();
                            if (editTextString.isEmpty()) {
                                textInputLayout.setError("Field must not be empty");
                                noErrors = false;
                            } else if (!isvalidemail(email)) {
                                txtemail.setError("Email is not valid");
                                noErrors = false;
                            } else if (!isvalidphone(phone) && edtphone.length() != 0) {
                                txtphone.setError("Phone is not valid");
                                noErrors = false;
                            } else {
                                textInputLayout.setError(null);
                            }
                        }
                        edtemail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                            @Override
                            public void onFocusChange(View v, boolean hasFocus) {
                                if (!hasFocus) {
                                    validateemailEditText(String.valueOf(((EditText) v).getText()));
                                }
                            }

                            private void validateemailEditText(String text) {
                                if (isvalidemail(text)) {
                                    txtemail.setError(null);
                                } else {
                                    txtemail.setError("Email is not valid");
                                }
                            }
                        });
                        edtphone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                            @Override
                            public void onFocusChange(View v, boolean hasFocus) {
                                if (!hasFocus)
                                    validatephoneEditText(String.valueOf(((EditText) v).getText()));
                            }

                            private void validatephoneEditText(String text) {
                                if (edtphone.length() == 0) {
                                    txtphone.setError(null);
                                } else if (isvalidphone(text)) {
                                    txtphone.setError(null);
                                } else {
                                    txtphone.setError("Phone number is not valid");
                                }
                            }
                        });
                        if (noErrors) {
                            // All fields are valid!
                            Retrofit retrofitallpost = new Retrofit.Builder()
                                    .baseUrl("http://api.amdavadblog.com/amdblog/")
                                    .addConverterFactory(GsonConverterFactory.create())
                                    .addCallAdapterFactory(SynchronousCallAdapterFactory.create())
                                    .build();
                            final ApiService apiService = retrofitallpost.create(ApiService.class);
                            if (phone.equals("")) {
                                edtphone.setText("null");
                                phone = edtphone.getText().toString();
                            }
                            Call<Contact> call = apiService.saveContactDetail(name, email, phone, message);
                            call.enqueue(new Callback<Contact>() {
                                @Override
                                public void onResponse(Call<Contact> call, Response<Contact> response) {
                                    edtemail.setText(null);
                                    edtmessage.setText(null);
                                    edtname.setText(null);
                                    edtphone.setText(null);
                                    Toast.makeText(getApplicationContext(), "Thank you for contact us", Toast.LENGTH_LONG).show();

                                    Log.e(TAG, "Success");
                                }

                                @Override
                                public void onFailure(Call<Contact> call, Throwable t) {
                                    Crashlytics.logException(t);
                                    Log.e(TAG, "Fail" + t);
                                }
                            });
                        }
//
                    }
                });
            }
            else
            {snackbarerror();}
        }
        catch (Exception ex)
        {
            Crashlytics.logException(ex);
        }
        InitializeAds(view);
        return view;
    }

    private boolean isvalidphone(String edtphone) {
        return edtphone.length() == 10;
    }

    private boolean isvalidemail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    private void InitializeAds(View view) {
        try
        {
            MobileAds.initialize(getActivity(), "ca-app-pub-1870433400625480~7602115204");
            //adView = view.findViewById(R.id.adView1);
            AdRequest adRequest = new AdRequest.Builder().addTestDevice("EB38215ED85EFA82E937126940E5C31F").build();
            adviewcontact.loadAd(adRequest);
        }
        catch (Exception ex)
        {
            Crashlytics.logException(ex);
            android.util.Log.e("Couldnt initialize ads", ex.getMessage());
        }
        adviewcontact.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                adviewcontact.setTag(true); // Set tag true if adView is loaded
            }
            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                adviewcontact.setTag(false); // Set tag false if loading failed
            }
        });
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
                    .make(framelayoutfragment, "No internet connection!", Snackbar.LENGTH_INDEFINITE)
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