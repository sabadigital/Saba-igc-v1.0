package com.saba.igc.org.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.saba.igc.org.R;
import com.saba.igc.org.adapters.CustomInfoWindowAdapter;
import com.saba.igc.org.application.SabaApplication;

public class MapActivity extends AppCompatActivity implements OnInfoWindowClickListener{
    private static final LatLng SABA_LOCATION = new LatLng(37.421177, -121.958697);
    private final float DEFAULT_ZOOM = 13.0f;
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        setUpMapIfNeeded();

        // setting toolbar here...
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if(toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("SABA's Map View");
            getSupportActionBar().setSubtitle("Really it is showing");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
//        // Do a null check to confirm that we have not already instantiated the map.
//        if (mMap == null) {
//            // Try to obtain the map from the SupportMapFragment.
//            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
//                    .getMapAsync(new OnMapReadyCallBack(){});
//            // Check if we were successful in obtaining the map.
//            if (mMap != null) {
//                setUpMap();
//            }
//        }
    }

    private void setUpMap() {
        // Saba location's lat & long
        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(getApplicationContext()));
        //mMap.addMarker(new MarkerOptions().position(SABA_LOCATION).title("Marker"));
        Marker saba = mMap.addMarker(new MarkerOptions()
                .position(SABA_LOCATION)
                .title("Saba Islamic Center")
                .snippet("4415 Fortran Ct. San Jose, CA 95134")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        saba.showInfoWindow();
        mMap.setMyLocationEnabled(true);

        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setAllGesturesEnabled(true);
        mMap.setTrafficEnabled(true);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(SABA_LOCATION, DEFAULT_ZOOM));
        mMap.setOnInfoWindowClickListener(this);
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Toast.makeText(this, marker.getSnippet(), Toast.LENGTH_LONG).show();
        //google.navigation:q=latitude,longitude
        //https://developers.google.com/maps/documentation/android/intents
        Uri gmmIntentUri = Uri.parse("google.navigation:q=4415 Fortran Ct. San Jose, CA 95134");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                this.finish();
                break;
            default:
                break;
        }
        return true;
    }

    private void trackAnalytics(String action){
        // All subsequent hits will be send with screen name = "main screen"
        SabaApplication.tracker().setScreenName("Prayer Times Screen");

        SabaApplication.tracker().send(new HitBuilders.EventBuilder()
                .setCategory("Prayer Times")
                .setAction(action)
                .setLabel("")
                .build());
    }
}
