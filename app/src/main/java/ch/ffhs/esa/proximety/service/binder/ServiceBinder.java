package ch.ffhs.esa.proximety.service.binder;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import ch.ffhs.esa.proximety.consts.ProximetyConsts;

/**
 * @author Patrick Bösch.
 */
public class ServiceBinder {
    private final Context context;
    private final Dialog loadingDialog;

    protected ServiceBinder(Context context, Dialog loadingDialog) {
        this.context = context;
        this.loadingDialog = loadingDialog;
    }

    protected Context getApplicationContext() {
        return context;
    }

    protected void closeLoadingDialog() {
        if (this.loadingDialog != null) {
            loadingDialog.cancel();
        }
    }

    protected String getToken() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(ProximetyConsts.PROXIMETY_SHARED_PREF, Context.MODE_PRIVATE);
        String token = sharedPreferences.getString(ProximetyConsts.PROXIMETY_SHARED_PREF_TOKEN, "");
        Log.i("ServiceBinder", "token: " + token);
        return token;
    }
}