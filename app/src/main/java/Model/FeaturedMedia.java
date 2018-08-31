package Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FeaturedMedia {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("URL")
    @Expose
    private String uRL;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getURL() {
        return uRL;
    }

    public void setURL(String uRL) {
        this.uRL = uRL;
    }

}
