package com.saba.igc.org.fragments;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Html;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

public class ContactAndDirectionsFragment extends Fragment implements OnInfoWindowClickListener{
    private static final LatLng SABA_LOCATION = new LatLng(37.421177, -121.958697);
    private final float DEFAULT_ZOOM = 13.0f;
    public GoogleMap mMap; // Might be null if Google Play services APK is not available.
    public SupportMapFragment mMapFragment;
    private View mView;
    public ContactAndDirectionsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        getActivity().setTitle("");// Need this to make it little compatible with API 16. might work for API 14 as well.

        // Inflate the layout for this fragment
        //http://stackoverflow.com/questions/19708541/android-maps-v2-crashes-when-reopened-in-fragment
        if (mView != null) {
            ViewGroup parent = (ViewGroup) mView.getParent();
            if (parent != null) {
                parent.removeView(mView);
            }
        }
        try {
            mView = inflater.inflate(R.layout.fragment_contact_and_directions, container, false);
        } catch (InflateException e) {

        }

        //http://michalu.eu/wordpress/android-mapfragment-nested-in-parent-fragment/
        mMapFragment = MyMapFragment.newInstance(new LatLng(37.421177, -121.958697), this);

        getChildFragmentManager().beginTransaction().add(R.id.map, mMapFragment).commit();
        String address = getResources().getString(R.string.saba_address_with_phone);
        TextView tvAddress = (TextView) mView.findViewById(R.id.addressValue);
        if(address!=null) {
            tvAddress.setText(Html.fromHtml(address));
        }
//
//        TextView tvMapView = (TextView) view.findViewById(R.id.tvMapView);
//
//        tvMapView.setMovementMethod(LinkMovementMethod.getInstance());
//        Spannable mySpannable = (Spannable)tvMapView.getText();
//        ClickableSpan myClickableSpan = new ClickableSpan()
//        {
//            @Override
//            public void onClick(View widget) {
//                Intent intent = new Intent(getActivity(), MapActivity.class);
//                startActivity(intent);
//            }
//        };
//        mySpannable.setSpan(myClickableSpan, 0, mySpannable.length(), Spannable.SPAN_POINT_POINT);

        return mView;
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
             FragmentManager m = getActivity().getSupportFragmentManager();
            mMap = ((SupportMapFragment)m.findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    public void setUpMap() {
        // Saba location's lat & long
        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(getActivity().getApplicationContext()));
        Marker saba = mMap.addMarker(new MarkerOptions()
                .position(SABA_LOCATION)
                .title("Saba Islamic Center")
                .snippet("4415 Fortran Ct. San Jose, CA 95134")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        saba.showInfoWindow();


        // mMap.setMyLocationEnabled(true); enables the GPS, which will consume more battery. not worth it.
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
        //google.navigation:q=latitude,longitude
        //https://developers.google.com/maps/documentation/android/intents
        Uri gmmIntentUri = Uri.parse("google.navigation:q=4415 Fortran Ct. San Jose, CA 95134");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        try {
            startActivity(mapIntent);
        } catch (ActivityNotFoundException e){
            e.printStackTrace();
            trackAnalytics(false);
            showAlert();
            return;
        }

        trackAnalytics(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
    
    protected void trackAnalytics(boolean success){
        SabaApplication.sendAnalyticsEvent(getResources().getString(R.string.contact_directions_fragment),
                getResources().getString(R.string.event_category_contact_directions),
                getResources().getString(R.string.clicked),
                (success == true) ? getResources().getString(R.string.directions_to_saba_info_window)
                        : "Directions to SABA - Failed to launch Google Maps");

    }

    public void showAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

        // Setting Dialog Title
        alertDialog.setTitle("Google Maps not found on the device to show directions to SABA.");

        // on pressing OK button
        alertDialog.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }
}

