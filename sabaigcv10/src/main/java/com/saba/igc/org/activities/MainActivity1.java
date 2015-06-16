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

import com.saba.igc.org.R;
import com.saba.igc.org.application.SabaClient;
import com.saba.igc.org.fragments.CommunityAnnouncementsFragment;
import com.saba.igc.org.fragments.ContactAndDirectionsFragment;
import com.saba.igc.org.fragments.PrayerTimesFragment;
import com.saba.igc.org.fragments.UpcomingProgramsFragment;
import com.saba.igc.org.fragments.WeeklyProgramsFragment;
import com.saba.igc.org.listeners.SabaServerResponseListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Syed Aftab Naqvi
 * @create December, 2014
 * @version 1.0
 */
/*public class MainActivity1 extends FragmentActivity implements SabaServerResponseListener {
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

		// Populate the Navigtion Drawer with options
		RelativeLayout drawerPane = (RelativeLayout)findViewById(R.id.drawerPane);

		// Setup drawer view
		dlDrawer.setupDrawerConfiguration(lvDrawer,
				R.layout.drawer_nav_item, R.id.mainContent, drawerPane);

		// Add nav items
		dlDrawer.addNavItem("Weekly Schedule", R.drawable.ic_weekly_schedule, "Weekly Schedule", WeeklyProgramsFragment.class);
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
		inflater.inflate(R.menu.prayer_times_fragment_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// The action bar home/up action should open or close the drawer.
		// ActionBarDrawerToggle will take care of this.
		if (dlDrawer.getDrawerToggle().onOptionsItemSelected(item)) {
			return true;
		}

		switch (item.getItemId()) {
			// action with ID action_refresh was selected
			case R.id.refreshFragment:
				break;
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

//	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
//		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
//				bitmap.getHeight(), Config.ARGB_8888);
//		Canvas canvas = new Canvas(output);
//
//		final int color = 0xff424242;
//		final Paint paint = new Paint();
//		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
//		final RectF rectF = new RectF(rect);
//		final float roundPx = 12;
//
//		paint.setAntiAlias(true);
//		canvas.drawARGB(0, 0, 0, 0);
//		paint.setColor(color);
//		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
//
//		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
//		canvas.drawBitmap(bitmap, rect, rect, paint);
//
//		return output;
//	}

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
}*/

public class MainActivity1 extends AppCompatActivity implements SabaServerResponseListener {
	private DrawerLayout mDrawer;
	private Toolbar toolbar;
	private NavigationView mNvDrawer;
	private ActionBarDrawerToggle mDrawerToggle;
	private final String TAG = "MainActivity1";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main1);

		// Set a Toolbar to replace the ActionBar.
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		// Find our drawer view
		mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

		mDrawerToggle = setupDrawerToggle();

		// Tie DrawerLayout events to the ActionBarToggle
		mDrawer.setDrawerListener(mDrawerToggle);

		// Set the menu icon instead of the launcher icon.
		final ActionBar actionBar = getSupportActionBar();
		if(actionBar != null) {
			actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_36dp);
			actionBar.setDisplayHomeAsUpEnabled(true);
		}

		// Find our drawer view
		mNvDrawer = (NavigationView) findViewById(R.id.nvView);
		// Setup drawer view
		setupDrawerContent(mNvDrawer);

		// setting WeeklyPrograms displayed.
		Fragment fragment = null;
		try {
			fragment = (Fragment) WeeklyProgramsFragment.class.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Insert the fragment by replacing any existing fragment
		FragmentManager fragmentManager = getSupportFragmentManager();
		fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
	}

	private ActionBarDrawerToggle setupDrawerToggle() {
		return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open,  R.string.drawer_close);
	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		MenuInflater inflater = getMenuInflater();
//		// Uncomment to inflate menu items to Action Bar
//		inflater.inflate(R.menu.refresh_menu, menu);
//		return super.onCreateOptionsMenu(menu);
//	}

//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		// The action bar home/up action should open or close the drawer.
//		switch (item.getItemId()) {
//			case android.R.id.home:
//				mDrawer.openDrawer(GravityCompat.START);
//				return true;
//		}
//
//		return super.onOptionsItemSelected(item);
//	}

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
			case R.id.nav_weekly_schedule_fragment:
				fragmentClass = WeeklyProgramsFragment.class;
				break;
			case R.id.nav_upcoming_programs_fragment:
				fragmentClass = UpcomingProgramsFragment.class;
				break;
			case R.id.nav_community_announcements:
				fragmentClass = CommunityAnnouncementsFragment.class;
				break;
			case R.id.nav_contact_directions_fragment:
				fragmentClass = ContactAndDirectionsFragment.class;
				break;
			case R.id.nav_prayer_times_fragment:
				fragmentClass = PrayerTimesFragment.class;
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
		fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

		// Highlight the selected item, update the title, and close the drawer
		menuItem.setChecked(true);
		setTitle(menuItem.getTitle());
		mDrawer.closeDrawers();
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
}