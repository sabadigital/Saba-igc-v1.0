package com.saba.igc.org.activities;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import com.saba.igc.org.R;
import com.saba.igc.org.application.SabaClient;
import com.saba.igc.org.fragments.CommunityAnnouncementsFragment;
import com.saba.igc.org.fragments.ContactAndDirectionsFragment;
import com.saba.igc.org.fragments.PrayerTimesFragment;
import com.saba.igc.org.fragments.UpcomingProgramsFragment;
import com.saba.igc.org.fragments.WeeklyProgramsFragment;
import com.saba.igc.org.listeners.SabaServerResponseListener;
import com.saba.igc.org.navdrawer.FragmentNavigationDrawer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Syed Aftab Naqvi
 * @create December, 2014
 * @version 1.0
 */
public class MainActivity1 extends FragmentActivity implements SabaServerResponseListener {
	private FragmentNavigationDrawer dlDrawer;
	private final String TAG = "MainActivity1";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main1);
		// Find our drawer view
		dlDrawer = (FragmentNavigationDrawer) findViewById(R.id.drawer_layout);
		SabaClient.getInstance(this).getHijriDate("HijriDate", this);
        ListView lvDrawer = (ListView) findViewById(R.id.lvDrawer);

//        (R.layout.fragment_pray_times, container, false);
//
//        View view = inflater.inflate(R.layout.activity_main, container, false);
//
//        View header = getLayoutInflater().inflate(R.id.tvProgramHeader);
//        lvDrawer.addHeaderView(header);
		// Setup drawer view
		dlDrawer.setupDrawerConfiguration(lvDrawer,
				R.layout.drawer_nav_item, R.id.flContent);


		// Add nav items
		dlDrawer.addNavItem("Weekly Schedule", R.drawable.ic_weekly_chedule1, "Weekly Schedule", WeeklyProgramsFragment.class);
		dlDrawer.addNavItem("Events", R.drawable.ic_events_annucements, "Events & Announcements", UpcomingProgramsFragment.class);
		dlDrawer.addNavItem("Community", R.drawable.ic_community, "Community Announcements", CommunityAnnouncementsFragment.class);
		dlDrawer.addNavItem("Contact and Directions", R.drawable.ic_contact_directions, "Contact and Directions", ContactAndDirectionsFragment.class);
		dlDrawer.addNavItem("Prayer Times", R.drawable.ic_prayer, "Prayer Times", PrayerTimesFragment.class);

		// Select default
		if (savedInstanceState == null) {
			dlDrawer.selectDrawerItem(0);	
		}
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// If the nav drawer is open, hide action items related to the content
		if (dlDrawer.isDrawerOpen()) {
			// Uncomment to hide menu items                        
			// menu.findItem(R.id.mi_test).setVisible(false);
		}
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		// Uncomment to inflate menu items to Action Bar      
		// inflater.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// The action bar home/up action should open or close the drawer.
		// ActionBarDrawerToggle will take care of this.
		if (dlDrawer.getDrawerToggle().onOptionsItemSelected(item)) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		dlDrawer.getDrawerToggle().syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggles
		dlDrawer.getDrawerToggle().onConfigurationChanged(newConfig);
	}

	@Override
	public void processJsonObject(String programName, JSONObject response) {
		Log.d(TAG, "HijriDate: JSON response." + response);
		if(response == null)
			return;

		try{
			String hijriDate = response.getString("hijridate");
			if(hijriDate != null){
				Log.d(TAG, "HijriDate: " + hijriDate);
				SabaClient.getInstance(this).saveHijriDate(hijriDate);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void processJsonObject(String programName, JSONArray response) {

	}


	// -------------------------- below code was used to generate Database with prayer times ----------------
	
//	populateDB("Gilroy", R.raw.gilroy_islamiccal_2009);
//	populateDB("Monterey", R.raw.monterey_islamiccal_2009);
//	populateDB("Oakland", R.raw.oakland_islamiccal_2009);
//	populateDB("Sacramento", R.raw.sacramento_islamiccal_2009);
//	populateDB("San Francisco", R.raw.sanfrancisco_islamiccal_2009);
//	populateDB("San Jose", R.raw.sanjose_islamiccal_2009);
//	populateDB("Santa Cruz", R.raw.santacruz_islamiccal_2009);
//	populateDB("Santa Rosa", R.raw.santarosa_islamiccal_2009);
//	populateDB("Stockton", R.raw.stockton_islamiccal_2009);
	
// this method was used to generate the DB from prayer text files
//	private void populateDB(String city, int idRes){
//	    try {
//	    	
//	    	InputStream inputStream = getResources().openRawResource(idRes);
//		    InputStreamReader inputreader = new InputStreamReader(inputStream);
//		    BufferedReader buffreader = new BufferedReader(inputreader);
//		    String line;
//	        while (( line = buffreader.readLine()) != null) {
//	        	PrayerTimes time = PrayerTimes.fromString(city, line);
//	        	time.saveTime();          
//	        }
//	    	buffreader.close();
//	        inputreader.close();
//	        inputStream.close();
//	        
//	    } catch (Exception e) {
//	    	e.printStackTrace();
//	        return;
//	    }
//	}
}