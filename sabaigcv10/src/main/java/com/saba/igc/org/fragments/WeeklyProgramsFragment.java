package com.saba.igc.org.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.saba.igc.org.activities.DailyProgramDetailActivity;
import com.saba.igc.org.models.DailyProgram;
import com.saba.igc.org.models.SabaProgram;

import org.json.JSONArray;

import java.io.File;
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
		// get programs from database. if program exists then display. otherwise make a network request.  
		mPrograms =  SabaProgram.getSabaPrograms(PROGRAM_NAME);
		if(mPrograms !=  null && mPrograms.size() == 0){
			// make a network request to pull the data from server.
			mSabaClient.getWeeklyPrograms(this);
		} else {
			mProgramName = PROGRAM_NAME;
		}
	}
	
	@Override
	protected void populatePrograms() {
		// TODO Auto-generated method stub
		mAdapter.clear();
		mSabaClient.getWeeklyPrograms(this);
	}
	
	// make sure this will be called only in case of WeeklyPrograms.
	@Override
	public void processJsonObject(String programName, JSONArray responseJSONArray){
		mProgramsProgressBar.setVisibility(View.GONE);
		mLvPrograms.onRefreshComplete();
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
	}
	
	@Override
	protected void processOnItemClick(int position){
		Intent intent = new Intent(getActivity(), DailyProgramDetailActivity.class);
		String day = null;
		if(mWeeklyPrograms != null){
			day = mWeeklyPrograms.get(position).get(0).getDay();
		} else {
			String data = mPrograms.get(position).getTitle(); 
			int index = data.indexOf(File.separator); // finding the index of '/'
			day = data.substring(0, index); // extract the day. e.g. Thursday
		}
		intent.putExtra("day", day);
		intent.putExtra("header", mPrograms.get(position).getTitle());
		startActivity(intent);
	}
}
