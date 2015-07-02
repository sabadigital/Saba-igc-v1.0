package com.saba.igc.org.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.saba.igc.org.R;
import com.saba.igc.org.adapters.ProgramsArrayAdapter;
import com.saba.igc.org.application.SabaApplication;
import com.saba.igc.org.application.SabaClient;
import com.saba.igc.org.listeners.SabaServerResponseListener;
import com.saba.igc.org.models.DailyProgram;
import com.saba.igc.org.models.SabaProgram;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
/**
 * @author Syed Aftab Naqvi
 * @create December, 2014
 * @version 1.0
 */
public abstract class SabaBaseFragment extends Fragment implements SabaServerResponseListener {

	private   String				TAG					= "SabaBaseFragment";
	protected SabaClient 			mSabaClient 		= null;
	protected ProgramsArrayAdapter 	mAdapter 			= null;
	protected List<SabaProgram> 	mPrograms 			= null;
	protected ListView 				mLvPrograms 		= null;
	//protected ProgressBar 		mProgramsProgressBar;
	protected String 				mProgramName 		= null;
	protected SwipeRefreshLayout 	mSwipeRefreshLayout	= null;
	protected boolean				mRefreshInProgress	= false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mSabaClient = SabaApplication.getSabaClient();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		getActivity().setTitle("");// Need this to make it little compatible with API 16. might work for API 14 as well.
		View view = inflater.inflate(R.layout.activity_main, container, false);
		
        mLvPrograms = (ListView) view.findViewById(R.id.lvUpcomingPrograms);
        //mTvLastRrefreshedValue = (TextView) view.findViewById(R.id.tvLastRrefreshedValue);
        
        if(mPrograms != null && mPrograms.size() == 0) {
			mPrograms = new ArrayList<SabaProgram>();
		}

		mAdapter = new ProgramsArrayAdapter(getActivity(), mPrograms);
		mLvPrograms.setAdapter(mAdapter);
		
		//OnItemClickListener
		mLvPrograms.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				if (mProgramName.compareTo("Weekly Programs") == 0) {
					processOnItemClick(position);
				}
			}
		});

		//Initialize swipe to refresh view
		mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
		mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_red_dark,
				android.R.color.holo_green_dark,
				android.R.color.holo_blue_dark);
		mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				if (mRefreshInProgress)
					return;

				//Refreshing data from server
				populatePrograms();
			}
		});

		// shows the refreshView in begining - if we sent the network request.
		mSwipeRefreshLayout.post(new Runnable() {
			@Override
			public void run() {
				if (mRefreshInProgress)
					mSwipeRefreshLayout.setRefreshing(true);
			}
		});

		return view;
	}
	
	@Override
	public void processJsonObject(String programName, JSONObject response) {
		if (mSwipeRefreshLayout.isRefreshing()) {
			mSwipeRefreshLayout.setRefreshing(false);
		}

		if(response == null){
			// display error.
			// improvement: try to read the old data from the database....
			return;
		}

		try{
			mProgramName = response.getString("title");
			JSONArray ProgramsJson = response.getJSONArray("entry");
			//List<SabaProgram> programs = null;
			if(mProgramName != null && mProgramName.compareToIgnoreCase("Weekly Programs") == 0){
				// parse weekly programs differently....
				List<List<DailyProgram>> weeklyPrograms = DailyProgram.fromJSONArray(programName, ProgramsJson);
				SabaProgram.fromWeeklyPrograms(mProgramName, weeklyPrograms);
			} else {
				mPrograms = SabaProgram.fromJSONArray(mProgramName, ProgramsJson);
			}
			addAll(mPrograms);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void processJsonObject(String programName, JSONArray response){
		mRefreshInProgress = false;
		if (mSwipeRefreshLayout.isRefreshing()) {
			mSwipeRefreshLayout.setRefreshing(false);
		}

		mProgramName = programName;
		//List<SabaProgram> programs = null;
		if(mProgramName != null && mProgramName.compareToIgnoreCase("Weekly Programs") == 0){
			// parse weekly programs differently....
			List<List<DailyProgram>> weeklyPrograms = DailyProgram.fromJSONArray(programName, response);
			mPrograms = SabaProgram.fromWeeklyPrograms(mProgramName, weeklyPrograms);
		} else {
			mPrograms = SabaProgram.fromJSONArray(mProgramName, response);
		}
		addAll(mPrograms);
		mAdapter.addAll(mPrograms);
	}
	
	// Delegate the adding to the internal adapter. // most recommended approach... minimize the code... 
	public void addAll(List<SabaProgram> programs){
		// delete existing records. We don't want to keep duplicate entries.
		 SabaProgram.deleteSabaPrograms(mProgramName);
		
		// save new/latest programs.
		for(final SabaProgram program : programs){
			program.saveProgram();
		}
	}
	
	// delete old data from the WeeklyProgram table and then save all newly retrieved weekly Programs.
	public void addAllWeeklyPrograms(List<List<DailyProgram>> programs){
		// delete existing records. We don't want to keep duplicate/old entries. 
		 DailyProgram.deletePrograms();
		
		// save new/latest programs.
		for(final List<DailyProgram> dailyPrograms : programs){
			for(final DailyProgram program : dailyPrograms){
				program.saveProgram();
			}	
		}
	}
		
	protected void setProgramName(String program){
		mProgramName = program;
	}
	
	protected String getProgramName(){
		return mProgramName;
	}
	
	protected abstract void populatePrograms();
	protected abstract void processOnItemClick(int position);

//	@Override
//	public void onDestroyView(){
//		super.onDestroyView();
//		Log.d(TAG, "onDestroyView: ********** " + mProgramName);
////		mSwipeRefreshLayout.setRefreshing(false);
////		mSabaClient.removeTarget(mProgramName, this);
////		mAdapter.clear();
////		mPrograms.clear();
//	}
//
//	@Override
//	public void onDetach(){
//		super.onDetach();
//		Log.d(TAG, "onDetach ********** " + mProgramName);
//		mSwipeRefreshLayout.setRefreshing(false);
//		mSabaClient.removeTarget(mProgramName, this);
//		mAdapter.clear();
//		mPrograms.clear();
//	}
}
