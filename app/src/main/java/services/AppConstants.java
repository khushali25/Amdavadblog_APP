package services;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.support.annotation.RequiresApi;
import android.support.v4.content.FileProvider;

import com.crashlytics.android.Crashlytics;
import com.example.xps.amdavadblog_app.BuildConfig;
import com.example.xps.amdavadblog_app.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Paths;
import java.util.regex.Pattern;

import static com.facebook.FacebookSdk.getApplicationContext;

public class AppConstants
{
    static String CacheFilePersonalPath;
    static String PostsCacheFilePath;
    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
    static Context context;
    public enum CacheType
    {
        Category,
        Posts,
        PostDetail;
        public static Context getContext() {
            //  return instance.getApplicationContext();
            return context;
        }
        public static final int SIZE = java.lang.Integer.SIZE;

        public int getValue()
        {

            return this.ordinal();
        }

        public static CacheType forValue(int value)
        {

            return values()[value];
        }
    }

   // @RequiresApi(api = Build.VERSION_CODES.O)
    public static String getPostsCacheFilePath() {
        String data = null;
        context = getApplicationContext();
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                   // data = Paths.get(getCacheFilePersonalPath(), "posts.json").toString();
                    data = new File(getCacheFilePersonalPath(),"posts.json").toString();
                   //final Uri uri = FileProvider.getUriForFile(context,"posts.json", new File(getCacheFilePersonalPath()));
                   //data = uri.toString();

                }
            }
            catch (Exception ex) {
                Crashlytics.logException(ex);
            }

        return data;
    }

//    @SuppressLint("NewApi")
//    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static String getPostsCacheFilePathByCategory(int categoryId) {
        String data = null;
        context = getApplicationContext();
        try {
            File toInstall = new File(getCacheFilePersonalPath());
            String fileName = "posts_" + categoryId + ".json";
            data = new File(toInstall,fileName).toString();
           // final String uri = String.valueOf(FileProvider.getUriForFile(context,fileName, toInstall));
          //  data = uri.toString();
        }
        catch (Exception ex) {
            Crashlytics.logException(ex);
        }
        return data;
    }

    public static String getCacheFilePersonalPath() {
        String data = null;
        File main = null;
        try {
          main = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
        }
        catch (Exception ex) {
            Crashlytics.logException(ex);
        }
        return main.getPath();
    }


}
