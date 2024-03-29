package ch.ffhs.esa.proximety.activities;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.Header;

import ch.ffhs.esa.proximety.R;
import ch.ffhs.esa.proximety.consts.ProximetyConsts;
import ch.ffhs.esa.proximety.helper.LoadingDialogHelper;
import ch.ffhs.esa.proximety.service.binder.friend.FriendServiceBinder;
import ch.ffhs.esa.proximety.service.handler.ResponseHandler;

/**
 * @author Patrick Bösch, Andy Villiger
 */
public class FriendAddActivity extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_add);
    }

    private void onFriendRequest() {
        Toast.makeText(getApplicationContext(), getText(R.string.friend_add_success), Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, MainActivity.class);

        startActivity(intent);

        finish();
    }

    public void onButtonInviteClick(View button){
        EditText email = (EditText)findViewById(R.id.inputEmail);

        SharedPreferences sharedPreferences = getSharedPreferences(ProximetyConsts.PROXIMETY_SHARED_PREF, MODE_PRIVATE);
        String loginEMail = sharedPreferences.getString(ProximetyConsts.PROXIMETY_SHARED_PREF_EMAIL, "");

        if (!loginEMail.equals(email.getText().toString())) {
            final Dialog loadingDialog = LoadingDialogHelper.createDialog(this);
            loadingDialog.show();
            FriendServiceBinder fsb = new FriendServiceBinder(getApplicationContext(), loadingDialog);

            fsb.sendRequest(email.getText().toString(), new ResponseHandler(getApplicationContext()) {
                @Override
                public void onSuccess(int statusCode, Header[] headers, Object response) {
                    if (statusCode == 200) {
                        loadingDialog.cancel();
                        onFriendRequest();
                    } else {
                        Toast.makeText(getApplicationContext(), getErrorMessage(response), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), getText(R.string.same_email), Toast.LENGTH_SHORT).show();
        }
    }
}
