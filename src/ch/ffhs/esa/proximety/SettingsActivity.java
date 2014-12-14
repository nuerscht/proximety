package ch.ffhs.esa.proximety;

import android.os.Bundle;
import android.preference.PreferenceActivity;

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
