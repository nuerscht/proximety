package ch.ffhs.esa.proximety.service.binder;

import android.content.Context;
import android.content.SharedPreferences;

import ch.ffhs.esa.proximety.consts.ProximetyConsts;

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

    protected String getToken() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(ProximetyConsts.PROXIMETY_SHARED_PREF, Context.MODE_PRIVATE);
        return sharedPreferences.getString(ProximetyConsts.PROXIMETY_SHARED_PREF_TOKEN, "");
    }
}