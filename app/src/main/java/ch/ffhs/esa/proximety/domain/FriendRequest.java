package ch.ffhs.esa.proximety.domain;

import com.google.gson.annotations.SerializedName;

/**
 * Created by boe on 21.12.2014.
 */
public class FriendRequest {
    @SerializedName("_id")
    public String id;
    @SerializedName("requester")
    public String requester;
    @SerializedName("requestee")
    public String requestee;
}
