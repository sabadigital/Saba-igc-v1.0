package com.saba.igc.org.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.saba.igc.org.models.SabaProgram;

/**
 * @author Syed Aftab Naqvi
 * @create December, 2014
 * @version 1.0
 */
public class UpcomingProgramsFragment extends SabaBaseFragment {
	private final String PROGRAM_NAME = "Upcoming Programs";
	private final String TAG = "UpcomingProgramsFragment"; 
	
	public UpcomingProgramsFragment(){
	}

	public View onCreateView(LayoutInflater inflater,
							 @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
		getActivity().setTitle("Announcements");
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// get programs from database. if program exists then display. otherwise make a network request.  
		mPrograms =  SabaProgram.getSabaPrograms(PROGRAM_NAME);
		if(mPrograms != null && mPrograms.size() == 0){
			// make a network request to pull the data from server.
			mSabaClient.getUpcomingPrograms(this);
		} else {
			mProgramName = PROGRAM_NAME;
		}
	}
	
	@Override
	protected void populatePrograms() {
		// TODO Auto-generated method stub
		mAdapter.clear();
		mSabaClient.getUpcomingPrograms(this);
	}
	
	@Override
	protected void processOnItemClick(int position){
	}
}


