package Model;

import com.example.xps.amdavadblog_app.R;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Adapter.PostContentAdapter;

public class Post {
    @SerializedName("id")
    @Expose
    public int id;
    @SerializedName("title")
    @Expose
    public title title;
    @SerializedName("category")
    @Expose
    public String category;
    @SerializedName("author")
    @Expose
    public String author;
    @SerializedName("excerpt")
    @Expose
    public Excerpt excerpt;
    @SerializedName("date")
    @Expose
    public String date;
    @SerializedName("link")
    @Expose
    public String link;
    @SerializedName("imagePath")
    @Expose
    public String imagePath;
    @SerializedName("featured_media")
    @Expose
    public int featured_media;

    public int getFeatured_media() {
        return featured_media;
    }

    public void setFeatured_media(int featured_media) {
        this.featured_media = featured_media;
    }

    public class title
    {
        public String rendered;
        public String getRendered() {
            return rendered;
        }

        public void setRendered(String rendered) {
            this.rendered = rendered;
        }


    }
    public class Excerpt
    {
        public String getRendered() {
            return rendered;
        }

        public void setRendered(String rendered) {
            this.rendered = rendered;
        }

        public String rendered;

    }
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

//    public String getExcerpt() {
//        return excerpt;
//    }
//
//    public void setExcerpt(String excerpt) {
//        this.excerpt = excerpt;
//    }
//

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

//    public String getTitle() {
//        return title;
//    }
//    public void setTitle(String title) {
//        this.title = title;
//    }

    public String getLink() {
        return link;
    }
    public void setLink(String link) {
        this.link = link;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

//    public static List<Post> getItems()
//    {
//        List<Post> postlist = new ArrayList<>();
////        for (int j = 1; j <= 5; j++)
////        {
//                Post post = new Post();
//                Post post1 = new Post();
//                Post post2 = new Post();
//                Post post3 = new Post();
//
//                post.setTitle("Enjoy this tasty food treat in rainy season");
//                post.setAuthor("By Palak Madlani");
//                post.setCategory("#Flavors of Amdavad");
//                post.setExcerpt("Enjoy this tasty food treat in rainy season Treats for the season of joy! Finally! Yes, the time is here...");
//                post.setImagePath("drawable://" + R.drawable.rainy);
//                post.setDate("2018-06-26T16:17:55");
//
//        post1.setTitle("Hair care tips for the rainy season");
//        post1.setAuthor("By Sirisha Bhavaraju");
//        post1.setCategory("#Things to do");
//        post1.setExcerpt("Hair care tips for the rainy season Dancing in the rain is so romantic, and that wet hair look is...");
//        post1.setImagePath("drawable://" + R.drawable.rainhair);
//
//        post2.setTitle("The Best Ways to Overcome Laziness");
//        post2.setAuthor("By Palak Madlani");
//        post2.setCategory("#Things to do");
//        post2.setExcerpt("The Best Ways to Overcome Laziness Mind matters- let go the laziness There are days in one’s life when laziness...");
//        post2.setImagePath("drawable://" + R.drawable.lazy);
//
//        post3.setTitle("Get max discount on food by using these apps in ahmedabad");
//        post3.setAuthor("By Palak Madlani");
//        post3.setCategory("#News");
//        post3.setExcerpt("Get max discount on food by using these apps in ahmedabad Take the name of Gujarat &amp; the flavour of...");
//        post3.setImagePath("drawable://" + R.drawable.coupn);
//
//                postlist.add(post);
//                postlist.add(post1);
//                postlist.add(post2);
//                postlist.add(post3);
//       // }
//        return postlist;
//    }
}
