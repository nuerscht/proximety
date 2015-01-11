package ch.ffhs.esa.proximety.service.client;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Looper;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import ch.ffhs.esa.proximety.R;

/**
 * Created by Patrick Bösch.
 */
public class RestClient {
    private static final String BASE_URL = "http://api.proximety.ch/";

    private static AsyncHttpClient syncClient = new SyncHttpClient();
    private static AsyncHttpClient asyncClient = new AsyncHttpClient();

    public static void get(Context context, String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        if (isOnline(context)) {
            getClient().get(context, getAbsoluteUrl(url), params, responseHandler);
        } else {
            Toast.makeText(context, context.getText(R.string.network_not_available), Toast.LENGTH_SHORT).show();
        }
    }

    public static void put(Context context, String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        if (isOnline(context)) {
            getClient().put(context, getAbsoluteUrl(url), params, responseHandler);
        } else {
            Toast.makeText(context, context.getText(R.string.network_not_available), Toast.LENGTH_SHORT).show();
        }
    }

    public static void put(Context context, String url, JSONObject jsonObject, AsyncHttpResponseHandler responseHandler) {
        if (isOnline(context)) {
            StringEntity entity = null;
            try {
                entity = new StringEntity(jsonObject.toString());
                entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            getClient().put(context, getAbsoluteUrl(url), entity, "application/json", responseHandler);
        } else {
            Toast.makeText(context, context.getText(R.string.network_not_available), Toast.LENGTH_SHORT).show();
        }
    }

    public static void delete(Context context, String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        if (isOnline(context)) {
            getClient().delete(context, getAbsoluteUrl(url), null, params, responseHandler);
        } else {
            Toast.makeText(context, context.getText(R.string.network_not_available), Toast.LENGTH_SHORT).show();
        }
    }

    public static void post(Context context, String url, JSONObject jsonObject, AsyncHttpResponseHandler responseHandler) {
        if (isOnline(context)) {
            StringEntity entity = null;
            try {
                entity = new StringEntity(jsonObject.toString());
                entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            getClient().post(context, getAbsoluteUrl(url), entity, "application/json", responseHandler);
        } else {
            Toast.makeText(context, context.getText(R.string.network_not_available), Toast.LENGTH_SHORT).show();
        }
    }

    private static AsyncHttpClient getClient()
    {
        // Return the synchronous HTTP client when the thread is not prepared
        if (Looper.myLooper() == null)
            return syncClient;
        return asyncClient;
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        String url = BASE_URL + relativeUrl;
        return url;
    }

    private static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }
}