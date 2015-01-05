package ch.ffhs.esa.proximety.activities;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
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
import ch.ffhs.esa.proximety.async.GravatarImage;
import ch.ffhs.esa.proximety.consts.ProximetyConsts;
import ch.ffhs.esa.proximety.delegate.DrawerNavActivityDelegate;
import ch.ffhs.esa.proximety.domain.Friend;
import ch.ffhs.esa.proximety.domain.Message;
import ch.ffhs.esa.proximety.helper.Gravatar;
import ch.ffhs.esa.proximety.helper.LocationHelper;
import ch.ffhs.esa.proximety.list.FriendList;
import ch.ffhs.esa.proximety.service.binder.friend.FriendServiceBinder;
import ch.ffhs.esa.proximety.service.binder.location.LocationServiceBinder;
import ch.ffhs.esa.proximety.service.binder.user.UserServiceBinder;
import ch.ffhs.esa.proximety.service.handler.ResponseHandler;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.List;

/*
 * Main view of the app using tabs and fragments
 * 
 * Tabbing has been built according to the sample app at 
 * http://developer.android.com/training/implementing-navigation/lateral.html
 */
public class MainActivity extends ActionBarActivity implements ConnectionCallbacks, OnConnectionFailedListener, LocationListener{
    protected static final String TAG = MainActivity.class.getName();
    //300000 = 5 Minutes
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 300000;
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";

    String SENDER_ID = "201494589339";

	AppSectionsPagerAdapter sectionsPagerAdapter;
	ViewPager viewPager;
    DrawerLayout drawerLayout;
    ListView drawerList;

    //Drawer Navigation
    DrawerNavActivityDelegate drawerDelegate;

    protected GoogleApiClient mGoogleApiClient;
    protected LocationRequest mLocationRequest;

    static MapViewFragment mapViewFragment;

    GoogleCloudMessaging gcm;
    String regid;


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

		final ActionBar actionBar = getSupportActionBar();

		// Create the adapter which returns the section pages fragments
		sectionsPagerAdapter = new AppSectionsPagerAdapter(
				getSupportFragmentManager());

		// Specify that tabs should be displayed in the action bar.
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        //Workaround for Icon in Android older 5.0
        actionBar.setDisplayShowHomeEnabled(false);

		ActionBar.TabListener tabListener = new ActionBar.TabListener() {
			@Override
			public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
				viewPager.setCurrentItem(tab.getPosition());
			}

