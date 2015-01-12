package ch.ffhs.esa.proximety.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

import ch.ffhs.esa.proximety.R;
import ch.ffhs.esa.proximety.consts.ProximetyConsts;

/**
 * @author Andy Villiger
 */
public class InitialScreenActivity extends ActionBarActivity {

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
	
	public void onRegisterButtonClick(View button){
		Intent intent = new Intent(this, RegisterActivity.class);
		
		startActivity(intent);
	}
	
	public void onLoginButtonClick(View button){
		Intent intent = new Intent(this, LoginActivity.class);
		
		startActivity(intent);
	}
}
