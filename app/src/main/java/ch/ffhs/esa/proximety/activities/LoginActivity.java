package ch.ffhs.esa.proximety.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.apache.http.Header;
import org.json.JSONObject;

import ch.ffhs.esa.proximety.R;
import ch.ffhs.esa.proximety.domain.test.JsonTestValidate;
import ch.ffhs.esa.proximety.service.binder.ServiceBinder;
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

        ServiceBinder sb = new ServiceBinder();
        JsonTestValidate jtv = sb.get(new ResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, Object response) {
                Toast.makeText(getApplicationContext(), "Yeah :)", Toast.LENGTH_SHORT).show();
                onLoginSuccess((JsonTestValidate) response);
            }

            @Override
            public void onError(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(getApplicationContext(), "Fehler :(", Toast.LENGTH_SHORT).show();
            }
        });
        Toast.makeText(getApplicationContext(), "sali :)", Toast.LENGTH_SHORT).show();
	}
}
