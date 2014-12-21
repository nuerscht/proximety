package ch.ffhs.esa.proximety.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.Header;

import ch.ffhs.esa.proximety.R;
import ch.ffhs.esa.proximety.service.binder.user.UserServiceBinder;
import ch.ffhs.esa.proximety.service.handler.ResponseHandler;

public class RegisterActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.proximety_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

    private void onLoginSuccess() {
        Intent intent = new Intent(this, MainActivity.class);

        startActivity(intent);

        finish();
    }
	
	public void onRegisterButtonClick(View button){
        UserServiceBinder usb = new UserServiceBinder(getApplicationContext());

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
                if (statusCode == 200) {
                    onLoginSuccess();
                } else {
                    Toast.makeText(getApplicationContext(), getErrorMessage(response), Toast.LENGTH_SHORT).show();
                }
            }
        });
	}
}
