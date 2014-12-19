package ch.ffhs.esa.proximety.service.binder.setting;

import android.content.Context;

import ch.ffhs.esa.proximety.service.binder.ServiceBinder;

/**
 * Created by boe on 15.12.2014.
 */
public class SettingServiceBinder extends ServiceBinder {
    public SettingServiceBinder(Context context) {
        super(context);
    }

    public void setFriendSetting(int friendId, int distance, boolean alarm, String token) {

    }

    public void setUserSetting(String token) {

    }
}
