package Core;

import android.provider.MediaStore;

import java.util.List;

import Model.Media;
import Model.Post;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface WordPressService {
    @GET("/wp-json/wp/v2/posts")
    Call<List<Post>> getAllPost();

    @GET("/wp-json/wp/v2/media/{id}")
    Media getFeaturedImageById(@Path("id") int id);

    @GET("/wp-json/wp/v2/posts")
    Call<List<Post>> getAllPostByCategoryId(@Query("categories") int id);
}
