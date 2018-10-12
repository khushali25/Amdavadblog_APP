package services;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Environment;

import com.crashlytics.android.Crashlytics;

import java.io.File;
import java.nio.file.Paths;

public class AppConstants
{
    static String CacheFilePersonalPath;
    static String PostsCacheFilePath;

    public enum CacheType
    {
        Category,
        Posts,
        PostDetail;

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
    @SuppressLint("NewApi")
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static String getPostsCacheFilePath() {
        String data = null;
            try {
                data = Paths.get(getCacheFilePersonalPath(), "posts.json").toString();
            }
            catch (Exception ex) {
                Crashlytics.logException(ex);
            }

        return data;
    }

    @SuppressLint("NewApi")
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static String getPostsCacheFilePathByCategory(int categoryId) {
        String data = null;
        try {
            String fileName = "posts_" + categoryId + ".json";
            data = Paths.get(getCacheFilePersonalPath(), fileName).toString();
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
//        File[] t = main.getParentFile().listFiles();
//
//        for (File dir  : t) {
//             data = dir.getAbsolutePath();
//        }
        }
        catch (Exception ex) {
            Crashlytics.logException(ex);
        }
        return main.getPath();
    }


}
