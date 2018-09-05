package Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import org.apache.commons.lang3.StringEscapeUtils;

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
import static com.google.android.gms.internal.zzahn.runOnUiThread;

public class PostContentAdapter extends ItemsAdapter<Post, PostContentAdapter.ViewHolder>
{
    public ImageLoader imgloader;
    Context context;
    private int index;
    List<Post> post;
    String postid,input;
    Post item;
    Typeface custom_font3;
    ViewHolder vh1;

    float txtsize;

    public PostContentAdapter(List<Post> posts,Activity context) {
        this.post = posts;
        this.context = context;
        setItemsList(this.post);
        imgloader = ImageLoader.getInstance();
    }
          @Override
           public int getCount() {
              return post == null ? 0 : post.size();
           }
           public void setData(List<Post> questionList) {
               this.post=questionList;
               setItemsList(this.post);
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
                inflate(R.layout.post_listing, null);

        PostContentAdapter.ViewHolder vh = new PostContentAdapter.ViewHolder(view);

        if (!imgloader.isInited()) {
            imgloader.init(ImageLoaderConfiguration
                    .createDefault(parent.getContext()));
        }
        return vh;
    }
    @Override
    protected void onBindHolder(PostContentAdapter.ViewHolder vh, final int position) {
        vh1 = vh;
        AssetManager am = context.getApplicationContext().getAssets();
        Typeface custom_font = Typeface.createFromAsset(am, "font/Lora-Bold.ttf");
        Typeface custom_font1 = Typeface.createFromAsset(am, "font/WorkSans-Regular.ttf");
        custom_font3 = Typeface.createFromAsset(am, "font/Martel-Bold.ttf");

        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int pxWidth = displayMetrics.widthPixels;
        float dpWidth = pxWidth / displayMetrics.density;
        int pxHeight = displayMetrics.heightPixels;
        float dpHeight = pxHeight / displayMetrics.density;
        txtsize=dpHeight*0.026f;

        item = getItem(position);
        postid = item.getPostId();
        String title = item.getTitle();
        String decodedtitle= StringEscapeUtils.unescapeHtml3(title);
        vh1.Title.setText(decodedtitle);
        vh1.Title.setTypeface(custom_font);
        vh1.Category.setText(Html.fromHtml("#" + item.getCategory().getName()));
        vh1.Category.setTypeface(custom_font1);
        vh1.Author.setText(Html.fromHtml(item.getAuthor().getName()));
        vh1.Author.setTypeface(custom_font3);
        vh.Author.setTextSize(txtsize);
        vh1.Excerpts.setText(Html.fromHtml(item.getExcerpt()));
        vh1.Excerpts.setTypeface(custom_font3);
        vh.Excerpts.setTextSize(txtsize);
         input = item.getDate();
        GetDateTime();

        if (item.getFeaturedMedia().getURL() != null) {

            File file = ImageLoader.getInstance().getDiskCache().get(item.getFeaturedMedia().getURL());
            if (!file.exists()) {
                DisplayImageOptions options = new DisplayImageOptions.Builder()
                        .cacheOnDisk(true)
                        .build();
                imgloader.displayImage(item.getFeaturedMedia().getURL(), vh1.Art, options);
            } else {
                //imgLoader.DisplayImage(post.imagePath, vh.Art, options);
                vh1.Art.setImageURI(android.net.Uri.parse(file.getAbsolutePath()));
            }
        } else {
            String imageUri = "drawable://" + R.drawable.ic_home_black_24dp;
            DisplayImageOptions options = new DisplayImageOptions.Builder()
                    .cacheOnDisk(true)
                    .build();
            imgloader.displayImage(imageUri, vh1.Art, options);
        }

        final String dateTime = vh1.date.getText().toString();
        //vh.Art.setTag(R.id.postimage, item);
        vh1.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                intent = new Intent(context, UnfoldableDetailsActivity.class);
                Post tempitem = getItem(position);
                String posttempid = tempitem.getPostId();
                intent.putExtra("Position",position);
                intent.putExtra("BlogId", posttempid);
                intent.putExtra("Title", tempitem.getTitle());
                intent.putExtra("Author", tempitem.getAuthor().getName());
                intent.putExtra("Date", dateTime);
                intent.putExtra("Excepts", tempitem.getExcerpt());
                intent.putExtra("content",tempitem.getContent());
                intent.putExtra("Image", tempitem.getFeaturedMedia().getURL());
                context.startActivity(intent);
            }
        });
    }
    private void GetDateTime() {

        String Postdatetime = null;
        String currentDateTime = null;

        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        DateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy KK:mm a");
        try {
            System.out.println(outputFormat.format(inputFormat.parse(input)));
            Postdatetime = outputFormat.format(inputFormat.parse(input));
            vh1.date.setText(outputFormat.format(inputFormat.parse(input)));
            vh1.date.setTypeface(custom_font3);
            vh1.date.setTextSize(txtsize);
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
                vh1.date.setText(Html.fromHtml(String.valueOf(diffDays + "d")));
            }
            else if(diffDays == 0 && diffHours == 0)
            {
                vh1.date.setText(Html.fromHtml(String.valueOf(diffMinutes + "m")));
            }
            else if(diffDays == 0 && diffHours == 0 && diffMinutes == 0)
            {
                vh1.date.setText(Html.fromHtml(String.valueOf(diffMinutes + "s")));
            }
            else
            {
                vh1.date.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
