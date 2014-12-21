package ch.ffhs.esa.proximety.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import ch.ffhs.esa.proximety.R;
import ch.ffhs.esa.proximety.consts.ProximetyConsts;

public class InitialScreenActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = getSharedPreferences(ProximetyConsts.PROXIMETY_SHARED_PREF, MODE_PRIVATE);
        String token = sharedPreferences.getString(ProximetyConsts.PROXIMETY_SHARED_PREF_TOKEN, "");

        if (token.trim().isEmpty())
            setContentView(R.layout.activity_initial_screen);
        else {
            //if already logged in load main activity
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
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
	
	public void onRegisterButtonClick(View button){
		Intent intent = new Intent(this, RegisterActivity.class);
		
		startActivity(intent);
	}
	
	public void onLoginButtonClick(View button){
		Intent intent = new Intent(this, LoginActivity.class);
		
		startActivity(intent);
	}
}
