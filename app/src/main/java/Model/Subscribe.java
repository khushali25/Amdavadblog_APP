package Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Subscribe {

    @SerializedName("email")
    @Expose
    private String email;
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    @Override
    public String toString() {
        return "Subscribe{" +
                "email='" + email + '\'' +
                '}';
    }
}
