package Core;

import android.provider.ContactsContract;

import java.util.List;

import Model.Author;
import Model.Category;
import Model.Media;
import Model.PoseSearch;
import Model.Post;
import Model.StartJsonDataClass;
import Model.Subscribe;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface WordPressService {

    @GET("GetAllPosts")
    Call<StartJsonDataClass> getAllPostPerPage(@Query("page") int PostId);

    @GET("GetAllPostsByCategoryId")
    Call<StartJsonDataClass> getAllPostByCategoryId(@Query("page") int PostId, @Query("CategoryId") int catId);
//
//    @GET("GetPostDetailById")
//    Call<StartJsonDataClass> getPostDetailById(@Query("page") String PostId);

   @GET("/wp-json/wp/v2/media/{id}")
    Media getFeaturedImageById(@Path("id") int id);
//
//    @GET("/wp-json/wp/v2/posts")
//    Call<List<Post>> getAllPostByCategoryId(@Query("categories") int id);

    @GET("/wp-json/wp/v2/posts")
    Call<List<PoseSearch>> getPostsByQuerySearch(@Query("search") String searchterm);

    @GET("/wp-json/wp/v2/users/{id}")
    Author getPostAuthorById(@Path("id") int id);

    @GET("/wp-json/wp/v2/categories/{id}")
    Category getPostCategoryById(@Path("id") int id);

    @POST("saveSubscriber")
    @FormUrlEncoded
    Call<Subscribe> saveSubscriberEmail(@Field("email") String email);

//    @GET("/wp-json/wp/v2/posts/{id}")
//    Call<Post.PostDetail> getPostDetailById(@Path("id") int id);
}
