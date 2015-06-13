package com.saba.igc.org.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.saba.igc.org.R;
import com.saba.igc.org.activities.MapActivity;

public class ContactAndDirectionsFragment extends Fragment {

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
        getActivity().setTitle("Contact and Directions");

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contact_and_directions, container, false);
        TextView tvMapView = (TextView) view.findViewById(R.id.tvMapView);


        tvMapView.setMovementMethod(LinkMovementMethod.getInstance());
        //tvMapView.setText(myString, BufferType.SPANNABLE);
        Spannable mySpannable = (Spannable)tvMapView.getText();
        ClickableSpan myClickableSpan = new ClickableSpan()
        {
            @Override
            public void onClick(View widget) {
                Intent intent = new Intent(getActivity(), MapActivity.class);
                startActivity(intent);
            }
        };
        mySpannable.setSpan(myClickableSpan, 0, mySpannable.length(), Spannable.SPAN_POINT_POINT);



//        tvMapView.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                if(v.getId() == R.id.tvMapView){
//                    Log.d("Contact", "Show the mapView");
//                    Intent intent = new Intent(getActivity(), MapActivity.class);
//                    startActivity(intent);
//                }
//            }
//        });

        return view;
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
