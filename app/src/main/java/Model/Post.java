package Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Post {
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("PostId")
    @Expose
    private String postId;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("link")
    @Expose
    private String link;
    @SerializedName("excerpt")
    @Expose
    private String excerpt;
    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("featured_media")
    @Expose
    private FeaturedMedia featuredMedia;
    @SerializedName("category")
    @Expose
    private Category category;
    @SerializedName("author")
    @Expose
    private Author author;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    public String getExcerpt() {
        return excerpt;
    }

    public void setExcerpt(String excerpt) {
        this.excerpt = excerpt;
    }

    public FeaturedMedia getFeaturedMedia() {
        return featuredMedia;
    }

    public void setFeaturedMedia(FeaturedMedia featuredMedia) {
        this.featuredMedia = featuredMedia;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }
}