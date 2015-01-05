package ch.ffhs.esa.proximety.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.Header;

import ch.ffhs.esa.proximety.R;
import ch.ffhs.esa.proximety.domain.FriendRequest;
import ch.ffhs.esa.proximety.service.binder.friend.FriendServiceBinder;
import ch.ffhs.esa.proximety.service.handler.ResponseHandler;

/**
 * Created by boe on 21.12.2014.
 */
public class FriendAddActivity extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_add);
    }

    private void onFriendRequest(FriendRequest friendRequest) {
        Toast.makeText(getApplicationContext(), getText(R.string.friend_add_success), Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, MainActivity.class);

        startActivity(intent);

        finish();
    }

    public void onButtonInviteClick(View button){
        FriendServiceBinder fsb = new FriendServiceBinder(getApplicationContext());


        EditText email = (EditText)findViewById(R.id.inputEmail);
        fsb.sendRequest(email.getText().toString(), new ResponseHandler(getApplicationContext()) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, Object response) {
                if (statusCode == 200) {
                    onFriendRequest((FriendRequest) response);
                } else {
                    Toast.makeText(getApplicationContext(), getErrorMessage(response), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
