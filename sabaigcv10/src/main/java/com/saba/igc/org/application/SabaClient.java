package com.saba.igc.org.application;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.saba.igc.org.listeners.SabaServerResponseListener;

/**
 * @author Syed Aftab Naqvi
 * @create December, 2014
 * @version 1.0
 */
public class SabaClient {
	private static SabaClient sabaClient;
	private static Context mContext;
//	private SabaServerResponseListener mTarget;
	private static final String SABA_BASE_URL = "http://www.saba-igc.org/mobileapp/datafeedproxy.php?sheetName=weekly&sheetId=";
	private static String PRAY_TIME_INFO_URL = "http://praytime.info/getprayertimes.php?lat=34.024899&lon=-117.89730099999997&gmt=-480&m=11&d=31&y=2014&school=0";
	private static String PRAY_TIME_INFO_BASE_URL = "http://praytime.info/getprayertimes.php?school=0&gmt=-480";
	private static final int TIME_OUT = 30000;
	
//	private class ReadFromDatabase extends AsyncTask<String, Void, List<SabaProgram> > {
//		@Override
//		protected List<SabaProgram> doInBackground(String... programName) {
//	        return SabaProgram.getSabaPrograms(programName[0]);
//	    }
//
//		@Override
//	    protected void onPostExecute(List<SabaProgram> result) {
//			if(result != null){
//				System.out.println(result.get(0).getTitle());
//				mTarget.getPrograms(null, result);
//			}
//	    }
//	}
//	
	/**
	 * @param context
	 * @return
	 */
	public synchronized static SabaClient getInstance(Context context) {
	   if(sabaClient == null) {
		   mContext = context;
		   sabaClient = new SabaClient();
	   }

	   return sabaClient;
	}
	
	public void getUpcomingPrograms(final SabaServerResponseListener targert){
		
		// check the database, if lastUpdate was recent? 
		// sheet # 2 is Upcoming programs
		sendRequest("Upcoming Programs", SABA_BASE_URL+2, targert);
 	}
	
	public void getWeeklyPrograms(final SabaServerResponseListener targert){
		// sheet # 4 is Weekly Announcements
		sendRequest("Weekly Programs", SABA_BASE_URL+4, targert);

	}
	
	public void getCommunityAnnouncements(final SabaServerResponseListener targert){
		// sheet # 5 is Community Announcements
		sendRequest("Community Announcements", SABA_BASE_URL+5, targert);
	}
	
	public void getGeneralAnnouncements(final SabaServerResponseListener targert){
		// sheet # 6 is General Announcements
		sendRequest("General Announcements", SABA_BASE_URL+6, targert);
	}
	
	private void sendRequest(final String programName, final String url, final SabaServerResponseListener targert){
		// create the network client
    	AsyncHttpClient client = new AsyncHttpClient();
    	
    	client.setTimeout(TIME_OUT);
    	
    	// trigger the network request
    	client.get(url, new JsonHttpResponseHandler(){
			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				super.onFailure(statusCode, headers, throwable, errorResponse);
				throwable.printStackTrace();
				targert.processJsonObject(programName, errorResponse);
			}

//			@Override
//			public void onFailure(int statusCode, Header[] headers,
//					String responseString, Throwable throwable) {
//				// TODO Auto-generated method stub
//				super.onFailure(statusCode, headers, responseString, throwable);
//			}
//
//			@Override
//			public void onFailure(int statusCode, Header[] headers,
//					Throwable throwable, JSONArray errorResponse) {
//				// TODO Auto-generated method stub
//				
//				
//				int i = 0;
//				i++;
//				super.onFailure(statusCode, headers, throwable, errorResponse);
//			}

//			@Override
//			public void onSuccess(int statusCode, Header[] headers,
//					String responseString) {
//				// TODO Auto-generated method stub
//				super.onSuccess(statusCode, headers, responseString);
//			}
//
//			@Override
//			protected Object parseResponse(byte[] responseBody)
//					throws JSONException {
//				// TODO Auto-generated method stub
//				return super.parseResponse(responseBody);
//			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONArray response) {
				super.onSuccess(statusCode, headers, response);
				targert.processJsonObject(programName, response);
			}
			
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				targert.processJsonObject(programName, response);
			}
    	});
	}

	public void getCachedPrograms(String string, SabaServerResponseListener target) {

		//mTarget = target;
		//new ReadFromDatabase().execute(string);
	}
	
	public void getPrayTimes(double longitude, double latitude, SabaServerResponseListener target) {
		StringBuilder sb = new StringBuilder(PRAY_TIME_INFO_BASE_URL);
		
		// setting location
		sb.append("&lat=");
		sb.append(latitude);
		sb.append("&lon=");
		sb.append(longitude);
		
		// setting today's date.
		Calendar now = Calendar.getInstance();
		sb.append("&m=");
		sb.append(now.get(Calendar.MONTH+1)); // month is zero based.
		sb.append("&d=");
		sb.append(now.get(Calendar.DAY_OF_MONTH));
		sb.append("&y=");
		sb.append(now.get(Calendar.YEAR));
		
	    sendRequest("Pray Times", sb.toString(), target);
	}
}