package ch.ffhs.esa.proximety.delegate;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;

import ch.ffhs.esa.proximety.R;

/**
 * Created by Patrick Bösch.
 */
@SuppressWarnings("deprecation")
public class DrawerNavActivityDelegate {
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;

    private final ActionBarActivity activity;

    public DrawerNavActivityDelegate(ActionBarActivity activity) {
        this.activity = activity;
    }

    @SuppressWarnings("UnusedParameters")
    public void onCreate(Bundle savedInstanceState) {
        mTitle = mDrawerTitle = activity.getTitle();
        DrawerLayout mDrawerLayout = (DrawerLayout) activity.findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(activity, mDrawerLayout,
                R.drawable.ic_launcher, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                activity.getSupportActionBar().setTitle(mTitle);
                activity.invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                activity.getSupportActionBar().setTitle(mDrawerTitle);
                activity.invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setHomeButtonEnabled(true);
    }

    @SuppressWarnings("UnusedParameters")
    public void onPostCreate(Bundle savedInstanceState) {
        mDrawerToggle.syncState();
    }

    public void onConfigurationChanged(Configuration newConfig) {
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return mDrawerToggle.onOptionsItemSelected(item);
    }
}
