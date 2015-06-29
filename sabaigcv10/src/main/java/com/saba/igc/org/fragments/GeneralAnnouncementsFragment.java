package com.saba.igc.org.fragments;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.saba.igc.org.R;
import com.saba.igc.org.models.SabaProgram;

/**
 * @author Syed Aftab Naqvi
 * @create December, 2014
 * @version 1.0
 */
public class GeneralAnnouncementsFragment extends SabaBaseFragment {
	private final String PROGRAM_NAME = "General Announcements";
	
	public GeneralAnnouncementsFragment(){
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mProgramName = PROGRAM_NAME;

		// get programs from database. if program exists then display. otherwise make a network request.  
		mPrograms =  SabaProgram.getSabaPrograms(PROGRAM_NAME);
		if(mPrograms != null && mPrograms.size() == 0){
			// make a network request to pull the data from server.
			mSabaClient.getGeneralAnnouncements(this);
		}

		// helps to display menu in fragments.
		setHasOptionsMenu(true);
	}
	
	@Override
	protected void populatePrograms() {
		//mProgramsProgressBar.setVisibility(View.VISIBLE);
		mAdapter.clear();
		mSabaClient.getGeneralAnnouncements(this);
	}
	
	@Override
	protected void processOnItemClick(int position){
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
		switch (item.getItemId()) {
			case R.id.refreshFragment:
				populatePrograms();
				return true;
		}

		return super.onOptionsItemSelected(item);
	}
}