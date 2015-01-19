package com.saba.igc.org.activities;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import com.saba.igc.org.R;
import com.saba.igc.org.fragments.CommunityAnnouncementsFragment;
import com.saba.igc.org.fragments.GeneralAnnouncementsFragment;
import com.saba.igc.org.fragments.PrayerTimesFragment;
import com.saba.igc.org.fragments.UpcomingProgramsFragment;
import com.saba.igc.org.fragments.WeeklyProgramsFragment;
import com.saba.igc.org.navdrawer.FragmentNavigationDrawer;

/**
 * @author Syed Aftab Naqvi
 * @create December, 2014
 * @version 1.0
 */
public class MainActivity1 extends FragmentActivity {
	private FragmentNavigationDrawer dlDrawer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main1);
		// Find our drawer view
		dlDrawer = (FragmentNavigationDrawer) findViewById(R.id.drawer_layout);
		// Setup drawer view
		dlDrawer.setupDrawerConfiguration((ListView) findViewById(R.id.lvDrawer), 
                     R.layout.drawer_nav_item, R.id.flContent);
		
		// Add nav items
		dlDrawer.addNavItem("Upcoming", R.drawable.ic_one, "Upcoming Programs", UpcomingProgramsFragment.class);
		dlDrawer.addNavItem("Weekly", R.drawable.ic_two, "Weekly Programs", WeeklyProgramsFragment.class);
		dlDrawer.addNavItem("Community", R.drawable.ic_two, "Community Announcements", CommunityAnnouncementsFragment.class);
		dlDrawer.addNavItem("General", R.drawable.ic_two, "General Announcements", GeneralAnnouncementsFragment.class);
		dlDrawer.addNavItem("Pray Times", R.drawable.ic_pray, "Pray Times", PrayerTimesFragment.class);

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