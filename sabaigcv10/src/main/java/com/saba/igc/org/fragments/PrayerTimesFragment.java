package com.saba.igc.org.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.saba.igc.org.R;
import com.saba.igc.org.adapters.PrayTimeAdapter;
import com.saba.igc.org.application.SabaApplication;
import com.saba.igc.org.application.SabaClient;
import com.saba.igc.org.extras.LocationBasedCityName;
import com.saba.igc.org.extras.PrayerLocation;
import com.saba.igc.org.listeners.LocationChangeListener;
import com.saba.igc.org.listeners.SabaServerResponseListener;
import com.saba.igc.org.models.PrayTime;
import com.saba.igc.org.models.PrayerTimes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

/**
 * @author Syed Aftab Naqvi
 * @create December, 2014
 * @version 1.0
 */
public class PrayerTimesFragment extends Fragment implements SabaServerResponseListener,
		LocationChangeListener {
	
	private final String TAG = "PrayerTimesFragment";
	private SabaClient			mSabaClient;
	private TextView 			mTvCityName;
	private TextView 			mTvTodayDate;
	private TextView 			mTvHijriDate;
	private List<PrayTime> 		mPrayTimes;
	private ListView	 		mLvPrayTimes;
	private PrayTimeAdapter 	mAdapter;
	private ProgressBar 		mPrayTimesProgressBar;
	private PrayerLocation 		mPrayerLocationService;
	private boolean				mPrayerTimesFromWebInProgress;
	//private SimpleLocation 		mLocation;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mPrayerLocationService = new PrayerLocation(getActivity());
		if(mPrayerLocationService != null)
			mPrayerLocationService.setListener(this);

		mSabaClient = SabaApplication.getSabaClient();

		// helps to display menu in fragments.
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		getActivity().setTitle("");// Need this to make it little compatible with API 16. might work for API 14 as well.
		View view = inflater.inflate(R.layout.fragment_pray_times, container, false);
		setupUI(view);
		setDates();

		return view;
	}

	@Override
	public void onDestroyView(){
		super.onDestroyView();
		Log.d(TAG, "onDestroyView");
		//mPrayerLocationService.stopLocationService();
	}

//	@Override
//	public void onAttach(){
//
//	}

	@Override
	public void onDestroy(){
		super.onDestroy();
	}

	private void setupUI(View view) {
		mTvCityName 			= (TextView) view.findViewById(R.id.tvCityName);
		mTvHijriDate			= (TextView) view.findViewById(R.id.tvHijriDate);
		mTvTodayDate		 	= (TextView) view.findViewById(R.id.tvEnglishDate);
		//mLvPrayTimes 			= (eu.erikw.PullToRefreshListView) view.findViewById(R.id.lvPrayTimes);
		mLvPrayTimes 			= (ListView) view.findViewById(R.id.lvPrayTimes);
		mPrayTimesProgressBar 	= (ProgressBar) view.findViewById(R.id.prayTimesProgressBar);

		if(mPrayTimesProgressBar!=null)
			mPrayTimesProgressBar.getIndeterminateDrawable().setColorFilter(0xFF446600, android.graphics.PorterDuff.Mode.MULTIPLY);


	}

	// mark for delete.
	//Implementation of LocationListenerForPrayers.
