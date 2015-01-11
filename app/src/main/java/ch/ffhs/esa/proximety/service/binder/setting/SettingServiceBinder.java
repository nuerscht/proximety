package ch.ffhs.esa.proximety.service.binder.setting;

import android.app.Dialog;
import android.content.Context;

import ch.ffhs.esa.proximety.service.binder.ServiceBinder;

/**
 * Created Patrick Bösch.
 */
public class SettingServiceBinder extends ServiceBinder {
    public SettingServiceBinder(Context context, Dialog loadingDialog) {
        super(context, loadingDialog);
    }

    public void setFriendSetting(int friendId, int distance, boolean alarm, String token) {

    }

    public void setUserSetting(String token) {

    }
}
