package ch.ffhs.esa.proximety.service.handler;

import org.apache.http.Header;
import org.json.JSONObject;

/**
 * Created by boe on 18.12.2014.
 */
public abstract class ResponseHandler {
    public abstract void onSuccess(int statusCode, Header[] headers, Object response);
    public abstract void onError(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse);
}
