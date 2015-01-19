package com.saba.igc.org.fragments;

import im.delight.android.time.RelativeTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.saba.igc.org.R;
import com.saba.igc.org.activities.ProgramDetailActivity;
import com.saba.igc.org.adapters.ProgramsArrayAdapter;
import com.saba.igc.org.application.SabaApplication;
import com.saba.igc.org.application.SabaClient;
import com.saba.igc.org.listeners.SabaServerResponseListener;
import com.saba.igc.org.models.DailyProgram;
import com.saba.igc.org.models.SabaProgram;

import eu.erikw.PullToRefreshListView;
import eu.erikw.PullToRefreshListView.OnRefreshListener;

/**
 * @author Syed Aftab Naqvi
 * @create December, 2014
 * @version 1.0
 */
public abstract class SabaBaseFragment extends Fragment implements SabaServerResponseListener {

	protected SabaClient mSabaClient;
	protected ProgramsArrayAdapter mAdapter;
	protected List<SabaProgram> mPrograms;
	protected PullToRefreshListView mLvPrograms;
	protected ProgressBar mProgramsProgressBar;	
	protected String mProgramName;
	protected TextView mTvLastRrefreshedValue;

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
		View view = inflater.inflate(R.layout.activity_main, container, false);
		
		mProgramsProgressBar = (ProgressBar) view.findViewById(R.id.programsProgressBar);
        mLvPrograms = (PullToRefreshListView) view.findViewById(R.id.lvUpcomingPrograms);
        mTvLastRrefreshedValue = (TextView) view.findViewById(R.id.tvLastRrefreshedValue);
        
        if(mPrograms != null && mPrograms.size() == 0){
        	mPrograms = new ArrayList<SabaProgram>();
        } else {
        	mProgramsProgressBar.setVisibility(View.GONE);
        	
        	String format = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
			SimpleDateFormat sf = new SimpleDateFormat(format, Locale.ENGLISH);
			sf.setLenient(true);
		 
			try {
				long milliSeconds = sf.parse(mPrograms.get(0).getLastUpdated()).getTime();
				// e.g. formattedTime could now be <45 minutes ago>
				String formattedTime = RelativeTime.fromTimestamp(getResources(), milliSeconds);
				mTvLastRrefreshedValue.setText(formattedTime);
				
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        
		mAdapter = new ProgramsArrayAdapter(getActivity(), mPrograms);
		mLvPrograms.setAdapter(mAdapter);
		
		//OnItemClickListener
		mLvPrograms.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				
				if(mProgramName.compareTo("Weekly Programs")==0){
					processOnItemClick(position);
				} else {
					
					Intent intent = new Intent(getActivity(), ProgramDetailActivity.class);
					intent.putExtra("program", mPrograms.get(position));
					startActivity(intent);
				}
			}
		});
		
		mLvPrograms.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call listView.onRefreshComplete() when
                // once the network request has completed successfully.
            	populatePrograms();
            }
        });
		
		return view;
	}
	
	@Override
	public void processJsonObject(String programName, JSONObject response) {
		mLvPrograms.onRefreshComplete();
		mProgramsProgressBar.setVisibility(View.GONE);
		if(response == null){
			// display error.
			// improvement: try to read the old data from the database....
			return;
		}

		try{
			mProgramName = response.getString("title");
			JSONArray ProgramsJson = response.getJSONArray("entry");
			List<SabaProgram> programs = null;                                            
			if(mProgramName != null && mProgramName.compareToIgnoreCase("Weekly Programs") == 0){
				// parse weekly programs differently....
				List<List<DailyProgram>> weeklyPrograms = DailyProgram.fromJSONArray(programName, ProgramsJson);
				SabaProgram.fromWeeklyPrograms(mProgramName, weeklyPrograms);
			} else {
				programs = SabaProgram.fromJSONArray(mProgramName, ProgramsJson);
			}
			Log.d("TotalItems received: ", programs.size()+"");
			addAll(programs);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void processJsonObject(String programName, JSONArray response){
		mProgramsProgressBar.setVisibility(View.GONE);
		mLvPrograms.onRefreshComplete();
		if(mTvLastRrefreshedValue != null){
			String formattedTime;
			formattedTime = RelativeTime.fromTimestamp(getResources(), System.currentTimeMillis());
			// formattedTime could now be <45 minutes ago>
			mTvLastRrefreshedValue.setText(formattedTime);
		}
		if(response == null){
			// display error.
			return;
		}

		mProgramName = programName;
		List<SabaProgram> programs = null;
		if(mProgramName != null && mProgramName.compareToIgnoreCase("Weekly Programs") == 0){
			// parse weekly programs differently....
			List<List<DailyProgram>> weeklyPrograms = DailyProgram.fromJSONArray(programName, response);
			programs = SabaProgram.fromWeeklyPrograms(mProgramName, weeklyPrograms);
		} else {
			programs = SabaProgram.fromJSONArray(mProgramName, response);
		}
		Log.d("TotalItems received: ", programs.size()+"");
		addAll(programs);
	}
	
	// Delegate the adding to the internal adapter. // most recommended approach... minimize the code... 
	public void addAll(List<SabaProgram> programs){
		mAdapter.addAll(programs);
		
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
}
