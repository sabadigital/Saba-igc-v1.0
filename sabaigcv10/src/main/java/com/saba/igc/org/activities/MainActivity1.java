package com.saba.igc.org.activities;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.saba.igc.org.R;
import com.saba.igc.org.application.SabaApplication;
import com.saba.igc.org.fragments.CommunityAnnouncementsFragment;
import com.saba.igc.org.fragments.ContactAndDirectionsFragment;
import com.saba.igc.org.fragments.PrayerTimesFragment;
import com.saba.igc.org.fragments.UpcomingProgramsFragment;
import com.saba.igc.org.fragments.WeeklyProgramsFragment;
import com.saba.igc.org.listeners.SabaServerResponseListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.saba.igc.org.R.drawable;
import static com.saba.igc.org.R.id;
import static com.saba.igc.org.R.layout;

/**
 * @author Syed Aftab Naqvi
 * @create December, 2014
 * @version 1.0
 */

public class MainActivity1 extends AppCompatActivity implements SabaServerResponseListener {
	private DrawerLayout 			mDrawer;
	private Toolbar 				mToolbar;
	private TextView 				mTvToolbarTitle;
	private NavigationView 			mNvDrawer;
	private ActionBarDrawerToggle 	mDrawerToggle;
	private final String TAG = 		"MainActivity1";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(layout.activity_main1);

		// Set a Toolbar to replace the ActionBar.
		mToolbar = (Toolbar) findViewById(id.toolbar);
		setSupportActionBar(mToolbar);

		// Find our drawer view
		mDrawer = (DrawerLayout) findViewById(id.drawer_layout);

		mDrawerToggle = setupDrawerToggle();

		// Tie DrawerLayout events to the ActionBarToggle
		mDrawer.setDrawerListener(mDrawerToggle);

		// Set the menu icon instead of the launcher icon.
		final ActionBar actionBar = getSupportActionBar();
		if(actionBar != null) {
			actionBar.setHomeAsUpIndicator(drawable.ic_menu_white_36dp);
			actionBar.setDisplayHomeAsUpEnabled(true);

			// sets toolbar title in center.
			View view = getLayoutInflater().inflate(layout.custom_toolbar_view, null);
			mTvToolbarTitle = (TextView)view.findViewById(id.tvToolbarTitle);
			mToolbar.addView(view);
		}

		// Find our drawer view
		mNvDrawer = (NavigationView) findViewById(id.nvView);

		// Setup drawer view
		setupDrawerContent(mNvDrawer);

		setDates();

		// setting WeeklyPrograms displayed on startup. If we want to display something else on startup, change here.
		Fragment fragment = null;
		try {
			fragment = (Fragment) WeeklyProgramsFragment.class.newInstance();
			mTvToolbarTitle.setText("Weekly Schedule");
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Insert the fragment by replacing any existing fragment
		FragmentManager fragmentManager = getSupportFragmentManager();
		fragmentManager.beginTransaction().replace(id.flContent, fragment).commit();
	}

	private ActionBarDrawerToggle setupDrawerToggle() {
		return new ActionBarDrawerToggle(this, mDrawer, mToolbar, R.string.drawer_open, R.string.drawer_close) {
			public void onDrawerClosed(View view) {
				super.onDrawerClosed(view);
			}

			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				setDates();
			}
		};
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggles
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	private void setupDrawerContent(NavigationView navigationView) {
		navigationView.setNavigationItemSelectedListener(
				new NavigationView.OnNavigationItemSelectedListener() {
					@Override
					public boolean onNavigationItemSelected(MenuItem menuItem) {
						selectDrawerItem(menuItem);
						return true;
					}
				});
	}

	public void selectDrawerItem(MenuItem menuItem) {
		// Create a new fragment and specify the planet to show based on
		// position
		Fragment fragment = null;

		Class fragmentClass;
		switch(menuItem.getItemId()) {
			case id.nav_weekly_schedule_fragment:
				fragmentClass = WeeklyProgramsFragment.class;
				mTvToolbarTitle.setText("Weekly Schedule");
				break;
			case id.nav_upcoming_programs_fragment:
				fragmentClass = UpcomingProgramsFragment.class;
				mTvToolbarTitle.setText("Upcoming Programs");
				break;
			case id.nav_community_announcements:
				fragmentClass = CommunityAnnouncementsFragment.class;
				mTvToolbarTitle.setText("Community Announcements");
				break;
			case id.nav_contact_directions_fragment:
				fragmentClass = ContactAndDirectionsFragment.class;
				mTvToolbarTitle.setText("Contact and Directions");
				break;
			case id.nav_prayer_times_fragment:
				fragmentClass = PrayerTimesFragment.class;
				mTvToolbarTitle.setText("Prayer Times");
				break;
			default:
				fragmentClass = WeeklyProgramsFragment.class;
		}

		try {
			fragment = (Fragment) fragmentClass.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Insert the fragment by replacing any existing fragment
		FragmentManager fragmentManager = getSupportFragmentManager();
		fragmentManager.beginTransaction().replace(id.flContent, fragment).commit();

		// Highlight the selected item, update the title, and close the drawer
		menuItem.setChecked(true);
		setTitle(menuItem.getTitle());
		mDrawer.closeDrawers();
	}

	private void setDates(){
		TextView tvHijriDate = (TextView)mNvDrawer.findViewById(id.tvHijriOnHeader);

		// get Hijri date.
		String hijriDate = SabaApplication.getSabaClient().getHijriDate();
		if(tvHijriDate != null){
			tvHijriDate.setText(hijriDate);
		}

		// try sending a network call to get the hijridate from SABA server.
		if(hijriDate==null || hijriDate.isEmpty()){
			SabaApplication.getSabaClient().getHijriDate("hijriDate", MainActivity1.this);
		}

		TextView tvTodayDate = (TextView)mNvDrawer.findViewById(id.tvEnglishDateOnHeader);
		if(tvTodayDate != null){
			DateFormat dateInstance = SimpleDateFormat.getDateInstance(DateFormat.FULL);
			tvTodayDate.setText(dateInstance.format(Calendar.getInstance().getTime()));
		}
	}

	// SabaServerResponseListener methods
	@Override
	public void processJsonObject(String programName, JSONObject response) {
		Log.d(TAG, "HijriDate: JSON response." + response);
		if(response == null)
			return;

		try{
			String hijriDate = response.getString("hijridate");
			if(hijriDate != null){
				Log.d(TAG, "HijriDate: " + hijriDate);
				SabaApplication.getSabaClient().saveHijriDate(hijriDate);
				setDates();
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void processJsonObject(String programName, JSONArray response) {

	}
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
//}