package Helper;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.text.method.DialerKeyListener;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xps.amdavadblog_app.R;

import java.io.Console;

import Core.WordPressService;
import Model.StartJsonDataClass;
import Model.Subscribe;
import Model.SynchronousCallAdapterFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.support.constraint.Constraints.TAG;
import static com.facebook.FacebookSdk.getApplicationContext;

public class SocialMethod {
    public static void showRateApp(Context con) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        //Try Google play
        intent.setData(android.net.Uri.parse("market://details?id=com.cubeactive.qnotelistfree"));
        if (!MyStartActivity(intent, con)) {
            //Market (Google play) app seems not installed, let's try to open a webbrowser
            intent.setData(android.net.Uri.parse("https://play.google.com/store/apps/details?[Id]"));
            if (!MyStartActivity(intent, con)) {
                //Well if this also fails, we have run out of options, inform the user.
                Toast.makeText(con, "Could not open Android market, please install the market app.", Toast.LENGTH_LONG).show();
            }
        }
    }

    public static boolean MyStartActivity(Intent intent, Context con) {
        try {
            con.startActivity(intent);
            return true;
        } catch (ActivityNotFoundException e) {
            return false;
        }
    }

    public static void showFeedback(Context context) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(android.net.Uri.parse("mailto:"));
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"amdavadblogs@gmail.com"});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Amdavad Blog Feedback");
        try {
            context.startActivity(Intent.createChooser(emailIntent, "Send mail..."));
        } catch (ActivityNotFoundException ex) {
            Toast.makeText(context, "There is no email client installed.", Toast.LENGTH_LONG).show();
        }
    }

    public static void shareIt(Context context) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Amdavad Blogs");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, "Now follow latest blogs with Amdavad Blog click here to visit https://amdavadblog.com/");
        context.startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    public static void showSubscription(final Context context) {
        final Subscribe subscribe = new Subscribe();
        Retrofit retrofitallpost=new Retrofit.Builder()
                .baseUrl("http://192.168.1.5:3000/amdblog/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(SynchronousCallAdapterFactory.create())
                .build();
        final WordPressService wordPressService = retrofitallpost.create(WordPressService.class);

        LayoutInflater layoutinflater = LayoutInflater.from(context);
        final View promptView = layoutinflater.inflate(R.layout.subscriptionview, null);
        final AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setView(promptView)
                .setTitle("Subsribe")
                .setPositiveButton("Subscribe", null)
                .setNegativeButton("Cancel", null)
                .create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {

                Button buttonPositive = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE);
                buttonPositive.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        EditText userEmail = promptView.findViewById(R.id.emailid);
                        TextView showResult = (TextView) promptView.findViewById(R.id.result);
                        final String userValue = userEmail.getText().toString();
                        boolean emailResult = isValidEmail(userValue);
                        if (userValue.equals("")) {
                            try {
                                showResult.setText("Please enter email id");
                                showResult.setVisibility(View.VISIBLE);
                            } catch (Exception ex) {
                                new Error(ex.getMessage());
                            }
                        } else if (emailResult == false) {
                            showResult.setVisibility(View.VISIBLE);
                            showResult.setText("Please enter valid email address");
                        } else {

                                Call<Subscribe> call = wordPressService.saveSubscriberEmail(userValue);
                                call.enqueue(new Callback<Subscribe>() {
                                    @Override
                                    public void onResponse(Call<Subscribe> call, Response<Subscribe> response) {
                                        Toast.makeText(getApplicationContext(), "Thank you for subscribe", Toast.LENGTH_LONG).show();
                                        Log.e(TAG, "Success");
                                    }

                                    @Override
                                    public void onFailure(Call<Subscribe> call, Throwable t) {
                                        Log.e(TAG, "Fail" + t);
                                    }
                                });

                            PrefService ap = new PrefService(context);
                            ap.saveAccessKey("subscribe", userValue);
                            showResult.setVisibility(View.INVISIBLE);
                            alertDialog.dismiss();
                        }
                    }
                });
                Button buttonNegative = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_NEGATIVE);
                buttonNegative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });
            }
        });
        alertDialog.show();
    }

    public static boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static void alreadySubscribed(final Context context) {

        LayoutInflater layoutinflater = LayoutInflater.from(context);
        final AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setTitle("You have already been subscribed..!")
                 .setPositiveButton("Cancel", null)
                .create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button buttonPositive = ((AlertDialog) alertDialog).getButton(DialogInterface.BUTTON_POSITIVE);
                buttonPositive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });
            }
        });
        alertDialog.show();
    }
}