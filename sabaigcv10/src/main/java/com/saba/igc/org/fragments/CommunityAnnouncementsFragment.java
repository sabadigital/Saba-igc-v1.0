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
public class CommunityAnnouncementsFragment extends SabaBaseFragment {
	private final String PROGRAM_NAME = "Community Announcements";
	
	public CommunityAnnouncementsFragment(){
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// get programs from database. if program exists then display. otherwise make a network request.  
		mPrograms =  SabaProgram.getSabaPrograms(PROGRAM_NAME);
		if(mPrograms != null && mPrograms.size() == 0){
			// make a network request to pull the data from server.
			mSabaClient.getCommunityAnnouncements(this);
		} else {
			mProgramName = PROGRAM_NAME;
		}
	}

	public View onCreateView(LayoutInflater inflater,
							 @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
		getActivity().setTitle("Community");
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	protected void populatePrograms() {
		// TODO Auto-generated method stub
		mAdapter.clear();
		mSabaClient.getCommunityAnnouncements(this);
	}
	
	
	@Override
	protected void processOnItemClick(int position){
	}
}