//	public void locationUpdated(Location newLocation)
//	{
//		Log.d(TAG, "LocationListenerForPrayers::locationUpdated - Will try to get PrayerTimes for - Latitude: " + newLocation.getLatitude() + " - Longitude: " + newLocation.getLongitude());
//		// getting prayer times from http://praytime.info/
//		mSabaClient.getPrayTimes(getCurrentTimezoneOffsetInMinutes(), newLocation.getLatitude(), newLocation.getLongitude(), this);
//
//		// initiate the request to get the city name based of current latitude and longitude.
//		LocationBasedCityName locationBasedCityName = new LocationBasedCityName();
//		locationBasedCityName.getAddressFromLocation(newLocation.getLatitude(), newLocation.getLongitude(),
//				getActivity(), new GeocoderHandler());
//	}

    @Override
	public void processJsonObject(String programName, JSONObject response) {
		mPrayTimesProgressBar.setVisibility(View.GONE);
		if(response == null){
			Log.d(TAG, "json object is null for :" + programName);
			return;
		}

		if(programName.compareToIgnoreCase("hijriDate") == 0){
			try{
				String hijriDate = response.getString("hijridate");
				if(hijriDate != null){
					Log.d(TAG, "HijriDate: " + hijriDate);
					if(mTvHijriDate != null){
						mTvHijriDate.setText(hijriDate);
					}
					mSabaClient.saveHijriDate(hijriDate);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			} finally {
				if(mPrayerTimesFromWebInProgress == true)
					mPrayTimesProgressBar.setVisibility(View.VISIBLE);

				return; // we should return from here. Processing of hijriDate is done here.
			}
		}
		mPrayerTimesFromWebInProgress = false;

		// Handling data returned from http://praytime.info
		//mLvPrayTimes.onRefreshComplete();
		//{"Fajr":"05:59","Isha":"18:18","Asr":"14:43","Dhuhr":"12:11","Sunset":"17:01","Sunrise":"07:21","Maghrib":"17:19","Imsaak":"05:48"}
		Log.d(TAG, "prayerTimes: " + response.toString());
		mPrayTimes = PrayTime.fromJSON(response);
		if(getActivity() != null) {
			mAdapter = new PrayTimeAdapter(getActivity(), mPrayTimes);
			mLvPrayTimes.setAdapter(mAdapter);
		}
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

	private void setDates(){
		// Setting english date.
		if(mTvTodayDate != null){
			DateFormat dateInstance = SimpleDateFormat.getDateInstance(DateFormat.FULL);
			mTvTodayDate.setText(dateInstance.format(Calendar.getInstance().getTime()));
		}

		// get Hijri date.
		String hijriDate = mSabaClient.getHijriDate();
		if(mTvHijriDate != null){
			mTvHijriDate.setText(hijriDate);
		}

		// try sending a network call to get the hijridate from SABA server.
		if(hijriDate==null || hijriDate.isEmpty()){
			mSabaClient.getHijriDate("hijriDate", this);
		}
	}

	private void getCurrentLocation(){
		// check if GPS enabled
		if(mPrayerLocationService.canGetLocation()){
			Log.d(TAG, "refresh - Will try to get PrayerTimes for - Latitude: " +
					mPrayerLocationService.getLatitude() + " - Longitude: " + mPrayerLocationService.getLongitude());

			mPrayerLocationService.setListener(this);
		}
	}
	public void showAlert(){
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

		// Setting Dialog Title
		alertDialog.setTitle("Couldn't find your city.");

		// Setting Dialog Message
		alertDialog.setMessage("Make sure, you are connected to internet and GPS is working. Please try again in few minutes.");

		// on pressing OK button
		alertDialog.setNegativeButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});

		// Showing Alert Message
		alertDialog.show();
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
                	Log.d(TAG, "Geocoder was not able to return city_name.");
                	cityName = null;
            }
			Log.d(TAG, "Prayer City Name: " + cityName);
            mTvCityName.setText(cityName);

			if(cityName == null || cityName.isEmpty()) {
				mPrayTimesProgressBar.setVisibility(View.GONE);
				showAlert();
				return;
			}

			// checking our database for city name and prayerTimes
			int index = cityName.indexOf(',');
			if(index != -1){
				String city = cityName.substring(0, index);
				if(!city.isEmpty())
					getPrayerTimes(city);
			}
        }
    }

	/**
	 * getPrayerTimes(city) - checks the database if prayerTimes exists for given city.
	 * If it does then will convert "PrayerTimes" to array to "PrayTime". I know it its confusing :(
	 *  sorry about it...
	 * **/
	public void getPrayerTimes(String city){
		List<PrayerTimes> prayerTimes = getTodayPrayerTimesFromDB(city);

		// following block of code converts "PrayerTimes"(read from database) to
		// "PrayTime"(adapter uses this array).
		if(prayerTimes!=null && prayerTimes.size()>0){
			mPrayTimesProgressBar.setVisibility(View.GONE);
			mPrayTimes = new ArrayList<PrayTime>(prayerTimes.size());
			PrayerTimes prayerTime = prayerTimes.get(0);

			if(prayerTime == null){
				Log.d(TAG, "Something went wrong. Dabatbase shoudn't return null.");
				return;
			}
			// Please don't change the order. It might effect the dispplay order.
			mPrayTimes.add(new PrayTime("Imsaak", prayerTime.getImsaak()));
			mPrayTimes.add(new PrayTime("Fajr", prayerTime.getFajar()));
			mPrayTimes.add(new PrayTime("Sunrise", prayerTime.getSunrise()));
			mPrayTimes.add(new PrayTime("Dhuhr", prayerTime.getZohar()));
			mPrayTimes.add(new PrayTime("Sunset", prayerTime.getSunset()));
			mPrayTimes.add(new PrayTime("Maghrib", prayerTime.getMaghrib()));
			mPrayTimes.add(new PrayTime("Midnight", prayerTime.getMidnight()));

			if(getActivity()!=null) {
				mAdapter = new PrayTimeAdapter(getActivity(), mPrayTimes);
				mLvPrayTimes.setAdapter(mAdapter);
			}
		} else {
			// Now, its time to get the prayer times from web.
			// It has two steps.
			// 1- Get the current loction,lat-lon then get the current city (we want to display the city on UI)
			// 2- Start network request to get the prayer times.
			// Once, we will get the date, pasrse it and display on UI.
			// getCurrentLocation(), takes care of if GPS is enabled.

			// we need to fix this, actually, we have the city but we want now to to get the prayer times only.
			// Rather going through location, we should request the prayer times:
			mSabaClient.getPrayTimes(getCurrentTimezoneOffsetInMinutes(), mPrayerLocationService.getLatitude(), mPrayerLocationService.getLongitude(), this);
			//getCurrentLocation();
		}
	}

	/**
	 * Wrapper method to get the prayerTimes from PrayerTimes Model.
	 *
	 * */
	public List<PrayerTimes> getTodayPrayerTimesFromDB(String city){
		// check database for this city and it it exists then we will get the prayertimes from our database.
		// otherwise get the prayertimes from praytime.info.
		StringBuilder sb = new StringBuilder();
		sb.append(Calendar.getInstance().get(Calendar.MONTH)); // month is zero based.
		sb.append("-");
		sb.append(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

		return PrayerTimes.getTodayPrayerTimes(city, sb.toString());
	}

	public void onLocationChanged(Location newLocation){
		Log.d(TAG, "LocationListenerForPrayers::onLocationChanged - Will try to get cityName and State from GeoCoder - Latitude: " + newLocation.getLatitude() + " - Longitude: " + newLocation.getLongitude());
		if(mPrayerLocationService!=null)
			mPrayerLocationService.stopLocationService();

		// initiate the request to get the city name based of current latitude and longitude.
		LocationBasedCityName locationBasedCityName = new LocationBasedCityName();
		locationBasedCityName.getAddressFromLocation(newLocation.getLatitude(), newLocation.getLongitude(),
				getActivity(), new GeocoderHandler());
	}



	@Override
	public void onResume() {
		super.onResume();

		// make the device update its location
		mPrayerLocationService.setListener(this);
	}

	@Override
	public void onPause() {
		// stop location updates (saves battery)
		mPrayerLocationService.stopLocationService();
		super.onPause();
	}

	//------ refresh menu item.
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		menu.clear();
		inflater.inflate(R.menu.refresh_menu, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.refreshFragment:
				mPrayTimesProgressBar.setVisibility(View.VISIBLE);
				if(mAdapter!=null)
					mAdapter.clear();
				mTvHijriDate.setText("");
				mTvCityName.setText("");
				setDates();

				// Kicking off the procedure to get the location and then prayer times.
				getCurrentLocation();
				return true;
		}

		return super.onOptionsItemSelected(item);
	}
}
