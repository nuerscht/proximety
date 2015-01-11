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
import ch.ffhs.esa.proximety.helper.LoadingDialogHelper;
import ch.ffhs.esa.proximety.service.binder.user.UserServiceBinder;
import ch.ffhs.esa.proximety.service.handler.ResponseHandler;

public class RegisterActivity extends ActionBarActivity {
    private static String SESSION_INPUT_NAME  = "input_name";
    private static String SESSION_INPUT_EMAIL = "input_email";


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
	}

    @Override
    protected void onPause() {
        //store form values
        SharedPreferences sharedPrefsRegister = getSharedPreferences(this.getClass().getName(), MODE_PRIVATE);
        SharedPreferences.Editor editorRegister = sharedPrefsRegister.edit();

        EditText user = (EditText)findViewById(R.id.inputName);
        EditText email = (EditText)findViewById(R.id.inputEmail);
        editorRegister.putString(SESSION_INPUT_NAME, user.getText().toString());
        editorRegister.putString(SESSION_INPUT_EMAIL, email.getText().toString());
        editorRegister.apply();

        //save value for login
        SharedPreferences sharedPrefsLogin = getSharedPreferences(LoginActivity.class.getName(), MODE_PRIVATE);
        SharedPreferences.Editor editorLogin = sharedPrefsLogin.edit();
        editorLogin.putString(SESSION_INPUT_EMAIL, email.getText().toString());
        editorLogin.apply();

        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

        //restore form values
        SharedPreferences sharedPreferences = getSharedPreferences(this.getClass().getName(), MODE_PRIVATE);
        EditText user = (EditText)findViewById(R.id.inputName);
        EditText email = (EditText) findViewById(R.id.inputEmail);
        user.setText(sharedPreferences.getString(SESSION_INPUT_NAME, ""));
        email.setText(sharedPreferences.getString(SESSION_INPUT_EMAIL, ""));
    }

    private void onLoginSuccess() {
        Intent intent = new Intent(this, LoginActivity.class);

        startActivity(intent);

        finish();
    }



	public void onRegisterButtonClick(View button){
        final Dialog loadingDialog = LoadingDialogHelper.createDialog(this);
        loadingDialog.show();
        UserServiceBinder usb = new UserServiceBinder(getApplicationContext(), loadingDialog);

        EditText name = (EditText)findViewById(R.id.inputName);
        EditText user = (EditText)findViewById(R.id.inputEmail);
        EditText password = (EditText)findViewById(R.id.inputPassword);
        EditText passwordConfirm = (EditText)findViewById(R.id.inputPasswordConfirm);

        usb.signup(name.getText().toString(),
                user.getText().toString(),
                password.getText().toString(),
                passwordConfirm.getText().toString(), new ResponseHandler(getApplicationContext()) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, Object response) {
                loadingDialog.cancel();
                if (statusCode == 200) {
                    onLoginSuccess();
                } else {
                    Toast.makeText(getApplicationContext(), getErrorMessage(response), Toast.LENGTH_SHORT).show();
                }
            }
        });
	}
}
