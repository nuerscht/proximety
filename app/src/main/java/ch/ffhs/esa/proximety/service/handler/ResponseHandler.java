package ch.ffhs.esa.proximety.service.handler;

import android.widget.Toast;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by boe on 18.12.2014.
 */
public abstract class ResponseHandler {
    public abstract void onSuccess(int statusCode, Header[] headers, Object response);
    public abstract void onError(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse);

    public abstract void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse);

    protected  String getErrorMessage(JSONObject errorResponse) {
        String errorMsg = "Ein Fehler ist aufgetreten";
        try {
            errorMsg = errorResponse.getString("msg");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return errorMsg;
    }

    protected String getErrorMessage(JSONArray errorResponse) {
        StringBuilder sb = new StringBuilder();
        int count = 0;
        while (count < errorResponse.length()) {
            try {
                JSONObject errorObj = errorResponse.getJSONObject(count);
                sb.append(errorObj.getString("msg"));
                //newline if not last line
                if (count+1 != errorResponse.length())
                    sb.append("\n");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            count++;
        }

        String errorMsg = sb.toString();

        if (errorMsg.isEmpty()) {
            errorMsg = "Ein Fehler ist aufgetreten";
        }

        return errorMsg;
    }

    public abstract void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable);
}
