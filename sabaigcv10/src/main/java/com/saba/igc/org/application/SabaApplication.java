package com.saba.igc.org.application;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Configuration;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.saba.igc.org.extras.SimpleLocation;
import com.saba.igc.org.listeners.SabaServerResponseListener;
import com.saba.igc.org.models.DailyProgram;
import com.saba.igc.org.models.PrayerTimes;
import com.saba.igc.org.models.SabaProgram;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Syed Aftab Naqvi
 * @create December, 2014
 * @version 1.0
 */
public class SabaApplication extends Application {
	private static Context mContext;
	private static SimpleLocation mLocation;
	private String TAG = SabaApplication.class.toString();

	/**
	 * The Analytics singleton. The field is set in onCreate method override when the application
	 * class is initially created.
	 */
	private static GoogleAnalytics mAnalytics;

	/**
	 * The default app tracker. The field is from onCreate callback when the application is
	 * initially created.
	 */
	private static Tracker mTracker;

	/**
	 * Access to the global Analytics singleton. If this method returns null you forgot to either
	 * set android:name="&lt;this.class.name&gt;" attribute on your application element in
	 * AndroidManifest.xml or you are not setting this.analytics field in onCreate method override.
	 */
	public static GoogleAnalytics analytics() {
		return mAnalytics;
	}

	/**
	 * The default app tracker. If this method returns null you forgot to either set
	 * android:name="&lt;this.class.name&gt;" attribute on your application element in
	 * AndroidManifest.xml or you are not setting this.tracker field in onCreate method override.
	 */
	public static Tracker tracker() {
		return mTracker;
	}


//
//	public enum AppStartType {
//	    FIRST_TIME, 
//	    FIRST_TIME_VERSION, 
//	    NORMAL;
//	}
//
//	private SharedPreferences sharedPreferences;
//	private static final String LAST_APP_VERSION = "last_app_version";
//	private static AppStartType appStart = null;
//	public AppStartType checkAppStart() {
//		
//	    if (appStart == null) {
//	        PackageInfo pInfo;
//	        try {
//	            pInfo = context.getPackageManager().getPackageInfo(
//	                    context.getPackageName(), 0);
//	            int lastVersionCode = sharedPreferences.getInt(
//	                    LAST_APP_VERSION, -1);
//	            // String versionName = pInfo.versionName;
//	            int currentVersionCode = pInfo.versionCode;
//	            appStart = checkAppStart(currentVersionCode, lastVersionCode);
//	            // Update version in preferences
//	            sharedPreferences.edit()
//	                    .putInt(LAST_APP_VERSION, currentVersionCode).commit();
//	        } catch (NameNotFoundException e) {
//	            Log.w("AppStartType: ",
//	                    "Unable to determine current app version from pacakge manager. Assuming normal app start.");
//	        }
//	    }
//	    return appStart;
//	}
//
//	public AppStartType checkAppStart(int currentVersionCode, int lastVersionCode) {
//	    if (lastVersionCode == -1) {
//	        return AppStartType.FIRST_TIME;
//	    } else if (lastVersionCode < currentVersionCode) {
//	        return AppStartType.FIRST_TIME_VERSION;
//	    } else if (lastVersionCode > currentVersionCode) {
//	        Log.w("AppStartType: ", "Current version code (" + currentVersionCode
//	                + ") is less then the one recognized on last startup ("
//	                + lastVersionCode
//	                + "). Assuming normal app start.");
//	        return AppStartType.NORMAL;
//	    } else {
//	        return AppStartType.NORMAL;
//	    }
//	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		SabaApplication.mContext = this;

	    Configuration.Builder config1 = new Configuration.Builder(this);
	    config1.addModelClasses(PrayerTimes.class, SabaProgram.class, DailyProgram.class);
	    ActiveAndroid.initialize(config1.create());

		// Create global configuration and initialize ImageLoader with this configuration
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().
				cacheInMemory().cacheOnDisc().build();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
		.defaultDisplayImageOptions(defaultOptions)
		.build();
		ImageLoader.getInstance().init(config);

		getSabaClient().getHijriDate("hijriDate", new SabaServerResponseListener() {
			@Override
			public void processJsonObject(String programName, JSONObject response) {
				if(response == null){
					Log.d(TAG, "json object is null for :" + programName);
					return;
				}

				if(programName.compareToIgnoreCase("hijriDate") == 0){
					try{
						String hijriDate = response.getString("hijridate");
						if(hijriDate != null){
							Log.d(TAG, "HijriDate: " + hijriDate);
							getSabaClient().saveHijriDate(hijriDate);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}

			@Override
			public void processJsonObject(String programName, JSONArray response) {

			}
		});
		initializeGoogleAnalytics();
	}

	@Override
    public void onTerminate () {
        super.onTerminate ();
        ActiveAndroid.dispose ();
    }
	
	public static SabaClient getSabaClient() {
		return SabaClient.getInstance(SabaApplication.mContext);
	}

	private void initializeGoogleAnalytics(){
		//mAnalytics = GoogleAnalytics.getInstance(this);
		//mAnalytics.setLocalDispatchPeriod(120);

		//https://www.google.com/analytics/web/
		//mTracker = mAnalytics.newTracker("UA-65121409-6");
	}

	// this function sends tracking events for Analytics.
	public static void sendAnalyticsEvent(String screenName, String eventCategory, String eventAction, String eventLabel){
		//mTracker.setScreenName(screenName);
		//mTracker.send(new HitBuilders.EventBuilder()
		//		.setCategory(eventCategory)
		//		.setAction(eventAction)
		//		.setLabel(eventLabel)
		//		.build());
	}

	public static void sendAnalyticsScreenName(String screenName){
		// Set screen name.
		//mTracker.setScreenName(screenName);
		// Send a screen view.
		//mTracker.send(new HitBuilders.ScreenViewBuilder().build());
	}
}