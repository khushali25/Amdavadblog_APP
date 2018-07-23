package Core;

import java.util.List;

import Model.Post;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface WordPressService {
    @GET("/wp-json/wp/v2/posts")
    Call<List<Post>> getAllPost();
}
