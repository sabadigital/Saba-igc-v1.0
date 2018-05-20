package com.saba.igc.org.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.Marker;
import com.saba.igc.org.R;
// A very good example found at
// http://androidfreakers.blogspot.com/2013/08/display-custom-info-window-with.html

/**
 * Created by snaqvi on 6/12/15.
 */
public class CustomInfoWindowAdapter implements InfoWindowAdapter{

    private Context mContext;
    private final View mInfoContentsView;

    public CustomInfoWindowAdapter(Context context){
        mContext = context;
        mInfoContentsView = LayoutInflater.from(mContext).inflate(R.layout.custom_info_window, null);
    }

    @Override
    public View getInfoContents(Marker marker) {
//        if(marker != null) {
//            TextView tvTitle = ((TextView) mInfoContentsView.findViewById(R.id.tvInfoWindowName));
//            if(tvTitle!=null) {
//                marker.setTitle((String) tvTitle.getText());
//            }
//
//            TextView tvSnippet = ((TextView) mInfoContentsView.findViewById(R.id.tvInfoWindowAddress));
//            if(tvSnippet!=null)
//                marker.setSnippet((String) tvSnippet.getText());
//        }
        return mInfoContentsView;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        // TODO Auto-generated method stub
        return null;
    }

}