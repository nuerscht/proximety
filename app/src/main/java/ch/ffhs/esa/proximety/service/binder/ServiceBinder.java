package ch.ffhs.esa.proximety.service.binder;

import android.content.Context;

/**
 * Created by boe on 18.12.2014.
 */
public class ServiceBinder {
    private Context context;

    public ServiceBinder(Context context) {
        this.context = context;
    }

    protected Context getApplicationContext() {
        return context;
    }

    /*public void get(final ResponseHandler responseHandler) {

        RestClient.get("", null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Gson gson = new Gson();
                responseHandler.onSuccess(statusCode, headers, gson.fromJson(response.toString(), JsonTestValidate.class));
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Gson gson = new Gson();
                responseHandler.onSuccess(statusCode, headers, gson.fromJson(response.toString(), JsonTestValidate.class));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                responseHandler.onError(statusCode, headers, throwable, errorResponse);
            }
        });
    }*/
}