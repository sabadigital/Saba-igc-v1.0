package com.saba.igc.org.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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

public class ContactAndDirectionsFragment extends Fragment implements OnInfoWindowClickListener{
    private static final LatLng SABA_LOCATION = new LatLng(37.421177, -121.958697);
    private final float DEFAULT_ZOOM = 13.0f;
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private SupportMapFragment mMapFragment;

    public ContactAndDirectionsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        getActivity().setTitle("");// Need this to make it little compatible with API 16. might work for API 14 as well.

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contact_and_directions, container, false);

        //http://stackoverflow.com/questions/25051246/how-to-use-supportmapfragment-inside-a-fragment
        // this post was really helpful to get this working.
        mMapFragment = new SupportMapFragment() {
            @Override
            public void onActivityCreated(Bundle savedInstanceState) {
                super.onActivityCreated(savedInstanceState);
                mMap = mMapFragment.getMap();
                if (mMap != null) {
                    setUpMap();
                }
            }
        };
        getChildFragmentManager().beginTransaction().add(R.id.map, mMapFragment).commit();

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

        return view;
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

    private void setUpMap() {
        // Saba location's lat & long
        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(getActivity().getApplicationContext()));
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
        Toast.makeText(getActivity(), marker.getSnippet(), Toast.LENGTH_LONG).show();
        //google.navigation:q=latitude,longitude
        //https://developers.google.com/maps/documentation/android/intents
        Uri gmmIntentUri = Uri.parse("google.navigation:q=4415 Fortran Ct. San Jose, CA 95134");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
