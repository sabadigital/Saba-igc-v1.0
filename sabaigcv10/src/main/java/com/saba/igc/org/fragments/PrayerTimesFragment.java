package com.saba.igc.org.fragments;

import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.saba.igc.org.R;
import com.saba.igc.org.adapters.PrayTimeAdapter;
import com.saba.igc.org.application.SabaClient;
import com.saba.igc.org.extras.LocationBasedCityName;
import com.saba.igc.org.extras.LocationService;
import com.saba.igc.org.listeners.LocationListenerForPrayers;
import com.saba.igc.org.listeners.SabaServerResponseListener;
import com.saba.igc.org.models.PrayTime;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

/**
 * @author Syed Aftab Naqvi
 * @create December, 2014
 * @version 1.0
 */
public class PrayerTimesFragment extends Fragment implements SabaServerResponseListener, LocationListenerForPrayers{
	
	private final String TAG = "PrayerTimesFragment";
	private TextView 			mTvCityName;
	private LocationService 	mLocationService;
	private List<PrayTime> 		mPrayTimes;
	//private eu.erikw.PullToRefreshListView	 		mLvPrayTimes;
	private ListView	 		mLvPrayTimes;
	private PrayTimeAdapter 	mAdapter;
	private ProgressBar 		mPrayTimesProgressBar;
	private boolean				mDestroyed;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		refresh();
		mDestroyed = false;
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		getActivity().setTitle("Prayer Times");
		View view = inflater.inflate(R.layout.fragment_pray_times, container, false);		
		setupUI(view);

//		mLvPrayTimes.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
//			@Override
//			public void onRefresh() {
//				// Your code to refresh the list here.
//				// Make sure you call listView.onRefreshComplete() when
//				// once the network request has completed successfully.
//				getLocationBasedAddress();
//			}
//		});
		return view;
	}

	@Override
	public void onDestroyView(){
		super.onDestroyView();
		mDestroyed = true;
	}

	@Override
	public void onDestroy(){
		super.onDestroy();
		mDestroyed = true;
	}

	private void setupUI(View view) {
		mTvCityName 			= (TextView) view.findViewById(R.id.tvCityName);
		TextView tvTodayDate 	= (TextView) view.findViewById(R.id.tvEnglishDate);
		//mLvPrayTimes 			= (eu.erikw.PullToRefreshListView) view.findViewById(R.id.lvPrayTimes);
		mLvPrayTimes 			= (ListView) view.findViewById(R.id.lvPrayTimes);
		mPrayTimesProgressBar 	= (ProgressBar) view.findViewById(R.id.prayTimesProgressBar);
		
		if(tvTodayDate != null){
			DateFormat dateInstance = SimpleDateFormat.getDateInstance(DateFormat.FULL);
			tvTodayDate.setText(dateInstance.format(Calendar.getInstance().getTime()));
		}
	}

	//Implementation of LocationListenerForPrayers.
	public void locationUpdated(Location newLocation)
	{
		if(mDestroyed) {
			Log.d(TAG, "LocationListenerForPrayers::locationUpdated - Fragment has been destroyed. returning. Can't get the prayer times.");
			return;
		}

		Log.d(TAG, "LocationListenerForPrayers::locationUpdated - Will try to get PrayerTimes for - Latitude: " + newLocation.getLatitude() + " - Longitude: " + newLocation.getLongitude());
		// getting prayer times from http://praytime.info/
		SabaClient client = SabaClient.getInstance(getActivity());
		client.getPrayTimes(getCurrentTimezoneOffsetInMinutes(), newLocation.getLatitude(), newLocation.getLongitude(), this);

		// initiate the request to get the city name based of current latitude and longitude.
		LocationBasedCityName locationBasedCityName = new LocationBasedCityName();
		locationBasedCityName.getAddressFromLocation(newLocation.getLatitude(), newLocation.getLongitude(),
				getActivity(), new GeocoderHandler());
	}

    @Override
	public void processJsonObject(String programName, JSONObject response) {
		mPrayTimesProgressBar.setVisibility(View.GONE);
		//mLvPrayTimes.onRefreshComplete();

		//{"Fajr":"05:59","Isha":"18:18","Asr":"14:43","Dhuhr":"12:11","Sunset":"17:01","Sunrise":"07:21","Maghrib":"17:19","Imsaak":"05:48"}
		if(response == null){
			Log.d(TAG, "prayer times - json object is null");
			return;
		}
		
		mPrayTimes = PrayTime.fromJSON(response);
		mAdapter = new PrayTimeAdapter(getActivity(), mPrayTimes); 
		mLvPrayTimes.setAdapter(mAdapter);
	}

	@Override
	public void processJsonObject(String programName, JSONArray response) {
		// we are not expecting any data here in this case....
	}

	private String getCurrentTimezoneOffsetInMinutes() {

		TimeZone tz = TimeZone.getDefault();
		Calendar cal = GregorianCalendar.getInstance(tz);
		int offsetInMillis = tz.getOffset(cal.getTimeInMillis());

		String offset = String.format("%d", offsetInMillis/(1000*60)); // offset in minutes

		return offset;
	}

	private void refresh(){
		if(mLocationService == null)
			mLocationService = new LocationService(
					getActivity(), this);
		else
			mLocationService.startLocationManager();
	}

	private class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            String cityName;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    cityName = bundle.getString("cityname");
                    break;
                default:
                	Log.d(TAG, "city name not found.");
                	cityName = null;
            }
			Log.d(TAG, "Prayer City Name: " + cityName);
            mTvCityName.setText(cityName);
        }
    }
}
