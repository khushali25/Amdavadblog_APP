package Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alexvasilkov.android.commons.adapters.ItemsAdapter;
import com.crashlytics.android.Crashlytics;
import com.example.xps.amdavadblog_app.R;
import com.example.xps.amdavadblog_app.UnfoldableDetailsActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.apache.commons.lang3.StringEscapeUtils;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import Model.PostSearch;
import Model.Post;

public class PostContentAdapterSearch extends ItemsAdapter<Post, PostContentAdapterSearch.ViewHolder>
{
    public ImageLoader imgloader;
    ImageLoaderConfiguration config;
    Context context;
    private int index;
    List<PostSearch> posts;
    String input;
    PostSearch item;
    Typeface custom_font3;
    PostContentAdapterSearch.ViewHolder vh1;

    public PostContentAdapterSearch(List<PostSearch> posts, Activity context) {
        try {
            this.posts = posts;
            this.context = context;
            setItemsList1(this.posts);
            imgloader = ImageLoader.getInstance();
        }
        catch(Exception ex)
        {
            Crashlytics.logException(ex);
        }

    }

    public void setItemsList1(List<PostSearch> post) {
        try {
            posts = post;
            notifyDataSetChanged();
        }
        catch(Exception ex)
        {
            Crashlytics.logException(ex);
        }

    }

    @Override
    public int getCount() {
        return posts == null ? 0 : posts.size();
    }
    public void setData1(List<PostSearch> questionList) {
        try {
            this.posts = questionList;
            setItemsList1(this.posts);
        }
        catch(Exception ex)
        {
            Crashlytics.logException(ex);
        }

    }
    class ViewHolder extends ItemsAdapter.ViewHolder {
        public TextView Title;
        public ImageView Art;
        public TextView Category;
        public TextView Author;
        public TextView Excerpts;
        public TextView date;

        public ViewHolder(View itemView) {
            super(itemView);
            try {
                Title = itemView.findViewById(R.id.posttitletext);
                Art = itemView.findViewById(R.id.postimage);
                Title = itemView.findViewById(R.id.posttitletext);
                Category = itemView.findViewById(R.id.category);
                Author = itemView.findViewById(R.id.author);
                Excerpts = itemView.findViewById(R.id.excerpt);
                date = itemView.findViewById(R.id.date1);
            }
            catch(Exception ex)
            {
                Crashlytics.logException(ex);
            }

        }
    }
    @Override
    protected PostContentAdapterSearch.ViewHolder onCreateHolder(ViewGroup parent, int viewType) {
        PostContentAdapterSearch.ViewHolder vh = null;
        try {

            View view = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.post_listing, null);
            vh = new PostContentAdapterSearch.ViewHolder(view);
            if (!imgloader.isInited()) {
                imgloader.init(ImageLoaderConfiguration
                        .createDefault(parent.getContext()));
            }
        }
        catch(Exception ex)
        {
            Crashlytics.logException(ex);
        }

