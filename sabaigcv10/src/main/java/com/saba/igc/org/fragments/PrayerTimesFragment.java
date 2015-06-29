package com.saba.igc.org.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
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
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.saba.igc.org.R;
import com.saba.igc.org.activities.MainActivity;
import com.saba.igc.org.adapters.PrayTimeAdapter;
import com.saba.igc.org.application.SabaApplication;
import com.saba.igc.org.application.SabaClient;
import com.saba.igc.org.extras.LocationBasedCityName;
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
 * December, 2014
 * @version 1.0
 */
public class PrayerTimesFragment extends Fragment implements SabaServerResponseListener,
		ConnectionCallbacks,
		OnConnectionFailedListener, LocationListener{

	private SabaClient			mSabaClient;
	private TextView 			mTvCityName;
	private TextView 			mTvTodayDate;
	private TextView 			mTvHijriDate;
	private List<PrayTime> 		mPrayTimes;
	private ListView	 		mLvPrayTimes;
	private PrayTimeAdapter 	mAdapter;
	private ProgressBar 		mPrayTimesProgressBar;
	private boolean				mPrayerTimesFromWebInProgress;

	// ======= Google Play Services..
	private static final String TAG = MainActivity.class.getSimpleName();
	private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
	private Location mLastLocation;
	private boolean	 mAlreadyGotLocationUpdate = false;

	// Google client to interact with Google API
	private GoogleApiClient mGoogleApiClient;
	private LocationRequest mLocationRequest;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mSabaClient = SabaApplication.getSabaClient();

		// First we need to check availability of play services
		if (checkPlayServices()) {
			// Building the GoogleApi client
			buildGoogleApiClient();
			createLocationRequest();

			if (mGoogleApiClient != null) {
				mGoogleApiClient.connect();
			}
		}

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

	private void setupUI(View view) {
		mTvCityName 			= (TextView) view.findViewById(R.id.tvCityName);
		mTvHijriDate			= (TextView) view.findViewById(R.id.tvHijriDate);
		mTvTodayDate		 	= (TextView) view.findViewById(R.id.tvEnglishDate);
		//mLvPrayTimes 			= (eu.erikw.PullToRefreshListView) view.findViewById(R.id.lvPrayTimes);
		mLvPrayTimes 			= (ListView) view.findViewById(R.id.lvPrayTimes);
		mPrayTimesProgressBar 	= (ProgressBar) view.findViewById(R.id.prayTimesProgressBar);

		if(mPrayTimesProgressBar!=null)
			mPrayTimesProgressBar.getIndeterminateDrawable().setColorFilter(0xFFFFFFFF, android.graphics.PorterDuff.Mode.MULTIPLY);
	}

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
			}
				if(mPrayerTimesFromWebInProgress)
					mPrayTimesProgressBar.setVisibility(View.VISIBLE);

				return; // we should return from here. Processing of hijriDate is done here.
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

	private void clearUIControls(){
		if(mAdapter!=null)
			mAdapter.clear();
		mTvHijriDate.setText("");
		mTvCityName.setText("");
	}

	private void refreshUI(){
		mPrayTimesProgressBar.setVisibility(View.VISIBLE);
		clearUIControls();

		setDates();
		// Kicking off the process to get the location and then prayer times,
		// first we will try Database, if city doesn't exits then we will reach out
		// to web to get the prayer times.
		if (mGoogleApiClient != null) {
			if(!mGoogleApiClient.isConnected()) {
				mGoogleApiClient.connect();
			} else {
				//startLocationUpdates();
				processCurrentLocation();
			}
		}
	}

	private String getCurrentTimezoneOffsetInMinutes() {

		TimeZone tz = TimeZone.getDefault();
		Calendar cal = GregorianCalendar.getInstance(tz);
		int offsetInMillis = tz.getOffset(cal.getTimeInMillis());

		return String.format("%d", offsetInMillis/(1000*60)); // in minutes
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

	public void showAlert(){
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

		// Setting Dialog Title
		alertDialog.setTitle("Geocoder is not responding in timely fashion.");

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

	public void processCurrentLocation(){
//		LocationAvailability la = LocationServices.FusedLocationApi.getLocationAvailability(mGoogleApiClient);
//		if (la != null && la.isLocationAvailable()) {
//			Log.d(TAG, "**** Location Available ****");
//		}
		// Gets the best and most recent location currently available,
		// which may be null in rare cases when a location is not available.
		mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
				mGoogleApiClient);

		if (mLastLocation == null) {
			showSettingsAlert();
			mPrayTimesProgressBar.setVisibility(View.GONE);
		} else {
			// Determine whether a Geocoder is available.
			if (!Geocoder.isPresent()) {
				Toast.makeText(getActivity(), "Sorry, Geocoder is not available.", Toast.LENGTH_LONG).show();
				return;
			}

			// initiate the request to get the city name based of current latitude and longitude.
			LocationBasedCityName locationBasedCityName = new LocationBasedCityName();
			locationBasedCityName.getAddressFromLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude(),
					getActivity(), new GeocoderHandler());
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
			mPrayTimes = new ArrayList<>(prayerTimes.size());
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
			// Once, we will get the date, pasrse it and display on UI.

			//we have the city but we want now to get the prayer times only.
			//We are going to send network request yo get prayerInfo.
			mSabaClient.getPrayTimes(getCurrentTimezoneOffsetInMinutes(), mLastLocation.getLatitude(), mLastLocation.getLongitude(), this);
		}
	}

	/**
	 * Wrapper method to get the prayerTimes from PrayerTimes Model.
	 *
	 * */
	public List<PrayerTimes> getTodayPrayerTimesFromDB(String city){
		// check database for this city and it it exists then we will get the prayertimes from our database.
		// otherwise get the prayertimes from praytime.info.
		StringBuilder sb;
		sb = new StringBuilder();
		sb.append(Calendar.getInstance().get(Calendar.MONTH)); // month is zero based.
		sb.append("-");
		sb.append(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

		return PrayerTimes.getTodayPrayerTimes(city, sb.toString());
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
				refreshUI();
				return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onStop() {
		super.onStop();
		if (mGoogleApiClient.isConnected()) {
			mGoogleApiClient.disconnect();
		}
	}

	@Override
	public void onPause() {
		// stop location updates (saves battery)
		super.onPause();
//		stopLocationUpdates();
	}

	/**
	 * Creating google api client object
	 * */
	protected synchronized void buildGoogleApiClient() {
		mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this)
				.addApi(LocationServices.API).build();
	}

	/**
	 * Creating location request object
	 * */
	protected void createLocationRequest() {
		// Location updates intervals in sec
		final int UPDATE_INTERVAL = 5000; // 5 sec
		final int FATEST_INTERVAL = 3000; // 3 sec
		final int DISPLACEMENT = 1; // 1 meters

		mLocationRequest = new LocationRequest();
		mLocationRequest.setInterval(UPDATE_INTERVAL);
		mLocationRequest.setFastestInterval(FATEST_INTERVAL);
		mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
	}

	/**
	 * Method to verify google play services on the device
	 * */
	private boolean checkPlayServices() {
		int resultCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(getActivity());
		if (resultCode != ConnectionResult.SUCCESS) {
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
				GooglePlayServicesUtil.getErrorDialog(resultCode, getActivity(),
						PLAY_SERVICES_RESOLUTION_REQUEST).show();
			} else {
				Toast.makeText(getActivity(),
						"This device is not supported.", Toast.LENGTH_LONG)
						.show();
				//finish();
				// lets find out what needs to be done.
			}
			return false;
		}
		return true;
	}

	/**
	 * Google api callback methods
	 */
	@Override
	public void onConnectionFailed(ConnectionResult result) {
		Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = "
				+ result.getErrorCode());
	}

	@Override
	public void onConnected(Bundle arg0) {
		//startLocationUpdates();
		processCurrentLocation();
	}

	@Override
	public void onConnectionSuspended(int arg0) {
		mGoogleApiClient.connect();
	}

	/**
	 * Starting the location updates
	 * */
	protected void startLocationUpdates() {
		mAlreadyGotLocationUpdate = false;
		LocationServices.FusedLocationApi.requestLocationUpdates(
				mGoogleApiClient, mLocationRequest, this);
	}

	/**
	 * Stopping location updates
	 */
	protected void stopLocationUpdates() {
		LocationServices.FusedLocationApi.removeLocationUpdates(
				mGoogleApiClient, this);
	}

	/*
	*
	*
	* */
	@Override
	public void onLocationChanged(Location location) {
		if(mAlreadyGotLocationUpdate){
			return;
		}

		mAlreadyGotLocationUpdate = true;
		// Assign the new location
		mLastLocation = location;
		Toast.makeText(getActivity(), "Location changed!",
				Toast.LENGTH_SHORT).show();
		stopLocationUpdates();
		processCurrentLocation();
	}

	// show settings Alert
	/**
	 * Function to show settings alert dialog
	 * On pressing Settings button will lauch Settings Options
	 * */
	public void showSettingsAlert(){
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

		// Setting Dialog Title
		alertDialog.setTitle("Location Settings");

		// Setting Dialog Message
		alertDialog.setMessage("Location is NOT enabled. Do you want to go to settings menu?");

		// On pressing Settings button
		alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int which) {
				Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				getActivity().startActivity(intent);
			}
		});

		// on pressing cancel button
		alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});

		// Showing Alert Message
		alertDialog.show();
	}
}
