package ch.ffhs.esa.proximety.domain;

import com.google.gson.annotations.SerializedName;

/**
 * Created by boe on 21.12.2014.
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
    public String latitude;
    @SerializedName("longitude")
    public String longitude;
}