			@Override
			public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
				// empty for now
			}

			@Override
			public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
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

        buildGoogleApiClient();

        if (checkPlayServices()) {
            Context context = getApplicationContext();

            gcm = GoogleCloudMessaging.getInstance(this);
            regid = getRegistrationId(context);

            if (regid.isEmpty()) {
                registerInBackground();
            }
        }
    }

    /**
     * Registers the application with GCM servers asynchronously.
     * <p>
     * Stores the registration ID and app versionCode in the application's
     * shared preferences.
     */
    private void registerInBackground() {
        final Context context = getApplicationContext();
        new AsyncTask() {
            @Override
            protected String doInBackground(Object... params) {
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    Log.i(TAG, "SENDER_ID: " + SENDER_ID);
                    regid = gcm.register(SENDER_ID);
                    Log.i(TAG, "regid: " + regid);

                    // You should send the registration ID to your server over HTTP,
                    // so it can use GCM/HTTP or CCS to send messages to your app.
                    // The request to your server should be authenticated if your app
                    // is using accounts.
                    sendRegistrationIdToBackend();

                    // For this demo: we don't need to send it because the device
                    // will send upstream messages to a server that echo back the
                    // message using the 'from' address in the message.

                    // Persist the regID - no need to register again.
                    storeRegistrationId(context, regid);
                } catch (IOException ex) {
                    Log.i(TAG, "IOException: " + ex.getMessage());
                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                }
                return "";
            }
        }.execute(null, null, null);
    }

    /**
     * Sends the registration ID to your server over HTTP, so it can use GCM/HTTP
     * or CCS to send messages to your app. Not needed for this demo since the
     * device sends upstream messages to a server that echoes back the message
     * using the 'from' address in the message.
     */
    private void sendRegistrationIdToBackend() {
        Log.i(TAG, "sendRegistrationIdToBackend" + regid);

        UserServiceBinder usb = new UserServiceBinder(getApplicationContext());
        usb.setClientId(regid, new ResponseHandler(getApplicationContext()) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, Object response) {
                Log.i(TAG, ((Message)response).message);
            }
        });
    }

    private void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getGCMPreferences();
        int appVersion = getAppVersion(context);
        Log.i(TAG, "Saving regId " + regId + " on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }

    /**
     * Gets the current registration ID for application on GCM service.
     * <p>
     * If result is empty, the app needs to register.
     *
     * @return registration ID, or empty string if there is no existing
     *         registration ID.
     */
    private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGCMPreferences();
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i(TAG, "Registration not found.");
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing regID is not guaranteed to work with the new
        // app version.
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i(TAG, "App version changed.");
            return "";
        }
        return registrationId;
    }

    /**
     * @return Application's {@code SharedPreferences}.
     */
    private SharedPreferences getGCMPreferences() {
        // This sample app persists the registration ID in shared preferences, but
        // how you store the regID in your app is up to you.
        return getSharedPreferences(MainActivity.class.getSimpleName(),
                Context.MODE_PRIVATE);
    }

    /**
     * @return Application's version code from the {@code PackageManager}.
     */
    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        checkPlayServices();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        createLocationRequest();
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();

        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public void onConnected(Bundle bundle) {
        startLocationUpdates();
    }

    protected void startLocationUpdates() {
        // The final argument to {@code requestLocationUpdates()} is a LocationListener
        // (http://developer.android.com/reference/com/google/android/gms/location/LocationListener.html).
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.i(TAG, "Location update: lat " + location.getLatitude() + "/long " + location.getLongitude());


        SharedPreferences sharedPreferences = getSharedPreferences(ProximetyConsts.PROXIMETY_SHARED_PREF, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(ProximetyConsts.PROXIMETY_SHARED_PREF_LATITUDE, (float) location.getLatitude());
        editor.putFloat(ProximetyConsts.PROXIMETY_SHARED_PREF_LONGITUDE, (float) location.getLongitude());
        editor.commit();

        LocationServiceBinder lsb = new LocationServiceBinder(getApplicationContext());
        lsb.updateLocation(location.getLatitude(), location.getLongitude(), new ResponseHandler(getApplicationContext()) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, Object response) {
                Log.i(TAG, ((Message)response).message);
            }
        });
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());
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
                startActivity(new Intent(this, FriendAddActivity.class));
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
                if (mapViewFragment == null)
                    mapViewFragment = new MapViewFragment();
				return mapViewFragment;

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
            super.onResume();
            if (mapView != null) {
                mapView.onResume();
            }
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
		}

        public void setMapMarker (double latitude, double longitude, String title, String address) {
            LatLng place = new LatLng(latitude, longitude);

            map.moveCamera(CameraUpdateFactory.newLatLngZoom(place, 13));

            map.addMarker(new MarkerOptions().title(title)
                    .snippet(address)
                    .position(place));
        }

        public void removeMarkers() {
            map.clear();
        }
	}

	public static class ListViewFragment extends Fragment {
		@Override
		public View onCreateView(final LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.fragment_section_list,
					container, false);

            FriendServiceBinder fsb = new FriendServiceBinder(getActivity().getApplicationContext());
            fsb.getListOfFriends(new ResponseHandler(getActivity().getApplicationContext()) {
                @Override
                public void onSuccess(int statusCode, Header[] headers, Object response) {
                    if (statusCode == 200) {
                        onListLoadSuccess(inflater, rootView, (JSONArray)response);
                    } else {
                        Toast.makeText(getApplicationContext(), getErrorMessage(response), Toast.LENGTH_SHORT).show();
                    }
                }
            });

			return rootView;
		}

        private void onListLoadSuccess(LayoutInflater inflater, View rootView, JSONArray friendListJson) {
            String[] friends = new String[friendListJson.length()];
            String[] places = new String[friendListJson.length()];
            String[] distance = new String[friendListJson.length()];
            final Bitmap[] images = new Bitmap[friendListJson.length()];
            final String[] ids = new String[friendListJson.length()];
            final FriendList friendList = new FriendList(inflater, friends, places, distance, images);

            //Create own location
            Location locationOwn = LocationHelper.getLocationOwn(getActivity());

            Gson gson = new Gson();

            //Remove all markers from map
            if (mapViewFragment != null)
                mapViewFragment.removeMarkers();

            for(int i = 0; i < friendListJson.length(); i++) {
                try {
                    Friend friend = gson.fromJson(friendListJson.getJSONObject(i).toString(), Friend.class);
                    ids[i] = friend.id;
                    friends[i] = friend.name;
                    if (friend.isLocationSet()) {
                        places[i] = LocationHelper.getAddressDescription(friend.latitude, friend.longitude, getActivity());

                        //Get Distance
                        Location locationFriend = new Location("");
                        locationFriend.setLatitude(friend.latitude);
                        locationFriend.setLongitude(friend.longitude);

                        distance[i] = LocationHelper.convertDistance(locationOwn.distanceTo(locationFriend));

                        //place marker in map
                        if (mapViewFragment != null)
                            mapViewFragment.setMapMarker(friend.latitude, friend.longitude, friend.name, places[i]);
                    } else {
                        places[i] = "";
                        distance[i] = ((String)getText(R.string.na)).concat(" m");
                    }
                    images[i] = BitmapFactory.decodeResource(getResources(), R.drawable.placeholder_friend);

                    Gravatar gravatar = new Gravatar(friend.email, i);

                    new GravatarImage() {
                        @Override
                        protected void onPostExecute(Gravatar gravatar) {
                            if (gravatar.getImage() != null) {
                                images[gravatar.getPosition()] = gravatar.getImage();
                                //Handle refresh data
                                friendList.refresh();
                            }
                        }
                    }.execute(gravatar);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            friendList.refresh();
            //List view
            final ListView listView = (ListView)rootView.findViewById(R.id.listview);
            listView.setAdapter(friendList);
            listView.setClickable(true);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                    Intent intent = new Intent(getActivity(), FriendDetailActivity.class);
                    intent.putExtra(ProximetyConsts.FRIENDS_DETAIL_FRIEND_ID, ids[position]);
                    startActivity(intent);
                    getActivity().finish();
                }
            });

            if (friendListJson.length() == 0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                builder.setPositiveButton(R.string.welcome_go, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getActivity(), FriendAddActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    }
                });

                builder.setMessage(getText(R.string.welcome_message)).setTitle(getText(R.string.welcome_message_title));
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                finish();
            }
            return false;
        }
        return true;
    }
}