        return vh;
    }
    @Override
    protected void onBindHolder(PostContentAdapterSearch.ViewHolder vh, final int position) {
        try {
            vh1 = vh;
            AssetManager am = context.getApplicationContext().getAssets();
            Typeface custom_font = Typeface.createFromAsset(am, "font/Lora-Bold.ttf");
            Typeface custom_font1 = Typeface.createFromAsset(am, "font/WorkSans-Regular.ttf");
            custom_font3 = Typeface.createFromAsset(am, "font/Martel-Bold.ttf");

            item = getItem1(position);
            String title = item.title.rendered;
            String decodedtitle = StringEscapeUtils.unescapeHtml3(title);
            vh1.Title.setText(decodedtitle);
            vh.Title.setTypeface(custom_font);
            vh.Category.setText(Html.fromHtml("#" + item.getCategoryname()));
            vh.Category.setTypeface(custom_font1);
            vh.Author.setText(Html.fromHtml(item.getAuthorname()));
            vh.Author.setTypeface(custom_font3);
            vh.Excerpts.setText(Html.fromHtml(item.excerpt.rendered));
            vh.Excerpts.setTypeface(custom_font3);
            GetDateTime();
            if (item.getImagePath() != null) {

                final File file = ImageLoader.getInstance().getDiskCache().get(item.getImagePath());
                if (!file.exists()) {

                    final DisplayImageOptions options = new DisplayImageOptions.Builder()
                            .delayBeforeLoading(0)
                            .resetViewBeforeLoading(true)
                            .cacheInMemory(true)
                            .cacheOnDisk(true)
                            .bitmapConfig(Bitmap.Config.RGB_565)
                            .imageScaleType(ImageScaleType.EXACTLY)
                            .resetViewBeforeLoading(true)
                            .build();

                    config = new ImageLoaderConfiguration.Builder(context)
                            .denyCacheImageMultipleSizesInMemory()
                            .defaultDisplayImageOptions(options)
                            .diskCacheExtraOptions(480, 320, null)
                            .threadPoolSize(10)
                            .build();
                    imgloader.displayImage(item.getImagePath(), vh1.Art, options);

                } else {

                    // assert imageLayout != null;
                    // ImageView imageView = (ImageView) view.findViewById(R.id.image);

                    ImageLoader.getInstance().displayImage(item.getImagePath(), vh1.Art,
                            new ImageLoadingListener() {
                                @Override
                                public void onLoadingStarted(String arg0, View arg1) {
                                    // TODO Auto-generated method stub
                                    //vh1.Art.setImageResource(R.drawable.ic_access_time_black_24dp);


                                }
                                @Override
                                public void onLoadingFailed(String arg0, View arg1,
                                                            FailReason arg2) {

                                }
                                @Override
                                public void onLoadingComplete(String arg0, View arg1,
                                                              Bitmap arg2) {
                                    // TODO Auto-generated method stub
                                    vh1.Art.setImageURI(android.net.Uri.parse(file.getAbsolutePath()));
                                }
                                @Override
                                public void onLoadingCancelled(String arg0, View arg1) {
                                    // TODO Auto-generated method stub
                                    vh1.Art.setImageResource(R.drawable.ic_access_time_black_24dp);
                                }
                            });
                    // MemoryCacheUtils.removeFromCache(item.getFeaturedMedia().getURL(), imgloader.getMemoryCache());
                    vh1.Art.setImageURI(android.net.Uri.parse(file.getAbsolutePath()));
                }
            } else {
                String imageUri = "drawable://" + R.drawable.ic_home_black_24dp;
                DisplayImageOptions options = new DisplayImageOptions.Builder()
                        .delayBeforeLoading(0)
                        .resetViewBeforeLoading(true)
                        .cacheInMemory(true)
                        .cacheOnDisk(true)
                        .bitmapConfig(Bitmap.Config.RGB_565)
                        .imageScaleType(ImageScaleType.EXACTLY)
                        .resetViewBeforeLoading(true)
                        .build();
                imgloader.displayImage(imageUri, vh1.Art, options);
            }
        }
        catch (Exception ex)
        {
            Crashlytics.logException(ex);
        }
//            if (item.imagePath != null) {
//
//                File file = ImageLoader.getInstance().getDiskCache().get(item.imagePath);
//                if (!file.exists()) {
//                    DisplayImageOptions options = new DisplayImageOptions.Builder()
//                            .cacheOnDisk(true)
//                            .build();
//                    imgloader.displayImage(item.imagePath, vh1.Art, options);
//                } else {
//                    vh1.Art.setImageURI(android.net.Uri.parse(file.getAbsolutePath()));
//                }
//            } else {
//                String imageUri = "drawable://" + R.drawable.ic_home_black_24dp;
//                DisplayImageOptions options = new DisplayImageOptions.Builder()
//                        .cacheOnDisk(true)
//                        .build();
//                imgloader.displayImage(imageUri, vh1.Art, options);
//            }
//
//
//        }
//        catch(Exception ex)
//        {
//            Crashlytics.logException(ex);
//        }
        vh1.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    final String dateTime = vh1.date.getText().toString();
                    Intent intent;
                    intent = new Intent(context, UnfoldableDetailsActivity.class);
                    intent.putExtra("I_CAME_FROM", "searchactivity");
                    PostSearch tempitem = getItem1(position);
                    int posttempid = tempitem.getId();
                    intent.putExtra("Position", position);
                    intent.putExtra("BlogId", posttempid);
                    intent.putExtra("Title", tempitem.title.rendered);
                    intent.putExtra("Author", tempitem.getAuthorname());
                    intent.putExtra("Date", dateTime);
                    intent.putExtra("Excepts", tempitem.excerpt.rendered);
                    intent.putExtra("content", tempitem.content.getRendered());
                    intent.putExtra("Image", tempitem.getImagePath());
                    context.startActivity(intent);
                }
                catch(Exception ex)
                {
                    Crashlytics.logException(ex);
                }
            }
        });
    }
    private PostSearch getItem1(int position) {
        try {
            if (posts == null || position < 0 || position >= posts.size()) {
                return null;
            }
        }
        catch(Exception ex)
        {
            Crashlytics.logException(ex);
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
            Crashlytics.logException(e);
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

            if(diffSeconds > 0 && diffMinutes == 0 && diffHours == 0 && diffDays == 0)
            {
                vh1.date.setText(Html.fromHtml(String.valueOf(diffSeconds + "sec" + " " + "ago")));
            }
            else if(diffMinutes > 0 && diffHours == 0 && diffDays == 0)
            {
                vh1.date.setText(Html.fromHtml(String.valueOf(diffMinutes + "min" + " " + "ago")));
            }
            else if(diffHours > 0 &&diffDays == 0)
            {
                vh1.date.setText(Html.fromHtml(String.valueOf(diffHours + "hr" + " " + "ago")));
            }
            else
            {
                vh1.date.setText(Html.fromHtml(String.valueOf(diffDays + "days" + " " + "ago")));
            }
        } catch (Exception e) {
            Crashlytics.logException(e);
            e.printStackTrace();
        }

    }
}
