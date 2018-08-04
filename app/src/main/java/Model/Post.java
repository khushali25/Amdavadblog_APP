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

//    @SerializedName("categories")
//    @Expose
//    public int categories;

    @SerializedName("author")
    @Expose
    public int author;

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

    @SerializedName("categoryname")
    @Expose
    public String categoryname;

    @SerializedName("authorname")
    @Expose
    public String authorname;

    public String getAuthorname() {
        return authorname;
    }

    public void setAuthorname(String authorname) {
        this.authorname = authorname;
    }
//
//    public int getCategories() {
//        return categories;
//    }
//
//    public void setCategories(int categories) {
//        this.categories = categories;
//    }

    public String getCategoryname() {
        return categoryname;
    }

    public void setCategoryname(String categoryname) {
        this.categoryname = categoryname;
    }

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

    public int getAuthor() {
        return author;
    }

    public void setAuthor(int author) {
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

    public static class PostDetail
    {
        public int id;
        public Content content;
        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public Content getContent() {
            return content;
        }

        public void setContent(Content content) {
            this.content = content;
        }

    }
    public class Content
    {
        public String rendered;
        public String getRendered() {
            return rendered;
        }

        public void setRendered(String rendered) {
            this.rendered = rendered;
        }


    }
}
