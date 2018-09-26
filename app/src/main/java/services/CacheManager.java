//package services;
//
//import android.content.Context;
//import android.util.Log;
//
//import com.example.xps.amdavadblog_app.BuildConfig;
//import com.squareup.okhttp.internal.DiskLruCache;
//
//import java.io.File;
//import java.io.IOException;
//import java.math.BigInteger;
//import java.security.MessageDigest;
//import java.security.NoSuchAlgorithmException;
//
//import Core.Helper.ApiService;
//import Interface.Cache;
//import retrofit2.Retrofit;
//
//public class CacheManager implements GsonResponseListener {
//
//    private static final String URL = "https://jsonplaceholder.typicode.com/";
//    private static ApiManager apiManager = new ApiManager();
//
//    private ApiService apiService;
//
//    static ApiService getService(){
//        if (apiManager.apiService == null) {
//            apiManager.apiService = apiManager.createRetrofit().create(ApiService.class);
//        }
//        return apiManager.apiService;
//    }
//
//    private Retrofit createRetrofit(){
//        return new Retrofit.Builder()
//                .baseUrl(URL)
//                .client(createClient())
//                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                .addConverterFactory(GsonCacheableConverter.create(this))
//                .build();
//    }
//
//    private OkHttpClient createClient(){
//        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
//        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
//
//        OkHttpClient.Builder builder = new OkHttpClient.Builder();
//        builder.addInterceptor(logging);
//        return builder.build();
//    }
//
//    @SuppressWarnings("unchecked")
//    @Override
//    public void onCacheableResponse(Class type, Object responseBody) {
//        if (responseBody instanceof Collection) BoxManager.getStore().boxFor(type).put((ArrayList) responseBody);
//        else BoxManager.getStore().boxFor(type).put(responseBody);    }
//
//}