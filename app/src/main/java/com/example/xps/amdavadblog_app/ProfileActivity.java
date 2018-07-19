package com.example.xps.amdavadblog_app;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private ShareDialog shareDialog;
    private String name,surname,email,birthday,gender;
    String imguri1;
    Bitmap theImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Bundle inBundle = getIntent().getExtras();
        name = inBundle.getString("name");
        surname = inBundle.getString("surname");
        email = inBundle.getString("email");
        birthday = inBundle.getString("birthday");
        gender = inBundle.getString("gender");
        imguri1 = inBundle.getString("imageUrl");

        TextView nameView = (TextView)findViewById(R.id.nameAndSurname);
        nameView.setText("" + name + " " + surname);

        TextView email1 = (TextView)findViewById(R.id.emailid);
        if(email != "")
            email1.setText(email);
        else
            email1.setText("Email Not Found");

        TextView gender1 = (TextView)findViewById(R.id.gender);
        if(gender != null) {
            gender1.setText(gender);
        }
        else {
            gender1.setText("Gender Not Found");
        }

        TextView dob = (TextView)findViewById(R.id.birthdate);
        if(birthday != "")
            dob.setText(birthday);
        else
            dob.setText("Birthdate Not Found");

        ImageView img = (ImageView) findViewById(R.id.profileImage);

        if(imguri1 == null)
        {
            theImage = BitmapFactory.decodeResource(this.getResources(),
                    R.drawable.unfold_glance);
            img.setImageBitmap(theImage);
        }
        else {
            getBitmapFromURL(imguri1);
            img.setImageBitmap(theImage);
        }
    }
    public Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            BufferedInputStream buf = new BufferedInputStream(input, 1024);
            theImage = BitmapFactory.decodeStream(buf);
            return theImage;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.share:
                share();
                break;

            case R.id.logout:
                logout();
                break;
        }
    }
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
    private void share(){
        shareDialog = new ShareDialog(this);
        ShareLinkContent content = new ShareLinkContent.Builder()
                .setContentUrl(Uri.parse("http://www.sitepoint.com"))
                .setContentTitle("This is a content title")
                .setContentDescription("This is a description")
                // .setPeopleIds(taggedUserIds)
                // .setPlaceId("{PLACE_ID}")
                .build();

        shareDialog.show(content);
    }

    private void logout(){
        LoginManager.getInstance().logOut();
        Intent login = new Intent(ProfileActivity.this, MainNavigationActivity.class);
        startActivity(login);
        finish();

    }
}
