package ch.ffhs.esa.proximety.service.binder.friend;

import android.app.Dialog;
import android.content.Context;

import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ch.ffhs.esa.proximety.consts.ProximetyConsts;
import ch.ffhs.esa.proximety.domain.FriendRequest;
import ch.ffhs.esa.proximety.domain.Message;
import ch.ffhs.esa.proximety.service.binder.ServiceBinder;
import ch.ffhs.esa.proximety.service.client.RestClient;
import ch.ffhs.esa.proximety.service.handler.ResponseHandler;

/**
 * Created by Patrick Bösch.
 */
public class FriendServiceBinder extends ServiceBinder {

    public FriendServiceBinder(Context context, Dialog loadingDialog) {
        super(context, loadingDialog);
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
                closeLoadingDialog();
                responseHandler.onError(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                closeLoadingDialog();
                responseHandler.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                closeLoadingDialog();
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
                responseHandler.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                closeLoadingDialog();
                responseHandler.onError(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                closeLoadingDialog();
                responseHandler.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                closeLoadingDialog();
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
                responseHandler.onSuccess(statusCode, headers, response.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                closeLoadingDialog();
                responseHandler.onError(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                closeLoadingDialog();
                responseHandler.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                closeLoadingDialog();
                responseHandler.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

    public void deleteFriend(String friendId, final ResponseHandler responseHandler) {

        RequestParams params = new RequestParams();
        params.put(ProximetyConsts.SERVICE_PARAM_TOKEN, getToken());
        params.put(ProximetyConsts.SERVICE_PARAM_FRIEND_ID, friendId);

        RestClient.delete(getApplicationContext(), "api/friend", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Gson gson = new Gson();
                responseHandler.onSuccess(statusCode, headers, gson.fromJson(response.toString(), Message.class));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                closeLoadingDialog();
                responseHandler.onError(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                closeLoadingDialog();
                responseHandler.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                closeLoadingDialog();
                responseHandler.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

    public void getListOfFriends(final ResponseHandler responseHandler) {

        RequestParams params = new RequestParams();
        params.put(ProximetyConsts.SERVICE_PARAM_TOKEN, getToken());

        RestClient.get(getApplicationContext(), "api/friend", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                responseHandler.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                closeLoadingDialog();
                responseHandler.onError(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                closeLoadingDialog();
                responseHandler.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                closeLoadingDialog();
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
                closeLoadingDialog();
                responseHandler.onError(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                closeLoadingDialog();
                responseHandler.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                closeLoadingDialog();
                responseHandler.onFailure(statusCode, headers, responseString, throwable);
            }
        });
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
                closeLoadingDialog();
                responseHandler.onError(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                closeLoadingDialog();
                responseHandler.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                closeLoadingDialog();
                responseHandler.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

    public void updateSettings(String friendId, boolean active, final ResponseHandler responseHandler) {
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put(ProximetyConsts.SERVICE_PARAM_TOKEN, getToken());

            if (active)
                jsonObj.put(ProximetyConsts.SERVICE_PARAM_ACTIVE, 1);
            else
                jsonObj.put(ProximetyConsts.SERVICE_PARAM_ACTIVE, 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RestClient.put(getApplicationContext(), "api/friend/".concat(friendId).concat("/alarm"), jsonObj, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Gson gson = new Gson();
                responseHandler.onSuccess(statusCode, headers, gson.fromJson(response.toString(), Message.class));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                closeLoadingDialog();
                responseHandler.onError(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                closeLoadingDialog();
                responseHandler.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                closeLoadingDialog();
                responseHandler.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }
}
