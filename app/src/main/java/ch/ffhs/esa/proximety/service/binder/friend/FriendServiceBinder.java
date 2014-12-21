package ch.ffhs.esa.proximety.service.binder.friend;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import ch.ffhs.esa.proximety.consts.ProximetyConsts;
import ch.ffhs.esa.proximety.domain.Friend;
import ch.ffhs.esa.proximety.domain.FriendRequest;
import ch.ffhs.esa.proximety.domain.Message;
import ch.ffhs.esa.proximety.service.binder.ServiceBinder;
import ch.ffhs.esa.proximety.service.client.RestClient;
import ch.ffhs.esa.proximety.service.handler.ResponseHandler;

/**
 * Created by boe on 15.12.2014.
 */
public class FriendServiceBinder extends ServiceBinder {

    public FriendServiceBinder(Context context) {
        super(context);
    }

    public void sendRequest(String email, final ResponseHandler responseHandler) {
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put(ProximetyConsts.SERVICE_PARAM_EMAIL, email);
            jsonObj.put(ProximetyConsts.SERVICE_PARAM_TOKEN, getToken());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RestClient.post(getApplicationContext(), "api/friend/request", jsonObj, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Gson gson = new Gson();
                responseHandler.onSuccess(statusCode, headers, gson.fromJson(response.toString(), FriendRequest.class));
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

    public void confirmRequest(String requestId, final ResponseHandler responseHandler) {
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put(ProximetyConsts.SERVICE_PARAM_REQUEST_ID, requestId);
            jsonObj.put(ProximetyConsts.SERVICE_PARAM_TOKEN, getToken());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RestClient.put(getApplicationContext(), "api/friend/request", jsonObj, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Gson gson = new Gson();
                responseHandler.onSuccess(statusCode, headers, gson.fromJson(response.toString(), Friend.class));
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

    public void declineRequest(String requestId, final ResponseHandler responseHandler) {
        RequestParams params = new RequestParams();
        params.put(ProximetyConsts.SERVICE_PARAM_TOKEN, getToken());
        params.put(ProximetyConsts.SERVICE_PARAM_REQUEST_ID, requestId);

        RestClient.delete(getApplicationContext(), "api/friend/request", params, new JsonHttpResponseHandler() {
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

    public void deleteFriend() {
    }

    public void getListOfFriends(final ResponseHandler responseHandler) {

        RequestParams params = new RequestParams();
        params.put(ProximetyConsts.SERVICE_PARAM_TOKEN, "7b2868f754193e2f8c1674cb83162a8445002756");
        // params.put(ProximetyConsts.SERVICE_PARAM_TOKEN, getToken());

        RestClient.get(getApplicationContext(), "api/friend", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                responseHandler.onSuccess(statusCode, headers, response);
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

    public void getFriendDetails(String friendId, final ResponseHandler responseHandler){
        RequestParams params = new RequestParams();
        params.put(ProximetyConsts.SERVICE_PARAM_TOKEN, getToken());

        RestClient.get(getApplicationContext(), "api/friend/" + friendId, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                responseHandler.onSuccess(statusCode, headers, response);
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

    public List<String> search(String searchText, String token) {
        return null;
    }

    public void queryOpenRequests(final ResponseHandler responseHandler) {

        RequestParams params = new RequestParams();
        params.put(ProximetyConsts.SERVICE_PARAM_TOKEN, getToken());

        RestClient.get(getApplicationContext(), "api/friend/request", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                responseHandler.onSuccess(statusCode, headers, response);
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

    private void invokeWebService() {

    }
}
