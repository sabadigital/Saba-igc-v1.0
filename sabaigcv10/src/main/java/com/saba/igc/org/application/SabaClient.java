package com.saba.igc.org.application;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.saba.igc.org.listeners.SabaServerResponseListener;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Syed Aftab Naqvi
 * @create December, 2014
 * @version 1.0
 */
public class SabaClient {
	private static volatile SabaClient 	mSabaClient;
	private static Context 				mContext;
	private static Map<String, SabaServerResponseListener> mMap;

	private static final String SABA_BASE_URL = "http://www.saba-igc.org/mobileapp/datafeedproxy.php?sheetName=weekly&sheetId=";
	private static final int TIME_OUT = 30000;

	private SabaClient(){

	}
	/**
	 * @param context Application Context
	 * @return SabaClient
	 */
	public static SabaClient getInstance(Context context) {
	   if(mSabaClient == null) {
		   synchronized(SabaClient.class) {
			   if(mSabaClient == null) {
				   mSabaClient = new SabaClient();
				   mSabaClient.mMap = new HashMap<String, SabaServerResponseListener>();
				   mSabaClient.mContext = context;
			   }
		   }
	   }

	   return mSabaClient;
	}

	public boolean isInProgress(){
		return mMap.size()!=0;
	}

	public void removeTarget(String program, SabaServerResponseListener target){
		if(mMap.containsKey(program)){
			mMap.remove(program);
		}
	}
	public void getUpcomingPrograms(final SabaServerResponseListener target){
		// check the database, if lastUpdate was recent? 
		// sheet # 2 is Upcoming programs
		sendRequest("Announcements", SABA_BASE_URL+2, target);
 	}
	
	public void getWeeklyPrograms(final SabaServerResponseListener target){
		// sheet # 4 is Weekly Announcements
		sendRequest("Weekly Programs", SABA_BASE_URL+4, target);

	}
	
	public void getCommunityAnnouncements(final SabaServerResponseListener target){
		// sheet # 5 is Community Announcements
		sendRequest("Community", SABA_BASE_URL+5, target);
	}
	
	public void getGeneralAnnouncements(final SabaServerResponseListener target){
		// sheet # 6 is General Announcements
		sendRequest("General Announcements", SABA_BASE_URL + 6, target);
	}
	
	private void sendRequest(final String programName, final String url, final SabaServerResponseListener target){
		//\Log.d("SabaClient", "********* sending request out for : " + programName);
		mMap.put(programName, target);

		// create the network client
    	AsyncHttpClient client = new AsyncHttpClient();
		client.setTimeout(TIME_OUT);

		// trigger the network request
		client.get(url, new JsonHttpResponseHandler() {
//			@Override
//			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
//				super.onFailure(statusCode, headers, throwable, errorResponse);
//			}
//
//			@Override
//			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//				super.onFailure(statusCode, headers, responseString, throwable);
//			}
//
//			@Override
//			public void onSuccess(int statusCode, Header[] headers, String responseString) {
//				super.onSuccess(statusCode, headers, responseString);
//			}
//
//			@Override
//			protected Object parseResponse(byte[] responseBody) throws JSONException {
//				return super.parseResponse(responseBody);
//			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
								  Throwable throwable, JSONObject errorResponse) {
				super.onFailure(statusCode, headers, throwable, errorResponse);
				throwable.printStackTrace();

				SabaServerResponseListener listener = mMap.get(programName);
				if(listener != null) {
					target.processJsonObject(programName, errorResponse);
					removeTarget(programName, listener);
				}
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
								  JSONArray response) {
				super.onSuccess(statusCode, headers, response);
				SabaServerResponseListener listener = mMap.get(programName);
				if(listener != null) {
					listener.processJsonObject(programName, response);
					removeTarget(programName, listener);
				}
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
								  JSONObject response) {
				super.onSuccess(statusCode, headers, response);

				SabaServerResponseListener listener = mMap.get(programName);
				if(listener != null) {
					target.processJsonObject(programName, response);
					removeTarget(programName, listener);
				}
			}
		});
	}

	public void getHijriDate(String hijriDate, SabaServerResponseListener target) {
		//String HIJRI_DATE_URL = "http://www.saba-igc.org/prayerTimes/salatDataService/salatDataService.php";
		sendRequest(hijriDate, "http://www.saba-igc.org/prayerTimes/salatDataService/salatDataService.php", target);
	}

	public void getPrayerTimeFromSaba(String prayTimesFromSaba, SabaServerResponseListener target){
		sendRequest(prayTimesFromSaba, "http://www.saba-igc.org/prayerTimes/salatDataService/salatDataService.php", target);
	}

	public void getPrayTimes(String timeZoneOffsetInMinutes, double latitude, double longitude, SabaServerResponseListener target) {
		//String PRAY_TIME_INFO_BASE_URL = "http://praytime.info/getprayertimes.php?school=0&gmt=";
		StringBuilder sb = new StringBuilder("http://praytime.info/getprayertimes.php?school=0&gmt=");

		sb.append(timeZoneOffsetInMinutes); // appending timeZoneOffsetInMinutes.

		// setting location
		sb.append("&lat=");
		sb.append(latitude);
		sb.append("&lon=");
		sb.append(longitude);

		// setting today's date.
		sb.append("&m=");
		sb.append(Calendar.getInstance().get(Calendar.MONTH) + 1); // month is zero based.
		sb.append("&d=");
		sb.append(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
		sb.append("&y=");
		sb.append(Calendar.getInstance().get(Calendar.YEAR));

		Log.d("PrayerTime URL: ", sb.toString());

		sendRequest("Prayer Times", sb.toString(), target);
	}
	public void getSabaYoutubeChannelVideos(SabaServerResponseListener target){
		sendRequest("SabaChannels", "https://www.googleapis.com/youtube/v3/search?part=snippet&q=wheelockcollege&type=sabavideos&key=AIzaSyCqg8SNUDh8GwRqBwdGMj_5aVBTjeaTmZw", target);
	}

	public void getLiveStreamFeeds(SabaServerResponseListener target){
		sendRequest("Live Stream Feeds", "http://www.saba-igc.org/liveStream/liveStreamLinkApp.php", target);
	}

// Need to find a better place for following code.
	private void savePreferences(String key, String value) {
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(mContext);
		Editor editor = sharedPreferences.edit();
		editor.putString(key, value);
		editor.apply();
	}

	private String getSavedPreferences(String key) {
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(mContext);
		return sharedPreferences.getString(key, "");
	}

	public String getHijriDate(){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		String englishDate = dateFormat.format(calendar.getTime());
		if( englishDate.compareTo(getEnglishDate())==0){
			return getSavedPreferences("hijriDate");
		}

		return "";
	}

	public void saveHijriDate(String hijriDate){
		saveEnglishDate();
		savePreferences("hijriDate", hijriDate);
	}

	public String getEnglishDate(){
		return getSavedPreferences("englishDate");
	}

	public void saveEnglishDate(){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		String englishDate = dateFormat.format(calendar.getTime());
		savePreferences("englishDate", englishDate);
	}
}