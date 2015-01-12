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
import ch.ffhs.esa.proximety.domain.Token;
import ch.ffhs.esa.proximety.helper.LoadingDialogHelper;
import ch.ffhs.esa.proximety.service.binder.user.UserServiceBinder;
import ch.ffhs.esa.proximety.service.handler.ResponseHandler;

public class LoginActivity extends ActionBarActivity {
    private static final String SESSION_INPUT_EMAIL = "input_email";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
	}

    @Override
    protected void onPause() {
        //store form values
        SharedPreferences sharedPreferences = getSharedPreferences(this.getClass().getName(), MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        EditText email = (EditText)findViewById(R.id.inputEmail);
        editor.putString(SESSION_INPUT_EMAIL, email.getText().toString());
        editor.apply();

        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

        //restore form values
        SharedPreferences sharedPreferences = getSharedPreferences(this.getClass().getName(), MODE_PRIVATE);
        EditText email = (EditText) findViewById(R.id.inputEmail);
        email.setText(sharedPreferences.getString(SESSION_INPUT_EMAIL, ""));
    }

    private void onLoginSuccess(Token token) {
        //save token
        SharedPreferences sharedPreferences = getSharedPreferences(ProximetyConsts.PROXIMETY_SHARED_PREF, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(ProximetyConsts.PROXIMETY_SHARED_PREF_TOKEN, token.token);
        editor.apply();

        Intent intent = new Intent(this, MainActivity.class);

        startActivity(intent);

        finish();
    }
	
	public void onLoginButtonClick(View button){
        final Dialog loadingDialog = LoadingDialogHelper.createDialog(this);
        loadingDialog.show();
        UserServiceBinder usb = new UserServiceBinder(getApplicationContext(), loadingDialog);

        EditText user = (EditText)findViewById(R.id.inputEmail);
        EditText password = (EditText)findViewById(R.id.inputPassword);
        usb.login(user.getText().toString(), password.getText().toString(), new ResponseHandler(getApplicationContext()) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, Object response) {
                loadingDialog.cancel();
                if (statusCode == 200) {
                    onLoginSuccess((Token)response);
                } else {
                    Toast.makeText(getApplicationContext(), getErrorMessage(response), Toast.LENGTH_SHORT).show();
                }
            }
        });
	}
}
