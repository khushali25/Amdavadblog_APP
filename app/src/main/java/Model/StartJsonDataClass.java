package Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StartJsonDataClass {

        @SerializedName("success")
        @Expose
        private boolean success;
        @SerializedName("count")
        @Expose
        private int count;
        @SerializedName("data")
        @Expose
        private List<Post> data = null;

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public List<Post> getData() {
            return data;
        }

        public void setData(List<Post> data) {
            this.data = data;

    }
}
