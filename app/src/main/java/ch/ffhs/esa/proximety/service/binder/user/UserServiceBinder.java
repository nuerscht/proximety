package ch.ffhs.esa.proximety.service.binder.user;

import android.app.Dialog;
import android.content.Context;

import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ch.ffhs.esa.proximety.consts.ProximetyConsts;
import ch.ffhs.esa.proximety.domain.Friend;
import ch.ffhs.esa.proximety.domain.Message;
import ch.ffhs.esa.proximety.domain.Token;
import ch.ffhs.esa.proximety.service.binder.ServiceBinder;
import ch.ffhs.esa.proximety.service.client.RestClient;
import ch.ffhs.esa.proximety.service.handler.ResponseHandler;

/**
 * @author Patrick Bösch.
 */
public class UserServiceBinder extends ServiceBinder {
    public UserServiceBinder(Context context, Dialog loadingDialog) {
        super(context, loadingDialog);
    }

    public void login(String email, String password, final ResponseHandler responseHandler) {
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put(ProximetyConsts.SERVICE_PARAM_EMAIL, email);
            jsonObj.put(ProximetyConsts.SERVICE_PARAM_PASSWORD, password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RestClient.post(getApplicationContext(), "api/token", jsonObj, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Gson gson = new Gson();
                responseHandler.onSuccess(statusCode, headers, gson.fromJson(response.toString(), Token.class));
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

    public void signup(String name, String email, String password, String passwordConfirm, final ResponseHandler responseHandler) {
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put(ProximetyConsts.SERVICE_PARAM_NAME, name);
            jsonObj.put(ProximetyConsts.SERVICE_PARAM_EMAIL, email);
            jsonObj.put(ProximetyConsts.SERVICE_PARAM_PASSWORD, password);
            jsonObj.put(ProximetyConsts.SERVICE_PARAM_PASSWORD_CONFIRM, passwordConfirm);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RestClient.post(getApplicationContext(), "api/signup", jsonObj, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Gson gson = new Gson();
                responseHandler.onSuccess(statusCode, headers, gson.fromJson(response.toString(), Friend.class));
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

    public void setClientId(String regid, final ResponseHandler responseHandler) {
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put(ProximetyConsts.SERVICE_PARAM_ID, regid);
            jsonObj.put(ProximetyConsts.SERVICE_PARAM_TOKEN, getToken());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RestClient.post(getApplicationContext(), "api/user/client-id", jsonObj, new JsonHttpResponseHandler() {
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