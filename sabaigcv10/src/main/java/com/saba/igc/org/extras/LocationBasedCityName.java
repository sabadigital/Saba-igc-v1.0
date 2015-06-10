package com.saba.igc.org.extras;


import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * @author Syed Aftab Naqvi
 * @create Jan 02, 2015
 * @version 1.0
 */

public class LocationBasedCityName {
    private final String TAG = "LocationAddress";

    public void getAddressFromLocation(final double latitude, final double longitude,
                                              final Context context, final Handler handler) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                StringBuilder sb = new StringBuilder();
                try {
                    List<Address> addressList = geocoder.getFromLocation(
                            latitude, longitude, 1);
                    if (addressList != null && addressList.size() > 0) {
                        Address address = addressList.get(0);
                        if(address != null) {
                            if(address.getLocality()==null)
                                sb.append(address.getSubLocality());
                            else
                                sb.append(address.getLocality());

                            sb.append(", ");
                            sb.append(address.getAdminArea());
                            if(address.getPostalCode() != null) {
                                sb.append(", ");
                                sb.append(address.getPostalCode());
                            }
                        }
                    }
                } catch (IOException e) {
                    Log.e(TAG, "Unable connect to Geocoder", e);
                } finally {
                    Message message = Message.obtain();
                    message.setTarget(handler);
                    Bundle bundle = new Bundle();
                    message.what = 1;
                    bundle.putString("cityname", sb.toString());
                    message.setData(bundle);
                    message.sendToTarget();
                }
            }
        }; 
        thread.start();
    }
}
