package ch.ffhs.esa.proximety.helper;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import java.io.IOException;
import java.util.List;

import ch.ffhs.esa.proximety.consts.ProximetyConsts;

/**
 * Created by Patrick Bösch.
 */
public class LocationHelper {


    public static String convertDistance(float distance) {
        if (distance < 1000) {

            return Integer.toString(Math.round(distance)).concat(" m");
        } else {
            distance = distance / 100;
            distance = Math.round(distance);
            distance = (float) (distance / 10.0);
            return Float.toString(distance).concat(" km");
        }
    }

    public static Location getLocationOwn(Activity activity) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(ProximetyConsts.PROXIMETY_SHARED_PREF, Context.MODE_PRIVATE);
        Location locationOwn = new Location("");
        locationOwn.setLatitude(sharedPreferences.getFloat(ProximetyConsts.PROXIMETY_SHARED_PREF_LATITUDE, 0));
        locationOwn.setLongitude(sharedPreferences.getFloat(ProximetyConsts.PROXIMETY_SHARED_PREF_LONGITUDE, 0));

        return locationOwn;
    }

    public static String getAddressDescription(double latitude, double longitude, Activity activity) {
        try {
            Geocoder geocoder = new Geocoder(activity.getApplicationContext());
            List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 1);
            Address address = addressList.get(0);
            String addressDescription = "";
            for (int j = 0; j < address.getMaxAddressLineIndex(); j++) {
                if (j != 0)
                    addressDescription = addressDescription.concat(", ");

                addressDescription = addressDescription.concat(address.getAddressLine(j).toString());
            }

            /*if (!address.getCountryName().isEmpty())
                addressDescription = addressDescription.concat(", ").concat(address.getCountryName());*/

            return addressDescription;
        } catch (IOException e) {
            return "";
        }
    }
}
