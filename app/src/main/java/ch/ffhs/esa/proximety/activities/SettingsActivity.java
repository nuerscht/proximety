package ch.ffhs.esa.proximety.activities;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;

import ch.ffhs.esa.proximety.R;

/**
 * @author alo
 *
 * Little workaround for AppCompat has to be done, as the PreferenceActivity has no support for the
 * SupportActionBar, thus doing standard ActionBarActivity with a PrerefenceFragment instead.
 */
public class SettingsActivity extends ActionBarActivity {

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle bundle){
		super.onCreate(bundle);

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
	}

    public static class SettingsFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle paramBundle) {
            super.onCreate(paramBundle);
            addPreferencesFromResource(R.xml.proximety_settings);
        }
    }
}
