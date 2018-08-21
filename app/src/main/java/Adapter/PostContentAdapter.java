package Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alexvasilkov.android.commons.adapters.ItemsAdapter;
import com.example.xps.amdavadblog_app.R;
import com.example.xps.amdavadblog_app.UnfoldableDetailsActivity;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import Model.Post;

import static com.facebook.AccessTokenManager.TAG;

//import static Model.Post.getItems;


public class PostContentAdapter extends ItemsAdapter<Post, PostContentAdapter.ViewHolder>
{
    public ImageLoader imgloader;
    Context context;
    private int index;
    List<Post> post;
    Post item;
    PostContentAdapter.ViewHolder vh;

    public PostContentAdapter(List<Post> posts,Activity context) {
        this.post = posts;
        this.context = context;
        //setItemsList(getItems());
        //  setItemsList(getItems1());
        imgloader = ImageLoader.getInstance();
    }
           @Override
           public int getCount() {
               int itemCount = 0;
               if (post != null) {
                   itemCount = post.size();

               }
               return itemCount;
           }
           @Override
               public Post getItem(int position) {
                   return post.get(position);
               }

           public void setData(List<Post> questionList) {
               this.post=questionList;
           }

           public void notify(List<Post> postList) {
               this.post = postList;
               notifyDataSetChanged();
           }
    class ViewHolder extends ItemsAdapter.ViewHolder {
        public TextView Title;
        public ImageView Art;
        public TextView Category;
        public TextView Author;
        public TextView Excerpts;
        public TextView date;
        public ProgressBar progressBar;

        public ViewHolder(View itemView) {
            super(itemView);
            Title = itemView.findViewById(R.id.posttitletext);
            Art = itemView.findViewById(R.id.postimage);
            Title = itemView.findViewById(R.id.posttitletext);
            Category = itemView.findViewById(R.id.category);
            Author = itemView.findViewById(R.id.author);
            Excerpts = itemView.findViewById(R.id.excerpt);
            date = itemView.findViewById(R.id.date1);
        }
    }
    @Override
    protected PostContentAdapter.ViewHolder onCreateHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.post_listing, null, false);
        PostContentAdapter.ViewHolder vh = new PostContentAdapter.ViewHolder(view);
        if (!imgloader.isInited()) {
            imgloader.init(ImageLoaderConfiguration
                    .createDefault(parent.getContext()));
        }
        return vh;
    }
    @Override
    protected void onBindHolder(PostContentAdapter.ViewHolder viewHolder, int position) {
       // String refreshedToken = null;

            //refreshedToken = FirebaseInstanceId.getInstance().getToken();

       // Log.d(TAG, "Refreshed token: " + refreshedToken);
        vh = viewHolder;
        AssetManager am = context.getApplicationContext().getAssets();
        Typeface custom_font = Typeface.createFromAsset(am, "font/Amaranth-Regular.ttf");
        Typeface custom_font1 = Typeface.createFromAsset(am, "font/WorkSans-Regular.ttf");
        Typeface custom_font2 = Typeface.createFromAsset(am, "font/Martel-Regular.ttf");
        Typeface custom_font3 = Typeface.createFromAsset(am, "font/Martel-Bold.ttf");

        item = getItem(position);
        vh.Title.setText(Html.fromHtml(item.title.rendered));
        vh.Title.setTypeface(custom_font);
        vh.Category.setText(Html.fromHtml("#" + item.getCategoryname()));
        vh.Category.setTypeface(custom_font1);
        vh.Author.setText(Html.fromHtml(item.getAuthorname()));
        vh.Author.setTypeface(custom_font3);
        vh.Excerpts.setText(Html.fromHtml(item.excerpt.rendered));
        vh.Excerpts.setTypeface(custom_font3);
        GetDateTime();

        if (item.imagePath != null) {

            File file = ImageLoader.getInstance().getDiskCache().get(item.getImagePath());
            if (!file.exists()) {
                DisplayImageOptions options = new DisplayImageOptions.Builder()
                        .cacheOnDisk(true)
                        .build();
                imgloader.displayImage(item.imagePath, vh.Art, options);
            } else {
                //imgLoader.DisplayImage(post.imagePath, vh.Art, options);
                vh.Art.setImageURI(android.net.Uri.parse(file.getAbsolutePath()));
            }
        } else {
            String imageUri = "drawable://" + R.drawable.ic_home_black_24dp;
            DisplayImageOptions options = new DisplayImageOptions.Builder()
                    .cacheOnDisk(true)
                    .build();
            imgloader.displayImage(imageUri, vh.Art, options);
        }

        final String dateTime = vh.date.getText().toString();
        //vh.Art.setTag(R.id.postimage, item);
        vh.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                intent = new Intent(context, UnfoldableDetailsActivity.class);
                intent.putExtra("BlogId", item.id);
                intent.putExtra("Title", item.title.rendered);
                intent.putExtra("Author", item.authorname);
                intent.putExtra("Date", dateTime);
                intent.putExtra("Excepts", item.excerpt.rendered);
                intent.putExtra("Image", item.imagePath);
                context.startActivity(intent);
            }
        });
    }
    private void GetDateTime() {

        String input = item.getDate();
        String Postdatetime = null;
        String currentDateTime = null;

        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        DateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy KK:mm a");
        try {
            System.out.println(outputFormat.format(inputFormat.parse(input)));
            Postdatetime = outputFormat.format(inputFormat.parse(input));
            vh.date.setText(outputFormat.format(inputFormat.parse(input)));

            SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy KK:mm a");
            currentDateTime = sdf1.format(new Date());
            System.out.print("Time.........." + currentDateTime + "....End");

        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy KK:mm a");

        Date d1 = null;
        Date d2 = null;

        try {
            d1 = format.parse(Postdatetime);
            d2 = format.parse(currentDateTime);
            //in milliseconds
            long diff = d2.getTime() - d1.getTime();
            long diffSeconds = diff / 1000 % 60;
            long diffMinutes = diff / (60 * 1000) % 60;
            long diffHours = diff / (60 * 60 * 1000) % 24;
            long diffDays = diff / (24 * 60 * 60 * 1000);

            System.out.print(diffDays + " days, ");
            System.out.print(diffHours + " hours, ");
            System.out.print(diffMinutes + " minutes, ");
            System.out.print(diffSeconds + " seconds.");

            if(diffDays > 0)
            {
                vh.date.setText(Html.fromHtml(String.valueOf(diffDays + "d")));
            }
            else if(diffDays == 0)
            {
                vh.date.setText(Html.fromHtml(String.valueOf(diffHours + "h")));
            }
            else if(diffDays == 0 && diffHours == 0)
            {
                vh.date.setText(Html.fromHtml(String.valueOf(diffMinutes + "m")));
            }
            else if(diffDays == 0 && diffHours == 0 && diffMinutes == 0)
            {
                vh.date.setText(Html.fromHtml(String.valueOf(diffMinutes + "s")));
            }
            else
            {
                vh.date.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
