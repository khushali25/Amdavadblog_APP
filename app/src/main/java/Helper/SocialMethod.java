package Helper;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

public class SocialMethod
{
    public static void showRateApp(Context con)
    {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        //Try Google play
        intent.setData(android.net.Uri.parse("market://details?id=com.cubeactive.qnotelistfree"));
        if (!MyStartActivity(intent, con))
        {
            //Market (Google play) app seems not installed, let's try to open a webbrowser
            intent.setData(android.net.Uri.parse("https://play.google.com/store/apps/details?[Id]"));
            if (!MyStartActivity(intent, con))
            {
                //Well if this also fails, we have run out of options, inform the user.
                Toast.makeText(con, "Could not open Android market, please install the market app.", Toast.LENGTH_LONG).show();
            }
        }
    }
    public static boolean MyStartActivity(Intent intent, Context con)
    {
        try
        {
            con.startActivity(intent);
            return true;
        }
        catch (ActivityNotFoundException e)
        {
            return false;
        }
    }

    public static void showFeedback(Context context)
    {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(android.net.Uri.parse("mailto:"));
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] { "amdavadblogs@gmail.com" });
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Amdavad Blog Feedback");
        try
        {
            context.startActivity(Intent.createChooser(emailIntent, "Send mail..."));
        }
        catch (ActivityNotFoundException ex)
        {
            Toast.makeText(context, "There is no email client installed.", Toast.LENGTH_LONG).show();
        }
    }

    public static void shareIt(Context context)
    {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Amdavad Blogs");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, "Now follow latest blogs with Amdavad Blog click here to visit https://amdavadblog.com/");
        context.startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    public static void showSubscription(final Context context)
    {
        LayoutInflater layoutinflater = LayoutInflater.from(context);
        final View promptView = layoutinflater.inflate(R.layout.subscriptionview, null);

        final android.support.v7.app.AlertDialog.Builder alert1 = new android.support.v7.app.AlertDialog.Builder(context);

        alert1.setView(promptView);
        alert1.setTitle("Subscribe");

        alert1.setPositiveButton("Subscribe", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                EditText userEmail = promptView.findViewById(R.id.emailid);
                TextView showResult = (TextView)promptView.findViewById(R.id.result);
                String userValue = userEmail.getText().toString();
                boolean emailResult = isValidEmail(userValue);
                Toast.makeText(context,userValue,Toast.LENGTH_LONG).show();
                if (userValue.equals(""))
                {
                    try
                    {

                        showResult.setText("Please enter email id");
                        showResult.setVisibility(View.VISIBLE);
                    }
                    catch (Exception ex)
                    {
                       new Error(ex.getMessage());
                    }
                }
//               else if (emailResult == false)
//                {
//                    showResult.setVisibility(View.VISIBLE);
//                    showResult.setText("Please enter valid email address");
//                }
                else
                {
                    showResult.setVisibility(View.INVISIBLE);
                    dialogInterface.dismiss();

                }
            }
        });
        alert1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

      android.support.v7.app.AlertDialog dialog = alert1.create();
       dialog.show();

    }
    public static boolean isValidEmail(String email)
    {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static void alreadySubscribed(Context context)
    {
        LayoutInflater layoutinflater = LayoutInflater.from(context);
        android.support.v7.app.AlertDialog.Builder alert2 = new android.support.v7.app.AlertDialog.Builder(context);
        final Dialog _dialog = alert2.create();
        _dialog.show();

        alert2.setMessage("You have already been subscribed..!!");
        alert2.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                _dialog.dismiss();
            }
        });
    }

}