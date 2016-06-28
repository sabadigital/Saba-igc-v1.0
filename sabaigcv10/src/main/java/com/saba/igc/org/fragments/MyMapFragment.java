package com.saba.igc.org.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

// We need to refactor/redesign this class. Currently, it is working...
//http://michalu.eu/wordpress/android-mapfragment-nested-in-parent-fragment/
public class MyMapFragment extends SupportMapFragment {
    private LatLng mPosition;
    private ContactAndDirectionsFragment mHostFragment;
    public MyMapFragment() {
        super();
    }

        public static MyMapFragment newInstance(LatLng position, ContactAndDirectionsFragment hostFragment) {
            MyMapFragment frag = new MyMapFragment();
            frag.mPosition = position;
            frag.mHostFragment = hostFragment;
            return frag;
        }

    @Override
    public View onCreateView(LayoutInflater arg0, ViewGroup arg1, Bundle arg2) {
        View v = super.onCreateView(arg0, arg1, arg2);
        initMap();
        return v;
    }

    private void initMap() {
        getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(mPosition, 15));
        getMap().addMarker(new MarkerOptions().position(mPosition));
        getMap().getUiSettings().setAllGesturesEnabled(true);
        getMap().getUiSettings().setCompassEnabled(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mHostFragment.mMap = mHostFragment.mMapFragment.getMap();
        if (mHostFragment.mMap != null) {
            mHostFragment.setUpMap();
        }
    }
}
