//package Helper;
//
//
//import okhttp3.Protocol;
//
//import java.io.IOException;
//import java.net.ConnectException;
//import java.net.UnknownHostException;
//
//import okhttp3.Interceptor;
//import okhttp3.MediaType;
//import okhttp3.Request;
//import okhttp3.Response;
//import okhttp3.ResponseBody;
//import services.CacheManager;
//
//public class CacheInterceptor implements Interceptor {
//    private final CacheManager mCacheManager;
//    private final Reachability mReachability;
//
//    public CacheInterceptor(CacheManager cacheManager, Reachability reachability) {
//        mCacheManager = cacheManager;
//        mReachability = reachability;
//    }
//
//    @Override
//    public Response intercept(Chain chain) throws IOException {
//        Request request = chain.request();
//        String key = request.url().toString();
//
//        Response response;
//        if (mReachability.isConnected()) {
//            try {
//                response = chain.proceed(request);
//                Response newResponse = response.newBuilder().build();
//
//                if (response.isSuccessful()) {
//                    if (response.code() == 204) {
//                        return response;
//                    }
//                    // save to cache this success model.
//                    mCacheManager.getCache().put(key, newResponse.body().string());
//
//                    // now we know that we definitely have a cache hit.
//                    return getCachedResponse(key, request);
//                }else if (response.code() >= 500) { // accommodate all server errors
//
//                    // check if there is a cache hit or miss.
//                    if (isCacheHit(key)) {
//                        // if data is in cache, the return the data from cache.
//                        return getCachedResponse(key, request);
//                    }else {
//                        // if it's a miss, we can't do much but return the server state.
//                        return response;
//                    }
//
//                }else { // if there is any client side error
//                    // forward the response as it is to the business layers to handle.
//                    return response;
//                }
//            } catch (ConnectException | UnknownHostException e) {
//                // Internet connection exception.
//                e.printStackTrace();
//            }
//        }
//
//        // if somehow there is an internet connection error
//        // check if the data is already cached.
//        if (isCacheHit(key)) {
//            return getCachedResponse(key, request);
//        }else {
//            // if the data is not in the cache we'll throw an internet connection error.
//            throw new UnknownHostException();
//        }
//    }
//
//    private Response getCachedResponse(String url, Request request) {
//        String cachedData = mCacheManager.getCache().get(url);
//
//        return new Response.Builder().code(200)
//                .body(ResponseBody.create(MediaType.parse("application/json"), cachedData))
//                .request(request)
//                .protocol(Protocol.HTTP_1_1)
//                .build();
//    }
//
//    public boolean isCacheHit(String key) {
//        return mCacheManager.getCache().get(key) != null;
//    }}