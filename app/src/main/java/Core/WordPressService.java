package Core;

import java.util.List;

import Model.Author;
import Model.Category;
import Model.Media;
import Model.Post;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface WordPressService {
    @GET("/wp-json/wp/v2/posts")
    Call<List<Post>> getAllPost();

    @GET("/wp-json/wp/v2/posts")
    Call<List<Post>> getAllPostPerPage(@Query("page") int id);

    @GET("/wp-json/wp/v2/media/{id}")
    Media getFeaturedImageById(@Path("id") int id);

    @GET("/wp-json/wp/v2/posts")
    Call<List<Post>> getAllPostByCategoryId(@Query("categories") int id);

    @GET("/wp-json/wp/v2/posts")
    Call<List<Post>> getPostsByQuerySearch(@Query("search") String searchterm);

    @GET("/wp-json/wp/v2/users/{id}")
    Author getPostAuthorById(@Path("id") int id);

    @GET("/wp-json/wp/v2/categories/{id}")
   Category getPostCategoryById(@Path("id") int id);

    @GET("/wp-json/wp/v2/posts/{id}")
    Call<Post.PostDetail> getPostDetailById(@Path("id") int id);
}
