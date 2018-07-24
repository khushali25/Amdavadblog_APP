package Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Media {

    public int id;

    public MediaDetail media_details;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public class MediaDetail
    {
        public Sizes getSizes() {
            return sizes;
        }

        public void setSizes(Sizes sizes) {
            this.sizes = sizes;
        }

        public Sizes sizes;

    }

    public class Sizes
    {
        public Medium getMedium_large() {
            return medium_large;
        }

        public void setMedium_large(Medium medium_large) {
            this.medium_large = medium_large;
        }

        public Medium medium_large;

    }

    public class Medium
    {
        public String getSource_url() {
            return source_url;
        }

        public void setSource_url(String source_url) {
            this.source_url = source_url;
        }

        public String source_url;

    }
}
