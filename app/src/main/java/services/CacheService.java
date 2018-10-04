package services;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;

import com.google.firebase.crash.FirebaseCrash;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import Core.Helper.ApiService;
import Core.Helper.SynchronousCallAdapterFactory;
import Model.Post;
import Model.StartJsonDataClass;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.facebook.FacebookSdk.getApplicationContext;

public class CacheService {
    static List AllPost;
    static List<Post> currentPost;
    boolean loading = false;
    static boolean reachedMax = false;
    private static final int REQUEST = 112;
    static Context context;
    static ActivityManager am = null;

    public static Context getContext() {
        //  return instance.getApplicationContext();
        return context;
    }

    static Retrofit retrofitallpost = new Retrofit.Builder()
            .baseUrl("http://api.amdavadblog.com/amdblog/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(SynchronousCallAdapterFactory.create())
            .build();

    @SuppressLint("NewApi")
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static List GetAllPostnew(boolean isForce, final int page) throws IOException {
        Call<StartJsonDataClass> call = null;
        context = getApplicationContext();

        final ApiService apiService = retrofitallpost.create(ApiService.class);
        final String filePath = AppConstants.getPostsCacheFilePath();

        if (!IsRequiredToReadFromCache(filePath) || isForce) {
            call = apiService.getAllPostPerPage(page);
            Response<StartJsonDataClass> response = null;
            try {
                response = call.execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (response.isSuccessful()) {
                // Toast.makeText(this, "server returned so many repositories: " + response.body().size(), Toast.LENGTH_SHORT).show();
                // todo display the data instead of just a toast
                if (page == 1) {
                    AllPost = response.body().getData();

                } else {
                    currentPost = response.body().getData();
                    AllPost.addAll(currentPost);
                    if (currentPost.size() < 10 || currentPost.isEmpty()) {
                        reachedMax = true;
                    }
                }
            } else {
            }
            Gson gsonBuilder = new GsonBuilder().create();
            String jsonFromJavaArrayList = gsonBuilder.toJson(currentPost);
            String json = jsonFromJavaArrayList;
            if (page == 1) {
                try {
                    SaveData(filePath, gsonBuilder.toJson(AllPost));

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                if (json.length() > 5)

                    try {
                        AppendData(filePath, json);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }

        } else {
            String json = GetData(filePath);
            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<Post>>() {}.getType();
            AllPost = new Gson().fromJson(json, listType);

        }

        return AllPost;

    }

    public static List GetPostByCategoryId(int page, int categoryId, boolean b) throws IOException {
        Call<StartJsonDataClass> call = null;
        context = getApplicationContext();

        final ApiService apiService = retrofitallpost.create(ApiService.class);
        final String filePath = AppConstants.getPostsCacheFilePathByCategory(categoryId);

        if (!IsRequiredToReadFromCache(filePath) || b) {
            call = apiService.getAllPostByCategoryId(page, categoryId);
            Response<StartJsonDataClass> response = null;
            try {
                response = call.execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (response.isSuccessful()) {
                // Toast.makeText(this, "server returned so many repositories: " + response.body().size(), Toast.LENGTH_SHORT).show();
                // todo display the data instead of just a toast
                if (page == 1) {
                    AllPost = response.body().getData();

                } else {
                    currentPost = response.body().getData();
                    AllPost.addAll(currentPost);
                    if (currentPost.size() < 10 || currentPost.isEmpty()) {
                        reachedMax = true;
                    }
                }
            } else {
            }
            Gson gsonBuilder = new GsonBuilder().create();
            String jsonFromJavaArrayList = gsonBuilder.toJson(currentPost);
            String json = jsonFromJavaArrayList;
            if (page == 1) {
                try {
                    SaveData(filePath, gsonBuilder.toJson(AllPost));

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                if (json.length() > 5)

                    try {
                        AppendData(filePath, json);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }

        } else {
            String json = GetData(filePath);
            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<Post>>() {}.getType();
            AllPost = new Gson().fromJson(json, listType);
        }

        return AllPost;

    }

    @SuppressLint("NewApi")
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static String GetData(String filePath) throws IOException {
        String content = "";
        int i;
        char c;
        String content2 = new String(Files.readAllBytes(Paths.get(filePath)));
        return content2;

    }

    @SuppressLint("NewApi")
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static void AppendData(String filePath, String json) throws IOException {
        String content = "";
        String content2 = new String(Files.readAllBytes(Paths.get(filePath)));

        content2 = content2.replace("]", ",");
        json = json.substring(1);
        String finalData = content2 + json;
        if ((new File(filePath)).isFile()) {
            (new File(filePath)).delete();
        }
        Files.write(Paths.get(filePath), finalData.getBytes());

        BasicFileAttributes attr = null;
        try {
            attr = Files.readAttributes(Paths.get(filePath), BasicFileAttributes.class);
            Date creationDate = new Date(attr.creationTime().to(TimeUnit.MILLISECONDS));
            Files.setAttribute(Paths.get(filePath), "creationTime", FileTime.fromMillis(creationDate.getTime()));

        } catch (IOException e) {
            System.out.println("error! " + e.getMessage());
        }
    }

    @SuppressLint("NewApi")
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static void SaveData(String filePath, String json) throws IOException {

        if ((new File(filePath)).isFile()) {
            (new File(filePath)).delete();
        }

        Files.write(Paths.get(filePath), json.getBytes());

        //Calendar c = Calendar.getInstance();
        BasicFileAttributes attr = null;
        try {
            attr = Files.readAttributes(Paths.get(filePath), BasicFileAttributes.class);
            Date creationDate = new Date(attr.creationTime().to(TimeUnit.MILLISECONDS));
            Files.setAttribute(Paths.get(filePath), "creationTime", FileTime.fromMillis(creationDate.getTime()));

        } catch (IOException e) {
            System.out.println("error! " + e.getMessage());
        }
    }

    @SuppressLint("NewApi")
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static boolean IsRequiredToReadFromCache(String filePath) {
        //return true;
        int frequency = 1;
        boolean result = false;
        try {
            if ((new File(filePath)).isFile()) {
                    File f = new File(filePath);
                    Date date = new Date();
                    Date filedate = new Date(f.lastModified());
                    LocalDateTime localdatetime = LocalDateTime.now();

                long diff = new Date().getTime() - filedate.getTime();
                long seconds = diff / 1000;
                long minutes = seconds / 60;

                    if (minutes < frequency) {
                        return true;            //read from cache
                    }
            }
        }
          catch(Exception ex)
            {
              //  FirebaseCrash.Report(ex);
            }
            return result;
        }
    }