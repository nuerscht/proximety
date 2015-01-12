package ch.ffhs.esa.proximety.domain;

import com.google.gson.annotations.SerializedName;

/**
 * @author Patrick Bösch.
 */
public class FriendRequest {
    @SerializedName("_id")
    public String id;
    @SerializedName("requester")
    public String requester;
    @SerializedName("requestee")
    public String requestee;
}
