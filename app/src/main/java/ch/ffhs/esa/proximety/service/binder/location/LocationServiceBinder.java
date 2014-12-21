package ch.ffhs.esa.proximety.service.binder.location;

import android.content.Context;
import android.location.Location;

import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import ch.ffhs.esa.proximety.consts.ProximetyConsts;
import ch.ffhs.esa.proximety.domain.Message;
import ch.ffhs.esa.proximety.domain.Token;
import ch.ffhs.esa.proximety.service.binder.ServiceBinder;
import ch.ffhs.esa.proximety.service.client.RestClient;
import ch.ffhs.esa.proximety.service.handler.ResponseHandler;

/**
 * Created by boe on 15.12.2014.
 */
public class LocationServiceBinder extends ServiceBinder {

    public LocationServiceBinder(Context context) {
        super(context);
    }

    public List<Location> queryFriendLocations(String token) {
        return null;
    }

    public Location queryFriend(int friendId, String token) {
        return null;
    }

    public void updateLocation(double latitude, double longitude, final ResponseHandler responseHandler) {
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put(ProximetyConsts.SERVICE_PARAM_TOKEN, getToken());
            jsonObj.put(ProximetyConsts.SERVICE_PARAM_LATITUDE, Double.toString(latitude));
            jsonObj.put(ProximetyConsts.SERVICE_PARAM_LONGITUDE, Double.toString(longitude));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RestClient.post(getApplicationContext(), "api/location", jsonObj, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Gson gson = new Gson();
                responseHandler.onSuccess(statusCode, headers, gson.fromJson(response.toString(), Message.class));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                responseHandler.onError(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                responseHandler.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                responseHandler.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }
}
