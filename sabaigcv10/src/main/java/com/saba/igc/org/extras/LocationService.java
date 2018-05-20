package com.saba.igc.org.extras;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.saba.igc.org.listeners.LocationChangeListener;


/**
 * @author Syed Aftab Naqvi
 * @create Jan 03, 2015
 * @version 1.0
 */

public class LocationService extends Service implements LocationListener {
    protected Context mContext;

    private final String TAG ="LocationService";
    private final long MIN_DISTANCE_FOR_UPDATE = 10;
    private final long MIN_TIME_FOR_UPDATE = 1000 * 60;

    private LocationChangeListener mLocationChangeListener;
    private LocationManager mLocationManager;

    public LocationService(Context context) {
    	mContext = context;
        startLocationManager();
    }

    /**
     * Get provider name.
     * @return Name of best suiting provider.
     * */
    String getProviderName() {
        LocationManager locationManager = (LocationManager) mContext
                .getSystemService(Context.LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        criteria.setPowerRequirement(Criteria.POWER_LOW); // Chose your desired power consumption level.
        criteria.setAccuracy(Criteria.ACCURACY_LOW); // Choose your accuracy requirement.
        criteria.setSpeedRequired(false); // Chose if speed for first location fix is required.
        criteria.setAltitudeRequired(false); // Choose if you use altitude.
        criteria.setBearingRequired(false); // Choose if you use bearing.
        criteria.setCostAllowed(false); // Choose if this provider can waste money :-)

        // Provide your criteria and flag enabledOnly that tells
        // LocationManager only to return active providers.
        return locationManager.getBestProvider(criteria, true);
    }

    public void startLocationManager() {
        mLocationManager = (LocationManager)mContext
                .getSystemService(LOCATION_SERVICE);
        boolean enabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        String providerName = getProviderName();
        if (mLocationManager.isProviderEnabled(providerName) == false){
            Log.d(TAG, "startLocationManager - provider " + providerName + " is disabled");
            return;
        }
        mLocationManager.requestLocationUpdates(getProviderName(),
                    MIN_TIME_FOR_UPDATE, MIN_DISTANCE_FOR_UPDATE, this);
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "onLocationChanged - Latitude: " + location.getLatitude() + " - Longitude: " + location.getLongitude());
        if(mLocationChangeListener != null)
            mLocationChangeListener.onLocationChanged(location);
        mLocationManager.removeUpdates(this);
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d(TAG, "onProviderDisabled - " + provider);
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d(TAG, "onProviderEnabled - " + provider);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }
}