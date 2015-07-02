package com.saba.igc.org.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.saba.igc.org.R;
import com.saba.igc.org.activities.DailyProgramDetailActivity;
import com.saba.igc.org.models.DailyProgram;
import com.saba.igc.org.models.SabaProgram;

import org.json.JSONArray;

import java.util.List;
/**
 * @author Syed Aftab Naqvi
 * @create December, 2014
 * @version 1.0
 */
public class WeeklyProgramsFragment extends SabaBaseFragment {
	private final String TAG = "WeeklyProgramsFragment"; 
	private final String PROGRAM_NAME = "Weekly Programs";
	private List<List<DailyProgram>> mWeeklyPrograms;
	
	public WeeklyProgramsFragment(){	
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mProgramName = PROGRAM_NAME;
		// get programs from database. if program exists then display. otherwise make a network request.  
		mPrograms =  SabaProgram.getSabaPrograms(PROGRAM_NAME);
		if(mPrograms !=  null && mPrograms.size() == 0){
			mRefreshInProgress = true;
			// make a network request to pull the data from server.
			mSabaClient.getWeeklyPrograms(this);
		}

		// helps to display menu in fragments.
		setHasOptionsMenu(true);
	}
	
	@Override
	protected void populatePrograms() {
		mSwipeRefreshLayout.setRefreshing(true);
		mAdapter.clear();
		mRefreshInProgress = true;
		mSabaClient.getWeeklyPrograms(this);
	}
	
	// make sure this will be called only in case of WeeklyPrograms.
	@Override
	public void processJsonObject(String programName, JSONArray responseJSONArray){
		mRefreshInProgress = false;
		if (mSwipeRefreshLayout.isRefreshing()) {
			mSwipeRefreshLayout.setRefreshing(false);
		}
		if(responseJSONArray == null){
			Log.d(TAG, "processJsonObject: responseJSONArray is null");
			return;
		}

		mProgramName = programName;
		// parse weekly programs differently....
		mWeeklyPrograms = DailyProgram.fromJSONArray(programName, responseJSONArray);
		mPrograms = SabaProgram.fromWeeklyPrograms(mProgramName, mWeeklyPrograms);
		addAllWeeklyPrograms(mWeeklyPrograms);
		addAll(mPrograms);
		mAdapter.addAll(mPrograms);
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
		// The action bar home/up action should open or close the drawer.
		switch (item.getItemId()) {
			case R.id.refreshFragment:
				if(mRefreshInProgress)
					return true;


				populatePrograms();
				return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void processOnItemClick(int position){
		Intent intent = new Intent(getActivity(), DailyProgramDetailActivity.class);
		String day = null;
		if(mWeeklyPrograms != null){
			day = mWeeklyPrograms.get(position).get(0).getDay();
		} else {
			String data = mPrograms.get(position).getTitle(); 
			int index = data.indexOf(','); // finding the index of ','
			day = data.substring(0, index); // extract the day. e.g. Thursday
		}
		intent.putExtra("day", day);
		intent.putExtra("header", mPrograms.get(position).getTitle());
		startActivity(intent);
	}
}


