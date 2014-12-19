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
import org.json.JSONArray;
import org.json.JSONObject;

import ch.ffhs.esa.proximety.R;
import ch.ffhs.esa.proximety.domain.test.JsonTestValidate;
import ch.ffhs.esa.proximety.service.binder.ServiceBinder;
import ch.ffhs.esa.proximety.service.binder.user.UserServiceBinder;
import ch.ffhs.esa.proximety.service.handler.ResponseHandler;

public class LoginActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
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

    private void onLoginSuccess(JsonTestValidate jsonTestValidate) {
        Intent intent = new Intent(this, MainActivity.class);

        startActivity(intent);

        finish();
    }
	
	public void onLoginButtonClick(View button){

        UserServiceBinder usb = new UserServiceBinder(getApplicationContext());

        EditText user = (EditText)findViewById(R.id.inputEmail);
        EditText password = (EditText)findViewById(R.id.inputPassword);
        usb.login(user.getText().toString(), password.getText().toString(), new ResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, Object response) {
                if (statusCode == 200) {
                    onLoginSuccess((JsonTestValidate) response);
                } else {
                    Toast.makeText(getApplicationContext(), "Ein Fehler ist aufgetreten", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(getApplicationContext(), "Ein Fehler ist aufgetreten", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Toast.makeText(getApplicationContext(), "Ein Fehler ist aufgetreten", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(getApplicationContext(), "Ein Fehler ist aufgetreten", Toast.LENGTH_SHORT).show();
            }
        });
	}
}
