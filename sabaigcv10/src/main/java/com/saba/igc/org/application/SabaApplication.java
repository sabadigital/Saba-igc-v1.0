package com.saba.igc.org.application;

import android.app.Application;
import android.content.Context;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Configuration;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.saba.igc.org.extras.SimpleLocation;
import com.saba.igc.org.models.DailyProgram;
import com.saba.igc.org.models.PrayerTimes;
import com.saba.igc.org.models.SabaDatabaseHelper;
import com.saba.igc.org.models.SabaProgram;

/**
 * @author Syed Aftab Naqvi
 * @create December, 2014
 * @version 1.0
 */
public class SabaApplication extends Application {
	private static Context mContext;
	private static SimpleLocation mLocation;
	
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
	    config1.addModelClasses(PrayerTimes.class);
	    config1.addModelClasses(SabaProgram.class);
	    config1.addModelClasses(DailyProgram.class);
	    ActiveAndroid.initialize(config1.create());
	    
	    
		// Create global configuration and initialize ImageLoader with this configuration
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().
				cacheInMemory().cacheOnDisc().build();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
		.defaultDisplayImageOptions(defaultOptions)
		.build();
		ImageLoader.getInstance().init(config);
		
		//mLocation = new SimpleLocation(mContext, true, true, 60000);
		//mLocation.beginUpdates();
		
		//createDB();
	}

	@Override
    public void onTerminate () {
        super.onTerminate ();
        ActiveAndroid.dispose ();
    }
	
	public static SabaClient getSabaClient() {
		return (SabaClient) SabaClient.getInstance(SabaApplication.mContext);
	}
	
	public static SimpleLocation getLocation(){
		return mLocation;
	}
	private static void createDB(){
		SabaDatabaseHelper sdh = new SabaDatabaseHelper(SabaApplication.mContext);
		sdh.createDatabase();
	}
}