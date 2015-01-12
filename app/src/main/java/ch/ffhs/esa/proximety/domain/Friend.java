package ch.ffhs.esa.proximety.domain;

import com.google.gson.annotations.SerializedName;

/**
 * @author Patrick Bösch.
 */
public class Friend {
    @SerializedName("_id")
    public String id;
    @SerializedName("name")
    public String name;
    @SerializedName("email")
    public String email;
    @SerializedName("friends")
    public String[] friends;
    @SerializedName("latitude")
    public Double latitude;
    @SerializedName("longitude")
    public Double longitude;

    public Boolean isLocationSet(){
        return latitude != null && longitude != null;
    }
}
