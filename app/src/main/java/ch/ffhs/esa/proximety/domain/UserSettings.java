package ch.ffhs.esa.proximety.domain;

import com.google.gson.annotations.SerializedName;

/**
 * @author Patrick Bösch.
 */
public class UserSettings {
    @SerializedName("active")
    public boolean active = true;

    @SerializedName("distance")
    public int distance = 5;
}
