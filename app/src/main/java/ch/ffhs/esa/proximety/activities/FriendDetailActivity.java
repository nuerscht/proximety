package ch.ffhs.esa.proximety.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ch.ffhs.esa.proximety.R;
import ch.ffhs.esa.proximety.consts.ProximetyConsts;
import ch.ffhs.esa.proximety.domain.Friend;
import ch.ffhs.esa.proximety.helper.LoadingDialogHelper;
import ch.ffhs.esa.proximety.helper.LocationHelper;
import ch.ffhs.esa.proximety.service.binder.friend.FriendServiceBinder;
import ch.ffhs.esa.proximety.service.handler.ResponseHandler;

public class FriendDetailActivity extends ActionBarActivity implements ActionBar.TabListener, OnMapReadyCallback {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;
    Friend friend;

    @Override
    @SuppressWarnings("deprecation")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_detail);

        //Todo: Get Data with given user id
        //Toast.makeText(getApplicationContext(), getIntent().getExtras().getString(ProximetyConsts.FRIENDS_DETAIL_FRIEND_ID), Toast.LENGTH_SHORT).show();

        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

            actionBar.addTab(
                    actionBar.newTab()
                            .setText(getText(R.string.details))
                            .setTabListener(this));
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(getText(R.string.map))
                            .setTabListener(this));


        String friendId = getIntent().getExtras().getString(ProximetyConsts.FRIENDS_DETAIL_FRIEND_ID);


        final Dialog loadingDialog = LoadingDialogHelper.createDialog(this);
        loadingDialog.show();
        FriendServiceBinder fsb = new FriendServiceBinder(getApplicationContext(), loadingDialog);

        fsb.getFriendDetails(friendId, new ResponseHandler(getApplicationContext()) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, Object response) {
                loadingDialog.cancel();
                if(statusCode == 200){
                    JSONArray resp = (JSONArray) response;
                    try {
                        JSONObject singleRow = resp.getJSONObject(0);
                        onFriendDetailsLoaded(singleRow);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), getErrorMessage(response), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void onFriendDeleteSuccess() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void onToggleButtonClick(View button) {
        FriendServiceBinder fsb = new FriendServiceBinder(getApplicationContext(), null);
        fsb.updateSettings(friend.id, ((ToggleButton)button).isChecked(), new ResponseHandler(getApplicationContext()) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, Object response) {
                Toast.makeText(getApplicationContext(), R.string.setting_saved, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onButtonDeleteFriendClick(View button) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FriendServiceBinder fsb = new FriendServiceBinder(getApplicationContext(), null);
                fsb.deleteFriend(friend.id, new ResponseHandler(getApplicationContext()) {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, Object response) {
                        onFriendDeleteSuccess();
                    }
                });
            }
        });

        builder.setMessage(getText(R.string.friend_delete_info)).setTitle(getText(R.string.friend_delete));
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void onFriendDetailsLoaded(JSONObject response) {

        TextView name = (TextView) findViewById(R.id.text_friend_name);
        TextView distanceView = (TextView) findViewById(R.id.text_distance_number);
        MapView map = (MapView) findViewById(R.id.friend_map);

        Gson gson = new Gson();
        friend = gson.fromJson(response.toString(), Friend.class);

        name.setText(friend.name);

        if(friend.isLocationSet()) {
            Location friendLocation = new Location("Friend location");
            friendLocation.setLatitude(friend.latitude);
            friendLocation.setLongitude(friend.longitude);

            Location ownLocation = LocationHelper.getLocationOwn(this);

            Float distance = friendLocation.distanceTo(ownLocation);

            distanceView.setText(LocationHelper.convertDistance(distance));

            // Get the map and update location there too
            map.getMapAsync(this);

        } else {
            distanceView.setText(getText(R.string.na));
        }

    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    /**
     * Callback for the friend loading (basic setup is done inside fragment)
     *
     * @param googleMap The map you get to use
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLngBounds.Builder latLngBuilder = new LatLngBounds.Builder();
        Location location = LocationHelper.getLocationOwn(this);
        latLngBuilder.include(new LatLng(location.getLatitude(), location.getLongitude()));
        googleMap.addMarker(new MarkerOptions().title("Me")
                .position(new LatLng(location.getLatitude(), location.getLongitude()))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

        if(friend.isLocationSet()){

            LatLng friendLocation = new LatLng(friend.latitude, friend.longitude);
            latLngBuilder.include(friendLocation);

            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(friendLocation, 13));

            googleMap.addMarker(new MarkerOptions().title(friend.name)
                    .snippet(LocationHelper.getAddressDescription(friend.latitude, friend.longitude, this))
                    .position(friendLocation));
        }

        //Move Camera on map
        LatLngBounds latLngBounds = latLngBuilder.build();
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(latLngBounds, 200);
        googleMap.moveCamera(cu);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 1:
                    // Reusing the map fragment!?
                    return new MapViewFragment();
                default:
                    return new FriendDetailFragment();
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
            View rootView = inflater.inflate(R.layout.fragment_friend_map,
                    container, false);

            // Gets the MapView from the XML layout and creates it
            mapView = (MapView) rootView.findViewById(R.id.friend_map);
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
    }

    public static class FriendDetailFragment extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_friend_detail,
                    container, false);
        }
    }


}
