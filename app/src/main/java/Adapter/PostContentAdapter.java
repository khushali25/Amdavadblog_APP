package Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alexvasilkov.android.commons.adapters.ItemsAdapter;
import com.alexvasilkov.android.commons.ui.Views;
import com.example.xps.amdavadblog_app.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.zip.Inflater;

import Model.Post;

import static Model.Post.getItems;


public class PostContentAdapter extends ItemsAdapter<Post, PostContentAdapter.ViewHolder>
        implements View.OnClickListener {

    public ImageLoader imgloader;
    Context context;
    private int index;

    public PostContentAdapter(Activity context) {
        this.context = context;
        setItemsList(getItems());
        //  setItemsList(getItems1());
        imgloader = ImageLoader.getInstance();
    }

    @Override
    public void onClick(View view) {

    }

    static class ViewHolder extends ItemsAdapter.ViewHolder {
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
            date = itemView.findViewById(R.id.date);
        }
    }

    @Override
    protected PostContentAdapter.ViewHolder onCreateHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.post_listing, null, false);
        PostContentAdapter.ViewHolder vh = new PostContentAdapter.ViewHolder(view);
        imgloader.init(ImageLoaderConfiguration
                .createDefault(parent.getContext()));
        vh.Art.setOnClickListener(this);
        return vh;
    }

    @Override
    protected void onBindHolder(PostContentAdapter.ViewHolder viewHolder, int position) {

        PostContentAdapter.ViewHolder vh = viewHolder;
        AssetManager am = context.getApplicationContext().getAssets();
        Typeface custom_font = Typeface.createFromAsset(am, "font/Amaranth-Bold.ttf");
        Typeface custom_font1 = Typeface.createFromAsset(am, "font/WorkSans-Regular.ttf");
        Typeface custom_font2 = Typeface.createFromAsset(am, "font/Martel-Regular.ttf");
        Typeface custom_font3 = Typeface.createFromAsset(am, "font/Martel-Bold.ttf");

        final Post item = getItem(position);
        vh.Title.setText(item.getTitle());
        vh.Title.setTypeface(custom_font);
        vh.Category.setText(item.getCategory());
        vh.Category.setTypeface(custom_font1);
        vh.Author.setText(item.getAuthor());
        vh.Author.setTypeface(custom_font3);
        vh.Excerpts.setText(item.getExcerpt());
        vh.Excerpts.setTypeface(custom_font2);

        //getDateWithServerTimeStamp();
        vh.date.setText(item.getDate());
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
            String imageUri = "drawable://" + R.drawable.demo;
            DisplayImageOptions options = new DisplayImageOptions.Builder()
                    .cacheOnDisk(true)
                    .build();
            imgloader.displayImage(imageUri, vh.Art, options);
        }
    }

    private void getDateWithServerTimeStamp() {
        Date now = new Date();
        Instant format = null;
        String format2 = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            format = Instant.parse("2018-06-26T16:17:55");
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(String.valueOf(format), Locale.US);
            String formattedNow = simpleDateFormat.format(now);
            String dateConvertBack = null;
            try {
                dateConvertBack = String.valueOf(simpleDateFormat.parse(formattedNow));
                // post.setDate(dateConvertBack);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
       // if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            format2 = "2018-06-26T16:17:55";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format2, Locale.US);
            String formattedNow = simpleDateFormat.format(now);
            String dateConvertBack = null;
            try {
                dateConvertBack = String.valueOf(simpleDateFormat.parse(formattedNow));
                // post.setDate(dateConvertBack);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
      //  }



    }
}



