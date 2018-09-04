package Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alexvasilkov.android.commons.adapters.ItemsAdapter;
import com.example.xps.amdavadblog_app.R;
import com.example.xps.amdavadblog_app.UnfoldableDetailsActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import Model.PoseSearch;
import Model.Post;

public class PostContentAdapterSearch extends ItemsAdapter<Post, PostContentAdapterSearch.ViewHolder>
{
    public ImageLoader imgloader;
    Context context;
    private int index;
    List<PoseSearch> posts;
    String postid,input;
    PoseSearch item;
    Typeface custom_font3;
    PostContentAdapterSearch.ViewHolder vh1;

    public PostContentAdapterSearch(List<PoseSearch> posts,Activity context) {
        this.posts = posts;
        this.context = context;
        setItemsList1(this.posts);
        //setItemsList(getItems());
        //  setItemsList(getItems1());
        imgloader = ImageLoader.getInstance();
    }

    public void setItemsList1(List<PoseSearch> post) {
        posts = post;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return posts == null ? 0 : posts.size();
    }
    public void setData1(List<PoseSearch> questionList) {
        this.posts=questionList;
        setItemsList1(this.posts);
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
    protected PostContentAdapterSearch.ViewHolder onCreateHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.post_listing, null);
        PostContentAdapterSearch.ViewHolder vh = new PostContentAdapterSearch.ViewHolder(view);
        if (!imgloader.isInited()) {
            imgloader.init(ImageLoaderConfiguration
                    .createDefault(parent.getContext()));
        }
        return vh;
    }
    @Override
    protected void onBindHolder(PostContentAdapterSearch.ViewHolder vh, final int position) {
        vh1 = vh;
        AssetManager am = context.getApplicationContext().getAssets();
        Typeface custom_font = Typeface.createFromAsset(am, "font/Lora-Bold.ttf");
        Typeface custom_font1 = Typeface.createFromAsset(am, "font/WorkSans-Regular.ttf");
        custom_font3 = Typeface.createFromAsset(am, "font/Martel-Bold.ttf");

        item = getItem1(position);
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

            File file = ImageLoader.getInstance().getDiskCache().get(item.imagePath);
            if (!file.exists()) {
                DisplayImageOptions options = new DisplayImageOptions.Builder()
                        .cacheOnDisk(true)
                        .build();
                imgloader.displayImage(item.imagePath, vh1.Art, options);
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
                PoseSearch tempitem = getItem1(position);
                int posttempid = tempitem.getId();
                intent.putExtra("Position",position);
                intent.putExtra("BlogId", posttempid);
                intent.putExtra("Title", tempitem.title.rendered);
                intent.putExtra("Author", tempitem.getAuthorname());
                intent.putExtra("Date", dateTime);
                intent.putExtra("Excepts", tempitem.excerpt.rendered);
                intent.putExtra("content",tempitem.content.getRendered());
                intent.putExtra("Image", item.imagePath);
                context.startActivity(intent);
            }
        });
    }
    private PoseSearch getItem1(int position) {
        if (posts == null || position < 0 || position >= posts.size()) {
            return null;
        }
        return posts.get(position);
    }

    private void GetDateTime() {
        input = item.getDate();
        String Postdatetime = null;
        String currentDateTime = null;

        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        DateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy KK:mm a");
        try {
            System.out.println(outputFormat.format(inputFormat.parse(input)));
            Postdatetime = outputFormat.format(inputFormat.parse(input));
            vh1.date.setText(outputFormat.format(inputFormat.parse(input)));
            vh1.date.setTypeface(custom_font3);
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
