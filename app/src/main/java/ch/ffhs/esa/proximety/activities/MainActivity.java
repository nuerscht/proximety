package ch.ffhs.esa.proximety.activities;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import ch.ffhs.esa.proximety.R;
import ch.ffhs.esa.proximety.consts.ProximetyConsts;
import ch.ffhs.esa.proximety.delegate.DrawerNavActivityDelegate;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

/*
 * Main view of the app using tabs and fragments
 * 
 * Tabbing has been built according to the sample app at 
 * http://developer.android.com/training/implementing-navigation/lateral.html
 */
public class MainActivity extends FragmentActivity {

	AppSectionsPagerAdapter sectionsPagerAdapter;
	ViewPager viewPager;
    DrawerLayout drawerLayout;
    ListView drawerList;

    //Drawer Navigation
    DrawerNavActivityDelegate drawerDelegate;

    public MainActivity() {
        drawerDelegate = new DrawerNavActivityDelegate(this);
    }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

        drawerDelegate.onCreate(savedInstanceState);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerList = (ListView) findViewById(R.id.left_drawer);

        String[] menuList = getResources().getStringArray(R.array.main_drawer_list);

        // Set adapter for drawer
        drawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, menuList));

        drawerList.setOnItemClickListener(new DrawerItemClickListener());

		final ActionBar actionBar = getActionBar();

		// Create the adapter which returns the section pages fragments
		sectionsPagerAdapter = new AppSectionsPagerAdapter(
				getSupportFragmentManager());

		// Specify that tabs should be displayed in the action bar.
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        //Workaround for Icon in Android older 5.0
        actionBar.setDisplayShowHomeEnabled(false);

		ActionBar.TabListener tabListener = new ActionBar.TabListener() {
			@Override
			public void onTabSelected(Tab tab, FragmentTransaction ft) {
				viewPager.setCurrentItem(tab.getPosition());
			}

			@Override
			public void onTabUnselected(Tab tab, FragmentTransaction ft) {
				// empty for now
			}

			@Override
			public void onTabReselected(Tab tab, FragmentTransaction ft) {
				// probably ignore this event
			}

		};

		// Set up the ViewPager, attaching the adapter and setting up a listener
		// for when the
		// user swipes between sections.
		viewPager = (ViewPager) findViewById(R.id.pager);
		viewPager.setAdapter(sectionsPagerAdapter);
		viewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});

		actionBar.addTab(actionBar.newTab()
				.setText(getResources().getText(R.string.friends))
				.setTabListener(tabListener));

		actionBar.addTab(actionBar.newTab()
				.setText(getResources().getText(R.string.map))
				.setTabListener(tabListener));
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    /** Handle drawer menu actions */
    private void selectItem(int position) {
        switch (position) {
            case 0:
                Toast.makeText(getApplicationContext(), "Activity not here yet!", Toast.LENGTH_SHORT).show();
                break;
            case 1:
                startActivity(new Intent(this, OpenRequestsActivity.class));
                break;
            case 2:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
            case 3:
                Toast.makeText(getApplicationContext(), "Help is currently unavailable!", Toast.LENGTH_SHORT).show();
                break;
            case 4:
                Toast.makeText(getApplicationContext(), "About what?", Toast.LENGTH_SHORT).show();
                break;
            case 5:
                logout();
                break;
            default:
                Toast.makeText(getApplicationContext(), "Invalid menu option selected!", Toast.LENGTH_SHORT).show();
        }

        // Highlight the selected item, update the title, and close the drawer
        drawerList.setItemChecked(position, true);
        drawerLayout.closeDrawer(drawerList);
    }

    private void logout() {
        //clear application session data
        SharedPreferences sharedPreferences = getSharedPreferences(ProximetyConsts.PROXIMETY_SHARED_PREF, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();

        //go to initial screen
        Intent intent = new Intent(this, InitialScreenActivity.class);
        startActivity(intent);
        finish();
    }

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		drawerDelegate.onPostCreate(savedInstanceState);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		drawerDelegate.onConfigurationChanged(newConfig);
	}

	/* Called whenever we call invalidateOptionsMenu() */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// If the nav drawer is open, hide action items related to the content
		// view
		drawerDelegate.onPrepareOptionsMenu(menu);
		return super.onPrepareOptionsMenu(menu);
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
			startActivity(new Intent(this, SettingsActivity.class));
			return true;
		}
		if (drawerDelegate.onOptionsItemSelected(item)) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the primary sections of the app.
	 */
	public static class AppSectionsPagerAdapter extends FragmentPagerAdapter {

		public AppSectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int i) {
			switch (i) {
			case 1:
				// "second" the map view
				return new MapViewFragment();

			default:
				// The standard list view.
				return new ListViewFragment();

			}
		}

		@Override
		public int getCount() {
			return 2;
		}

	}

	public static class MapViewFragment extends Fragment implements
			OnMapReadyCallback {

		MapView mapView;
		GoogleMap map;

        @Override
        public void onResume() {
            if (mapView != null) {
                mapView.onResume();
            }

            super.onResume();
        }

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_section_map,
					container, false);

			// Gets the MapView from the XML layout and creates it
			mapView = (MapView) rootView.findViewById(R.id.main_map_view);
			mapView.onCreate(savedInstanceState);

			mapView.getMapAsync(this);

			return rootView;
		}

		@Override
		public void onMapReady(GoogleMap gMap) {
			map = gMap;

			// somehow needed but doc says no:
			// http://developer.android.com/reference/com/google/android/gms/maps/MapsInitializer.html
			MapsInitializer.initialize(getActivity());

			map.getUiSettings().setMyLocationButtonEnabled(true);
			map.getUiSettings().setZoomControlsEnabled(true);
			map.setMyLocationEnabled(true);

			LatLng sydney = new LatLng(-33.867, 151.206);

			map.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 13));

			map.addMarker(new MarkerOptions().title("Sydney")
					.snippet("The most populous city in Australia.")
					.position(sydney));
		}
	}

	public static class ListViewFragment extends Fragment {

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_section_list,
					container, false);
			return rootView;
		}
	}

}
