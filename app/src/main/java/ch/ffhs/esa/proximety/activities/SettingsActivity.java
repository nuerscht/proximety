package ch.ffhs.esa.proximety.activities;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import ch.ffhs.esa.proximety.R;

/**
 * @author alo
 *
 */
public class SettingsActivity extends PreferenceActivity {

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle bundle){
		super.onCreate(bundle);
		
		this.addPreferencesFromResource(R.xml.proximety_settings);
	}
}
