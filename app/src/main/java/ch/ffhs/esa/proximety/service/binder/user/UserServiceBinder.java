package ch.ffhs.esa.proximety.service.binder.user;

import android.content.Context;

import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ch.ffhs.esa.proximety.domain.Friend;
import ch.ffhs.esa.proximety.domain.Token;
import ch.ffhs.esa.proximety.service.binder.ServiceBinder;
import ch.ffhs.esa.proximety.service.client.RestClient;
import ch.ffhs.esa.proximety.service.handler.ResponseHandler;

/**
 * Created by boe on 19.12.2014.
 */
public class UserServiceBinder extends ServiceBinder {
    public UserServiceBinder(Context context) {
        super(context);
    }

    public void login(String email, String password, final ResponseHandler responseHandler) {
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("email", email);
            jsonObj.put("password", password);
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

    public void signup(String name, String email, String password, String passwordConfirm, final ResponseHandler responseHandler) {
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("name", name);
            jsonObj.put("email", email);
            jsonObj.put("password", password);
            jsonObj.put("password_confirm", passwordConfirm);
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