package ch.ffhs.esa.proximety.domain.test;

import com.google.gson.annotations.SerializedName;

/**
 * Created by boe on 18.12.2014.
 */
public class JsonTestValidate {
    @SerializedName("object_or_array")
    public String objectOrArray;
    @SerializedName("empty")
    public boolean empty;
    @SerializedName("parse_time_nanoseconds")
    public int parseTimeNanoseconds;
    @SerializedName("validate")
    public boolean validate;
    @SerializedName("size")
    public int size;
}
