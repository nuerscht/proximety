package ch.ffhs.esa.proximety.service.handler;

import android.content.Context;
import android.widget.Toast;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ch.ffhs.esa.proximety.R;
import ch.ffhs.esa.proximety.consts.ProximetyConsts;

/**
 * Created by Patrick Bösch.
 */
public abstract class ResponseHandler {
    public abstract void onSuccess(int statusCode, Header[] headers, Object response);

    public void onError(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
        Toast.makeText(getApplicationContext(), getErrorMessage(errorResponse), Toast.LENGTH_SHORT).show();
    }

    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
        Toast.makeText(getApplicationContext(), getErrorMessage(errorResponse), Toast.LENGTH_SHORT).show();
    }

    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
    }


    private final Context context;

    public ResponseHandler(Context context) {
        this.context = context;
    }

    protected Context getApplicationContext() {
        return this.context;
    }

    String getUnknownErrorMessage() {
        return context.getString(R.string.unknown_error_msg);
    }

    String getNoDataErrorMessage() {
        return context.getString(R.string.no_data);
    }

    protected String getErrorMessage(Object object) {
        String errorMsg = getUnknownErrorMessage();
        String objMsg = "";

        if (object != null)
            objMsg = object.toString();
        else
            errorMsg = getNoDataErrorMessage();

        if (!objMsg.isEmpty()) {
            errorMsg = errorMsg.concat(" (").concat(objMsg).concat(")");
        }

        return errorMsg;
    }

    String getErrorMessage(JSONObject errorResponse) {
        String errorMsg = getUnknownErrorMessage();
        try {
            if (errorResponse != null)
                errorMsg = errorResponse.getString(ProximetyConsts.JSON_RETURN_MESSAGE);
            else
                errorMsg = getNoDataErrorMessage();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return errorMsg;
    }

    String getErrorMessage(JSONArray errorResponse) {
        StringBuilder sb = new StringBuilder();
        int count = 0;
        while (count < errorResponse.length()) {
            try {
                JSONObject errorObj = errorResponse.getJSONObject(count);
                sb.append(errorObj.getString(ProximetyConsts.JSON_RETURN_MESSAGE));
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
            errorMsg = getUnknownErrorMessage();
        }

        return errorMsg;
    }
}
